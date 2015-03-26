package entity;

public class Prediction implements Comparable<Prediction> {
	
	private final String key;
	private final double value;
	
	public Prediction(String key, double value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public double getValue() {
		return value;
	}

	public int compareTo(Prediction o) {
		return Double.compare(o.value, value);
	}
}