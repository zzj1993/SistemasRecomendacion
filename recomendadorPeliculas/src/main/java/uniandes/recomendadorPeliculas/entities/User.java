package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {

	private String name;
	private String email;
	private String password;
	
	@JsonCreator
	public User(@JsonProperty("email") String email, @JsonProperty("name") String name, 
			@JsonProperty("password") String password) {
		
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}