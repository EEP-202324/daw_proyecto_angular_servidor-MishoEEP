package project.school;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends CrudRepository<School, Long>, PagingAndSortingRepository<School, Long> {

}
