package project.school;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/schools")
@CrossOrigin
class SchoolController {

	private final SchoolRepository schoolRepository;

	private SchoolController(SchoolRepository schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<School> findById(@PathVariable Long requestedId) {
		Optional<School> schoolOptional = schoolRepository.findById(requestedId);
		if (schoolOptional.isPresent()) {
			return ResponseEntity.ok(schoolOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	 @PostMapping
	 private ResponseEntity<Void> createSchool(@RequestBody School newSchoolRequest, UriComponentsBuilder ucb) {
		 School savedSchool = schoolRepository.save(newSchoolRequest);
		   URI locationOfNewSchool = ucb
		            .path("schools/{id}")
		            .buildAndExpand(savedSchool.getId())
		            .toUri();
		   return ResponseEntity.created(locationOfNewSchool).build();
	 }

}