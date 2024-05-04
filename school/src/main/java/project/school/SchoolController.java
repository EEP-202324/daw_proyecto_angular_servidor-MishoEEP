package project.school;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
@CrossOrigin
class SchoolController {
	
	private SchoolRepository schoolRepository;
	
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
	
	
	
}