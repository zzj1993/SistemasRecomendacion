package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ratting {

	private final Long user;
	private final Long item;
	private final Integer ratting;

	@JsonCreator
	public Ratting(@JsonProperty("user") Long user,
			@JsonProperty("item") Long item,
			@JsonProperty("ratting") Integer ratting) {

		this.user = user;
		this.item = item;
		this.ratting = ratting;
	}

	public Long getUser() {
		return user;
	}

	public Long getItem() {
		return item;
	}

	public Integer getRatting() {
		return ratting;
	}
}
