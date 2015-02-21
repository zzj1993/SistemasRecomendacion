package uniandes.recomendadorPeliculas.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {

	private final Long id;
	private final String title;
	private final String genres;
	
	@JsonCreator
	public Movie(@JsonProperty("id") Long id,
			@JsonProperty("title") String title,
			@JsonProperty("genres") String genres) {
		this.id = id;
		this.title = title;
		this.genres = genres;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getGenres() {
		return genres;
	}
}