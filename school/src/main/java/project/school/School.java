package project.school;

public class School {
	 private int id;
	 private String name;
	 private String city;
	 private String rating;
	 
	public School(int id, String name, String city, String rating) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
