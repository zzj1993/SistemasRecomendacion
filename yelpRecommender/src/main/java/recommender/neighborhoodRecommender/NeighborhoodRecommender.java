package recommender.neighborhoodRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.FileGenerator;
import recommender.CollaborativeRecommender.ItemRecommender;
import entity.MeanCF;
import entity.Prediction;

public class NeighborhoodRecommender {

	private final ItemRecommender itemRecommender;
	private final CollaborativeRecommender cfRecommender;
	private final FileGenerator generator;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;

	public NeighborhoodRecommender(ItemRecommender itemRecommender, CollaborativeRecommender cfRecommender,
			FileGenerator generator) {
		this.itemRecommender = itemRecommender;
		this.cfRecommender = cfRecommender;
		this.generator = generator;
	}

	public void buildDataModel() {
		long ini = System.currentTimeMillis();
		System.out.println("Training Neighborhood Recommender");
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
	}

	public List<Prediction> recommendItems(String userId, String neighborhood, int size) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		List<String> businesses = generator.getBusinessInNeighbor(neighborhood);
		if (businesses != null) {
			for (int i = 0; i < businesses.size(); i++) {
				double preference = itemRecommender.estimatePreference(generator.getUserGeneratedId(userId),
						generator.getBusinessGeneratedId(businesses.get(i)));
				result.add(new Prediction(businesses.get(i), preference));
			}
		} else if (businesses == null || businesses.isEmpty()) {
			result = itemRecommender.recommendItems(userId, size);
		}
		Collections.sort(result);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		size = result.size() < size ? result.size() : size;
		return result.subList(0, size);
	}

	public double getRMSE() {
		return itemRecommender.getRMSE();
	}

	public double getMAE() {
		return itemRecommender.getMAE();
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	private void precisionRecall() {
		List<String> randomUsers = getUsers();
		List<String> neighborhoods = getNeighborhoods();
		precision = 0.0;
		recall = 0.0;
		for (String n : neighborhoods) {
			List<String> goodBusiness = getAllGoodBusiness(n);
			for (String u : randomUsers) {
				int goodRecommendations = 0;
				List<Prediction> items = recommendItems(u, n, 10);
				for (Prediction p : items) {
					if (Double.compare(p.getValue(), 4.0D) >= 0) {
						goodRecommendations++;
					}
				}
				precision += (double) goodRecommendations / 10.0D;
				if (!goodBusiness.isEmpty()) {
					recall += (double) goodRecommendations / (double) goodBusiness.size();
				} else {
					recall += 0;
				}
			}
		}
		precision = precision / ((double) randomUsers.size() * neighborhoods.size());
		recall = recall / ((double) randomUsers.size() * neighborhoods.size());
	}

	private List<String> getNeighborhoods() {
		List<String> neighborhoods = new ArrayList<String>(generator.getAllNeighborhoods());
		Random r = new Random();
		Collections.shuffle(neighborhoods);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < (3 * neighborhoods.size()) / 100; i++) {
			String k = neighborhoods.get(r.nextInt(neighborhoods.size()));
			result.add(k);
		}
		return result;
	}

	private List<String> getUsers() {
		List<String> users = new ArrayList<String>(generator.getAllUsers());
		Random r = new Random();
		Collections.shuffle(users);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < (3 * users.size()) / 100; i++) {
			String k = users.get(r.nextInt(users.size()));
			result.add(k);
		}
		return result;
	}

	private List<String> getAllGoodBusiness(String neighborhood) {
		List<String> result = new ArrayList<String>();
		List<String> businesses = generator.getBusinessInNeighbor(neighborhood);
		if (businesses != null) {
			for (int i = 0; i < (3 * businesses.size()) / 100; i++) {
				MeanCF m = cfRecommender.getBusinessMean(businesses.get(i));
				if (m != null) {
					double businesMean = m.getMean();
					if (Double.compare(businesMean, 4.0D) > 0) {
						result.add(businesses.get(i));
					}
				}
			}
		}
		return result;
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
		return itemRecommender.estimatePreference(userID, itemID);
	}
}
