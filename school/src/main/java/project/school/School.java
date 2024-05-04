package project.school;



import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class School {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String city;
	private String rating;

	public School(Long id, String name, String city, String rating) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.rating = rating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

}
