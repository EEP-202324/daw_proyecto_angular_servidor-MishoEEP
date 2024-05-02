package project.school;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class SchoolJsonTest {
	@Autowired
	private JacksonTester<School> json;

	@Test
	void schoolSerializationTest() throws IOException {
		School school = new School(1, "EEP", "Madrid", "7");
		assertThat(json.write(school)).isStrictlyEqualToJson("expected.json");
		assertThat(json.write(school)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(school)).extractingJsonPathNumberValue("@.id")
			.isEqualTo(1);
		assertThat(json.write(school)).hasJsonPathStringValue("@.name");
		assertThat(json.write(school)).extractingJsonPathStringValue("@.name")
			.isEqualTo("EEP");
		assertThat(json.write(school)).hasJsonPathStringValue("@.city");
		assertThat(json.write(school)).extractingJsonPathStringValue("@.city")
			.isEqualTo("Madrid");
		assertThat(json.write(school)).hasJsonPathStringValue("@.rating");
		assertThat(json.write(school)).extractingJsonPathStringValue("@.rating")
			.isEqualTo("7");
	}

}
