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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
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
	                        new Object[] {1, "EEP", "Madrid", 7},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
	    jdbcTemplate.update("INSERT INTO school (id, name, city, rating) VALUES (?, ?, ?, ?)",
	                        new Object[] {2, "ESNE", "Madrid", 9},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
	    jdbcTemplate.update("INSERT INTO school (id, name, city, rating) VALUES (?, ?, ?, ?)",
	                        new Object[] {3, "CESUR", "Madrid", 5},
	                        new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
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

		Number rating = documentContext.read("$.rating");
		assertThat(rating).isEqualTo(7);
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
	   School newSchool = new School(null, "ESNE", "Madrid", 9);
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
	     assertThat(ratings).containsExactlyInAnyOrder(7, 9, 5);
	 }
	 
	 @Test
	 void shouldReturnAPageOfSchools() {
	     ResponseEntity<String> response = restTemplate.getForEntity("/schools?page=0&size=1", String.class);
	     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	     DocumentContext documentContext = JsonPath.parse(response.getBody());
	     JSONArray page = documentContext.read("$[*]");
	     assertThat(page.size()).isEqualTo(1);
	 }
	 
	 @Test
	 void shouldReturnASortedPageOfSchools() {
	     ResponseEntity<String> response = restTemplate.getForEntity("/schools?page=0&size=1&sort=rating,asc", String.class);
	     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	     DocumentContext documentContext = JsonPath.parse(response.getBody());
	     JSONArray read = documentContext.read("$[*]");
	     assertThat(read.size()).isEqualTo(1);

	     Integer rating = documentContext.read("$[0].rating");
	     assertThat(rating).isEqualTo(5);
	 }
	 
	 @Test
	 void shouldReturnASortedPageOfSchoolsWithNoParametersAndUseDefaultValues() {
	     ResponseEntity<String> response = restTemplate.getForEntity("/schools", String.class);
	     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	     DocumentContext documentContext = JsonPath.parse(response.getBody());
	     JSONArray page = documentContext.read("$[*]");
	     assertThat(page.size()).isEqualTo(3);

	     JSONArray ratings = documentContext.read("$..rating");
	     assertThat(ratings).containsExactly(5, 7, 9);
	 }
	 
	 @Test
	 @DirtiesContext
	 void shouldUpdateAnExistingSchool() {
		    School schoolUpdate = new School(null , "", "", 8);
		    HttpEntity<School> request = new HttpEntity<>(schoolUpdate);
		    ResponseEntity<Void> response = restTemplate.exchange("/schools/1", HttpMethod.PUT, request, Void.class);
		    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		    
		    ResponseEntity<String> getResponse = restTemplate.getForEntity("/schools/1", String.class);
		    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		    Number id = documentContext.read("$.id");
		    String name = documentContext.read("$.name");
		    String city = documentContext.read("$.city");
		    Integer rating = documentContext.read("$.rating");
		    assertThat(id).isEqualTo(1);
		    assertThat(name).isEqualTo("EEP");
		    assertThat(city).isEqualTo("Madrid");
		    assertThat(rating).isEqualTo(8);
	 }
	 
	 @Test
	 @DirtiesContext
	 void shouldDeleteAnExistingSchool() {
	     ResponseEntity<Void> response = restTemplate
	             .exchange("/schools/1", HttpMethod.DELETE, null, Void.class);
	     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	     
	     ResponseEntity<String> getResponse = restTemplate
	             .getForEntity("/schools/1", String.class);
	     assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	 }
	 
	 @Test
	 void shouldNotDeleteASchoolThatDoesNotExist() {
	     ResponseEntity<Void> deleteResponse = restTemplate
	             .exchange("/schools/555", HttpMethod.DELETE, null, Void.class);
	     assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	 }

}
