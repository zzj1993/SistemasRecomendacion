package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {

	private final String user;
	private final Long item;
	private final Integer rating;

	@JsonCreator
	public Rating(@JsonProperty("user") String user,
			@JsonProperty("item") Long item,
			@JsonProperty("rating") Integer rating) {

		this.user = user;
		this.item = item;
		this.rating = rating;
	}

	public String getUser() {
		return user;
	}

	public Long getItem() {
		return item;
	}

	public Integer getRating() {
		return rating;
	}
}
