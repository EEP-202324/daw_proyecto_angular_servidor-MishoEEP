package project.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;

@JsonTest
class SchoolJsonTest {

	@Autowired
	private JacksonTester<School> json;

	@Autowired
	private JacksonTester<School[]> jsonList;

	private School[] schools;

	@BeforeEach
	void setUp() {
		schools = Arrays.array(new School(1L, "EEP", "Madrid", 7), new School(2L, "ESNE", "Madrid", 9),
				new School(3L, "CESUR", "Madrid", 5));
	}

	@Test
	void schoolSerializationTest() throws IOException {
		School school = new School(1L, "EEP", "Madrid", 7);
		assertThat(json.write(school)).isStrictlyEqualToJson("single.json");
		assertThat(json.write(school)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(school)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
		assertThat(json.write(school)).hasJsonPathStringValue("@.name");
		assertThat(json.write(school)).extractingJsonPathStringValue("@.name").isEqualTo("EEP");
		assertThat(json.write(school)).hasJsonPathStringValue("@.city");
		assertThat(json.write(school)).extractingJsonPathStringValue("@.city").isEqualTo("Madrid");
		assertThat(json.write(school)).hasJsonPathNumberValue("@.rating");
		assertThat(json.write(school)).extractingJsonPathNumberValue("@.rating").isEqualTo(7);
	}

	@Test
	void schoolDeserializationTest() throws IOException {
		String expected = """
				{
				    "id": 1,
				    "name": "EEP",
				    "city": "Madrid",
					"rating": 7
				     }
				      """;
		School parsedSchool = json.parseObject(expected);
		assertThat(parsedSchool.getId()).isEqualTo(1);
		assertThat(parsedSchool.getName()).isEqualTo("EEP");
		assertThat(parsedSchool.getCity()).isEqualTo("Madrid");
		assertThat(parsedSchool.getRating()).isEqualTo(7);
	}

	@Test
	void cashCardListSerializationTest() throws IOException {
		assertThat(jsonList.write(schools)).isStrictlyEqualToJson("list.json");
	}
}
