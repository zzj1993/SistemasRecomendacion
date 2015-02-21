package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
	
	private final String email;
	private final String password;

	@JsonCreator
	public Login(@JsonProperty("email") String email, @JsonProperty("password") String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}