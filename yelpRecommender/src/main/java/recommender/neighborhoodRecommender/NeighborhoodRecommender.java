package recommender.neighborhoodRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import business.Recommenders;
import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.utils.RecommendersInformation;
import entity.Prediction;

public class NeighborhoodRecommender {

	private final RecommendersInformation recommendersInformation;
	private final ItemRecommender itemRecommender;
	private final CollaborativeRecommender collaborativeRecommender;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private int trainingProgress;
	private double randomUsers;
	private double neighborhoodSize;
	private String lastRecommender;

	public NeighborhoodRecommender(RecommendersInformation recommendersInformation, ItemRecommender itemRecommender,
			double randomUsers, double neighborhoodSize, CollaborativeRecommender collaborativeRecommender) {
		this.recommendersInformation = recommendersInformation;
		this.itemRecommender = itemRecommender;
		this.collaborativeRecommender = collaborativeRecommender;
		this.randomUsers = randomUsers;
		this.neighborhoodSize = neighborhoodSize;
	}

	public void buildDataModel(String recommender) {
		lastRecommender = recommender;
		long ini = System.currentTimeMillis();
		trainingProgress = 0;
		System.out.println("Training Neighborhood Recommender");
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
		System.out.println("NeighborhoodRecommender: End Training");
		trainingProgress = 100;
	}

	public List<Prediction> recommendItems(String userId, String neighborhood, int size) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		List<String> businesses = recommendersInformation.getBusinessesInNeighborhood(neighborhood);
		if (businesses != null) {
			for (int i = 0; i < businesses.size(); i++) {
				double preference;
				if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
					preference = collaborativeRecommender.estimatePreference(userId, businesses.get(i));
				} else {
					preference = itemRecommender.estimatePreference(recommendersInformation.getUserGeneratedId(userId),
							recommendersInformation.getBusinessGeneratedId(businesses.get(i)));
				}
				result.add(new Prediction(businesses.get(i), preference));
			}
		} else if (businesses == null || businesses.isEmpty()) {
			if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
				result = collaborativeRecommender.recommendItems(userId);
			} else {
				result = itemRecommender.recommendItems(userId, size);
			}
		}
		Collections.sort(result);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		return result;
	}

	public double getRMSE() {
		if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
			return collaborativeRecommender.getRMSE();
		} else {
			return itemRecommender.getRMSE();
		}
	}

	public double getMAE() {
		if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
			return collaborativeRecommender.getMAE();
		} else {
			return itemRecommender.getMAE();
		}
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers(this.randomUsers);
		List<String> neighborhoods = recommendersInformation.getNeighborhoods(neighborhoodSize);
		precision = 0.0;
		recall = 0.0;
		int i = 1;
		for (String n : neighborhoods) {
			int goodBusiness = recommendersInformation.getAllGoodBusinessSizeInNeighborhood(n);
			for (String u : randomUsers) {
				int goodRecommendations = 0;
				List<Prediction> items = recommendItems(u, n, 10);
				for (Prediction p : items) {
					if (Double.compare(p.getValue(), 4.0D) >= 0) {
						goodRecommendations++;
					}
				}
				precision += (double) goodRecommendations / (double) items.size();
				if (goodBusiness != 0) {
					recall += (double) goodRecommendations / (double) goodBusiness;
				}
				System.out.println("Neighborhood Recommender: Precision Recall: " + i + " de " + randomUsers.size()
						* neighborhoods.size());
				trainingProgress = i * 100 / (randomUsers.size() * neighborhoods.size());
				i++;
			}
		}
		precision = precision / ((double) randomUsers.size() * neighborhoods.size());
		recall = recall / ((double) randomUsers.size() * neighborhoods.size());
		trainingProgress = 100;
	}

	public double getTrainingTime() {
		return (double) trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}

	public double estimatePreference(long userID, long itemID) {
		if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
			return collaborativeRecommender.estimatePreference(recommendersInformation.getUserId((int) userID),
					recommendersInformation.getBusinessId((int) itemID));
		} else {
			return itemRecommender.estimatePreference(userID, itemID);
		}
	}

	public int getTrainingProgress() {
		return trainingProgress;
	}
	
	public String getLastRecommender(){
		return lastRecommender;
	}
}