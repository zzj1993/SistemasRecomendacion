package recommender.hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.text.TextRecommender;
import recommender.utils.RecommendersInformation;
import entity.Prediction;
import entity.ReviewCF;

public class HybridRecommender {

	private final NeighborhoodRecommender neighborhoodRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	private final TextRecommender textRecommender;
	private final RecommendersInformation recommendersInformation;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private double rmse;
	private double mae;
	private double randomUsers;

	public HybridRecommender(NeighborhoodRecommender neighborhoodRecommender, DayTimeRecommender dayTimeRecommender,
			RecommendersInformation recommendersInformation, double randomUsers, TextRecommender textRecommender) {
		this.neighborhoodRecommender = neighborhoodRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.recommendersInformation = recommendersInformation;
		this.randomUsers = randomUsers;
		this.textRecommender = textRecommender;
	}

	public void init() {
		long ini = System.currentTimeMillis();
		RMSEMAE();
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
	}

	public List<Prediction> recommendItems(String userId, String neighborhood, int size, int day, int time) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		List<Prediction> neighborhoodRecommendations = neighborhoodRecommender.recommendItems(userId, neighborhood, size);
		List<Prediction> dayTimeRecommendations = dayTimeRecommender.recommendItems(userId, size, day, time);
		int maxSize = Math.max(neighborhoodRecommendations.size(), dayTimeRecommendations.size());
		for(int i = 0 ; i < maxSize && result.size() < 10 ; i++){
			if(i < neighborhoodRecommendations.size()){
				Prediction p = neighborhoodRecommendations.get(i);
				double val = p.getValue() + dayTimeRecommender.getSimilarity(p.getKey(), day, time);
				result.add(new Prediction(p.getKey(), val));
			}
			
			if(i < dayTimeRecommendations.size()){
				result.add(dayTimeRecommendations.get(i));
			}
		}

		Collections.sort(result);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		return result;
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers(this.randomUsers);
		List<String> neighborhoods = recommendersInformation.getNeighborhoods(5);
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int j = 0;
		Random r = new Random();
		for (String u : randomUsers) {
			int time = getTime();
			int day = getDay();
			String n = neighborhoods.get(r.nextInt(neighborhoods.size()));
			List<Prediction> items = recommendItems(u, n, 10, day, time);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			precision += (double) goodRecommendations / (double) items.size();
			recall += (double) goodRecommendations / (double) goodBusiness;
			System.out.println("Hybrid Recommender: Precision Recall: " + j + " de " + randomUsers.size());
			j++;
		}

		precision = precision / ((double) randomUsers.size());
		recall = recall / ((double) randomUsers.size());
	}

	private int getDay() {
		Random r = new Random();
		int time = r.nextInt(4);
		while (time == 0) {
			time = r.nextInt(4);
		}
		return time;
	}

	private int getTime() {
		Random r = new Random();
		int time = r.nextInt(24);
		if (0 <= time && time <= 5) {
			time = 0;
		} else if (6 <= time && time <= 9) {
			time = 6;
		} else if (10 <= time && time <= 15) {
			time = 10;
		} else if (16 <= time && time <= 19) {
			time = 16;
		} else {
			time = 20;
		}
		return time;
	}

	private void RMSEMAE() {
		rmse = 0.0D;
		mae = 0.0D;
		double sumaRMSE = 0D;
		double sumaMAE = 0D;
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		for (ReviewCF r : reviews) {
			double similarity = dayTimeRecommender.getSimilarity(r.getBusinessId(), getDay(), getTime());
			sumaRMSE += ((r.getComputedStars() + similarity - (double) r.getStars()) * (r.getComputedStars() + similarity - (double) r.getStars()));
			sumaMAE += Math.abs((r.getComputedStars() + similarity - (double) r.getStars()));
		}
		rmse = Math.sqrt(sumaRMSE / (double) reviews.size());
		mae = Math.sqrt(sumaMAE / (double) reviews.size());
	}

	public double getRMSE() {
		return rmse;
	}

	public double getMAE() {
		return mae;
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getTrainingTime() {
		return trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}
}