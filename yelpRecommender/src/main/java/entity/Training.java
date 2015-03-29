package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Training {
	
	private final String name;
	private final int size;
	
	@JsonCreator
	public Training(@JsonProperty("name") String name, @JsonProperty("size") int size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}
}
