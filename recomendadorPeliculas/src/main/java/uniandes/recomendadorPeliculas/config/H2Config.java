package uniandes.recomendadorPeliculas.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class H2Config {
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String driverClass;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String url;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String user;
	
	@NotNull
	@NotEmpty
	@JsonProperty
	private String password;
	

	public String getDriverClass() {
		return driverClass;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}