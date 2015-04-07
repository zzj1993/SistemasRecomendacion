package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Comparable<User> {

	private final String id;
	private final String name;

	@JsonCreator
	public User(@JsonProperty("id") String id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int compareTo(User o) {
		if (o.name != null && name != null)
			return name.compareTo(o.name);
		return 1;
	}
}