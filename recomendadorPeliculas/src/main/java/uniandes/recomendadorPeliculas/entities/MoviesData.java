package uniandes.recomendadorPeliculas.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoviesData {

	private final List<Ratting> rattings;
	private final List<Movie> movies;

	@JsonCreator
	public MoviesData(@JsonProperty("rattings") List<Ratting> rattings,
			@JsonProperty("movies") List<Movie> movies) {
		super();
		this.rattings = new ArrayList<Ratting>(rattings);
		this.movies = new ArrayList<Movie>(movies);
	}

	public List<Ratting> getRattings() {
		return rattings;
	}

	public List<Movie> getMovies() {
		return movies;
	}
}
