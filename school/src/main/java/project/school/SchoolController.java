package project.school;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping
	private ResponseEntity<List<School>> findAll(Pageable pageable) {
	    Page<School> page = schoolRepository.findAll(
	    		PageRequest.of(
	                    pageable.getPageNumber(),
	                    pageable.getPageSize(),
	                    pageable.getSortOr(Sort.by(Sort.Direction.DESC, "rating"))
	    ));
	    return ResponseEntity.ok(page.getContent());
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
		URI locationOfNewSchool = ucb.path("schools/{id}").buildAndExpand(savedSchool.getId()).toUri();
		return ResponseEntity.created(locationOfNewSchool).build();
	}

	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putSchool(@PathVariable Long requestedId, @RequestBody School schoolUpdate) {
	    Optional<School> schoolOptional = schoolRepository.findById(requestedId);

	    if (schoolOptional.isPresent()) {
	        School existingSchool = schoolOptional.get();

	        if (!"".equals(schoolUpdate.getName())) {
	            existingSchool.setName(schoolUpdate.getName());
	        }
	        if (!"".equals(schoolUpdate.getCity())) {
	            existingSchool.setCity(schoolUpdate.getCity());
	        }
	        if (schoolUpdate.getRating() != null && schoolUpdate.getRating() != 0) {
	            existingSchool.setRating(schoolUpdate.getRating());
	        }

	        schoolRepository.save(existingSchool);
	        return ResponseEntity.noContent().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
		if (!schoolRepository.existsById(id)) {
	        return ResponseEntity.notFound().build();
	    }
		schoolRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	private ResponseEntity<List<School>> searchSchools(@RequestParam("name") String name) {
	    if (name == null || name.trim().isEmpty()) {
	        return ResponseEntity.ok(List.of());
	    }
	    List<School> schools = schoolRepository.findByNameContainingIgnoreCase(name);
	    return ResponseEntity.ok(schools);
	}
}