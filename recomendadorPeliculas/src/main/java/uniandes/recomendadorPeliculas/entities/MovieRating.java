package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieRating {
	private final Long id;
	private final String title;
	private final String genres;
	private final Integer avgRating;
	private final Boolean changed;
	
	@JsonCreator
	public MovieRating(@JsonProperty("id") Long id,
			@JsonProperty("title") String title,
			@JsonProperty("genres") String genres, @JsonProperty("avgRating") Integer avgRating,
			@JsonProperty("changed") Boolean changed) {
		this.id = id;
		this.title = title;
		this.genres = genres;
		this.avgRating = avgRating;
		this.changed = changed;
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

	public Boolean getChanged() {
		return changed;
	}
}
