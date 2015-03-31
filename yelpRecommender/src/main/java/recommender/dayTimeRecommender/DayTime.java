package recommender.dayTimeRecommender;

public class DayTime {
	
	private int day;
	private int hour;
	private int  value;
	
	public DayTime(int day, int hour, int value) {
		this.day = day;
		this.hour = hour;
		this.value = value;
	}
	public int getDay() {
		return day;
	}
	public int getHour() {
		return hour;
	}
	public int getValue() {
		return value;
	}
}