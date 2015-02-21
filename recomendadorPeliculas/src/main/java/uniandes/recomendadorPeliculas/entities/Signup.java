package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Signup {
	
	private final String email;
	private final String password;
	private final String name;

	@JsonCreator
	public Signup(@JsonProperty("email") String email, @JsonProperty("password") String password, 
			@JsonProperty("name") String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
}