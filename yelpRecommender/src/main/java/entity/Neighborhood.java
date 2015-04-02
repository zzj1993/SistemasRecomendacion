package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Neighborhood {
	
	private final String name;
	
	@JsonCreator
	public Neighborhood(@JsonProperty("name") String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}