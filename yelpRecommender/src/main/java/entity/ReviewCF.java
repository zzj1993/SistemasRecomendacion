package entity;

public class ReviewCF {

	private final String businessId;
	private final String userId;
	private final int stars;
	private final double businessStars;
	private final double userStars;
	private final double computedStars;

	public ReviewCF(String businessId, String userId, int stars,
			double businessStars, double userStars, double computedStars) {
		super();
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
		this.businessStars = businessStars;
		this.userStars = userStars;
		this.computedStars = computedStars;
	}

	public String getBusinessId() {
		return businessId;
	}

	public String getUserId() {
		return userId;
	}

	public int getStars() {
		return stars;
	}

	public double getBusinessStars() {
		return businessStars;
	}

	public double getUserStars() {
		return userStars;
	}

	public double getComputedStars() {
		return computedStars;
	}
}