package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieRating {
	private final Long id;
	private final String title;
	private final String genres;
	private final Integer avgRating;
	
	@JsonCreator
	public MovieRating(@JsonProperty("id") Long id,
			@JsonProperty("title") String title,
			@JsonProperty("genres") String genres, @JsonProperty("avgRating") Integer avgRating) {
		this.id = id;
		this.title = title;
		this.genres = genres;
		this.avgRating = avgRating;
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

	public Integer getAvgRating() {
		return avgRating;
	}
}
