package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {

	private final Long user;
	private final Long item;
	private final Integer rating;

	@JsonCreator
	public Rating(@JsonProperty("userId") Long user,
			@JsonProperty("item") Long item,
			@JsonProperty("rating") Integer rating) {

		this.user = user;
		this.item = item;
		this.rating = rating;
	}

	public Long getUser() {
		return user;
	}

	public Long getItem() {
		return item;
	}

	public Integer getRating() {
		return rating;
	}
}
