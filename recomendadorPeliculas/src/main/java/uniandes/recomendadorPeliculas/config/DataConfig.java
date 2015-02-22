package uniandes.recomendadorPeliculas.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataConfig {
	@NotNull
	@NotEmpty
	@JsonProperty
	private String dir;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String movies;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String ratings;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String users;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String load;

	public String getDir() {
		return dir;
	}

	public String getMovies() {
		return movies;
	}

	public String getRatings() {
		return ratings;
	}

	public String getUsers() {
		return users;
	}

	public String getLoad() {
		return load;
	}
}
