package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Session {

	private long id;
	private UserInformation userInformation;

	public Session(@JsonProperty("id") long id,
			@JsonProperty("userInformation") UserInformation userInformation) {
		this.id = id;
		this.userInformation = userInformation;
	}

	public long getId() {
		return id;
	}

	public UserInformation getUserInformation() {
		return userInformation;
	}
}
