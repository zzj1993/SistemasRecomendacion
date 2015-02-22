package uniandes.recomendadorPeliculas.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoviesData {

	private final List<Rating> ratings;
	private final List<Movie> movies;

	@JsonCreator
	public MoviesData(@JsonProperty("ratings") List<Rating> ratings,
			@JsonProperty("movies") List<Movie> movies) {
		super();
		this.ratings = new ArrayList<Rating>(ratings);
		this.movies = new ArrayList<Movie>(movies);
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public List<Movie> getMovies() {
		return movies;
	}
}
