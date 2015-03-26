package entity;

public class ReviewCF {

	private final String businessId;
	private final String userId;
	private final int stars;
	private final double computedStars;

	public ReviewCF(String businessId, String userId, int stars, double computedStars) {
		this.businessId = businessId;
		this.userId = userId;
		this.stars = stars;
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

	public double getComputedStars() {
		return computedStars;
	}
}