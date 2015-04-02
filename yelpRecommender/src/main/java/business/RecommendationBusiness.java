package business;

import java.util.ArrayList;
import java.util.List;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.utils.RecommendersInformation;
import entity.Business;
import entity.Prediction;
import entity.Recommendation;

public class RecommendationBusiness {

	private final RecommendersInformation recommendersInformation;
	private final CollaborativeRecommender collaborativeRecommender;
	private final ItemRecommender itemRecommender;
	private final NeighborhoodRecommender nRecommender;
	private final DayTimeRecommender dayTimeRecommender;

	public RecommendationBusiness(RecommendersInformation recommendersInformation,
			CollaborativeRecommender collaborativeRecommender, NeighborhoodRecommender nRecommender,
			DayTimeRecommender dayTimeRecommender, ItemRecommender itemRecommender) {
		this.collaborativeRecommender = collaborativeRecommender;
		this.recommendersInformation = recommendersInformation;
		this.nRecommender = nRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.itemRecommender = itemRecommender;
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

	public List<Recommendation> getDayTimeRecommendations(String userId, String neighborhood, int day, int time) {
		List<Prediction> predictions = dayTimeRecommender.recommendItems(userId, neighborhood, 10, day, time);
		List<Recommendation> recommendations = getRecommendations(predictions, userId);
		return recommendations;
	}
	
	private List<Recommendation> getRecommendations(List<Prediction> predictions, String userId){
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
}