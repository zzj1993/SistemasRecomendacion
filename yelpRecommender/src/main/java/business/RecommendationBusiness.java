package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.hybrid.HybridRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.text.TextRecommender;
import recommender.utils.RecommendersInformation;
import entity.Business;
import entity.Prediction;
import entity.Recommendation;
import entity.ReviewCF;
import entity.ShortRecommendation;

public class RecommendationBusiness {

	private final RecommendersInformation recommendersInformation;
	private final CollaborativeRecommender collaborativeRecommender;
	private final ItemRecommender itemRecommender;
	private final NeighborhoodRecommender nRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	private final TextRecommender textRecommender;
	private final HybridRecommender hybridRecommender;

	public RecommendationBusiness(RecommendersInformation recommendersInformation,
			CollaborativeRecommender collaborativeRecommender, NeighborhoodRecommender nRecommender,
			DayTimeRecommender dayTimeRecommender, ItemRecommender itemRecommender, HybridRecommender hybridRecommender,
			TextRecommender textRecommender) {
		this.collaborativeRecommender = collaborativeRecommender;
		this.recommendersInformation = recommendersInformation;
		this.nRecommender = nRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.itemRecommender = itemRecommender;
		this.textRecommender = textRecommender;
		this.hybridRecommender = hybridRecommender;
	}

	public List<Recommendation> getCollaborativeRecommendations(String userId) {
		List<Prediction> predictions = collaborativeRecommender.recommendItems(userId);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}

	public List<Recommendation> getItemRecommendations(String userId) {
		List<Prediction> predictions = itemRecommender.recommendItems(userId, 10);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}

	public List<Recommendation> getNeighborhoodRecommendations(String userId, String neighborhood) {
		List<Prediction> predictions = nRecommender.recommendItems(userId, neighborhood, 10);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}

	public List<Recommendation> getDayTimeRecommendations(String userId, int day, int time) {
		List<Prediction> predictions = dayTimeRecommender.recommendItems(userId, 10, day, time);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}

	public List<ShortRecommendation> getUserRatings(String userId) {
		List<ShortRecommendation> result = new ArrayList<ShortRecommendation>();
		List<ReviewCF> revs = recommendersInformation.getUserReviews(userId);
		if (revs != null) {
			for (ReviewCF r : revs) {
				Business b = recommendersInformation.getBusinessInformation(r.getBusinessId());
				double colPref = collaborativeRecommender.estimatePreference(userId, r.getBusinessId());
				double itemPref = itemRecommender.estimatePreference(recommendersInformation.getUserGeneratedId(userId),
						recommendersInformation.getBusinessGeneratedId(r.getBusinessId()));
				if (Double.isNaN(itemPref))
					itemPref = 0;
				result.add(new ShortRecommendation(userId, r.getBusinessId(), b.getName(), r.getStars(), (int) itemPref,
						(int) colPref));
			}
		}
		return result;
	}

	private List<Recommendation> getRecommendations(List<Prediction> predictions, String userId) {
		List<Recommendation> recommendations = new ArrayList<Recommendation>();
		for (Prediction p : predictions) {
			String businessId = p.getKey();
			String userName = recommendersInformation.getUserName(userId);
			Business b = recommendersInformation.getBusinessInformation(businessId);
			Recommendation r = new Recommendation(businessId, b, userId, userName, 0, p.getValue());
			recommendations.add(r);
		}
		return recommendations;
	}

	public void deleteReview(String userId, String businessId) {
		recommendersInformation.deleteReview(userId, businessId);
		collaborativeRecommender.reTrain();
	}

	public void addRating(String userId, String businessId, int stars) {
		int colPref = (int) collaborativeRecommender.estimatePreference(userId, businessId);
		double itemPref = (double) itemRecommender.estimatePreference(recommendersInformation.getUserGeneratedId(userId),
				recommendersInformation.getBusinessGeneratedId(businessId));
		if(Double.isNaN(itemPref)){
			itemPref=0;
		}
		recommendersInformation.addRating(userId, businessId, stars, colPref, (int) itemPref);
	}

	public List<Recommendation> getHybridRecommendations(String userId, String neighborhood, int size, int day, int time) {
		List<Prediction> predictions = hybridRecommender.recommendItems(userId, neighborhood, size, day, time);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}
	
	public List<Recommendation> getTextRecommendations(String userId, String text) {
		List<Prediction> predictions = textRecommender.recommendItems(text);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}
}