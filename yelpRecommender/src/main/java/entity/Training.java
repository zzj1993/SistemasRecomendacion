package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Training {
	
	private final String name;
	private final int size;
	private final String correlation;
	
	@JsonCreator
	public Training(@JsonProperty("name") String name, @JsonProperty("size") int size, @JsonProperty("correlation") String correlation) {
		this.name = name;
		this.size = size;
		this.correlation = correlation;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public String getCorrelation() {
		return correlation;
	}
}
