package project.school;

import java.net.URI;
import java.sql.Types;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void resetDatabase() {
	    // First, clear out any existing data
	    jdbcTemplate.execute("TRUNCATE TABLE school");

	    // Then insert the test data
	    jdbcTemplate.update("INSERT INTO school (id, name, city, rating) VALUES (?, ?, ?, ?)",
	                        new Object[] {1, "EEP", "Madrid", "7"},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
	    jdbcTemplate.update("INSERT INTO school (id, name, city, rating) VALUES (?, ?, ?, ?)",
	                        new Object[] {2, "ESNE", "Madrid", "9"},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
	    jdbcTemplate.update("INSERT INTO school (id, name, city, rating) VALUES (?, ?, ?, ?)",
	                        new Object[] {3, "CESUR", "Madrid", "5"},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
	}
	

	@Test
	void shouldReturnSchoolWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/schools/1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(1);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("EEP");

		String city = documentContext.read("$.city");
		assertThat(city).isEqualTo("Madrid");

		String rating = documentContext.read("$.rating");
		assertThat(rating).isEqualTo("7");
	}

	@Test
	void shouldNotReturnASchoolWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/schools/555", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}
	
	@Test
	@DirtiesContext
	void shouldCreateANewSchool() {
	   School newSchool = new School(null, "ESNE", "Madrid", "9");
	   ResponseEntity<Void> createResponse = restTemplate.postForEntity("/schools", newSchool, Void.class);
	   assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	   
	   URI locationOfNewSchool = createResponse.getHeaders().getLocation();
	   ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewSchool, String.class);
	   assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	 @Test
	 void shouldReturnAllSchoolsWhenListIsRequested() {
		 ResponseEntity<String> response = restTemplate.getForEntity("/schools", String.class);
	     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	     DocumentContext documentContext = JsonPath.parse(response.getBody());
	     int schoolCount = documentContext.read("$.length()");
	     assertThat(schoolCount).isEqualTo(3);

	     JSONArray ids = documentContext.read("$..id");
	     assertThat(ids).containsExactlyInAnyOrder(1, 2, 3);

	     JSONArray names = documentContext.read("$..name");
	     assertThat(names).containsExactlyInAnyOrder("EEP", "ESNE", "CESUR");
	     
	     JSONArray cities = documentContext.read("$..city");
	     assertThat(cities).containsExactlyInAnyOrder("Madrid", "Madrid", "Madrid");
	     
	     JSONArray ratings = documentContext.read("$..rating");
	     assertThat(ratings).containsExactlyInAnyOrder("7", "9", "5");
	 }

}
