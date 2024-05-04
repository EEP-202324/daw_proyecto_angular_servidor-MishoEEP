package project.school;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

	    @Autowired
	    TestRestTemplate restTemplate;

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

}
