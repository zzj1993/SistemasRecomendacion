package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {

	private Long id;
	private Long userId;
	
	@JsonCreator
	public User(@JsonProperty("id") Long id, @JsonProperty("userId") Long userId) {
		
		this.id = id;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}
}