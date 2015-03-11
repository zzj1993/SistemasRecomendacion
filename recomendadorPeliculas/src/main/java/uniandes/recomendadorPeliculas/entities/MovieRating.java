package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieRating {
	private final Long id;
	private final String title;
	private final String genres;
	private final Integer avgRating;
	private Float predictedRating;
	
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

	public Float getPredictedRating() {
		return predictedRating;
	}

	public void setPredictedRating(Float predictedRating) {
		this.predictedRating = predictedRating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieRating other = (MovieRating) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}