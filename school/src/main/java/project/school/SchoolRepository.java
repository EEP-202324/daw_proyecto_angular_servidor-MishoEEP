package project.school;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends CrudRepository<School, Long>, PagingAndSortingRepository<School, Long> {

	@Query("SELECT s FROM School s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<School> findByNameContainingIgnoreCase(@Param("name") String name);

}
