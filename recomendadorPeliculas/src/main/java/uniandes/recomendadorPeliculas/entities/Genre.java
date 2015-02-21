package uniandes.recomendadorPeliculas.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Genre {

	private final String genre;
	
	@JsonCreator
	public Genre(@JsonProperty("genre") String genre){
		this.genre = genre;
	}

	public String getGenre() {
		return genre;
	}
}
