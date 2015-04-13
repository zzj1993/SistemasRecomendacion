package recommender.hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.text.TextRecommender;
import recommender.utils.RecommendersInformation;
import recommender.utils.RecommendersUtils;
import entity.Prediction;
import entity.ReviewCF;

public class HybridRecommender {

	private final NeighborhoodRecommender neighborhoodRecommender;
	private final DayTimeRecommender dayTimeRecommender;
	private final TextRecommender textRecommender;
	private final RecommendersInformation recommendersInformation;

	private double nWeight;
	private double dtWeight;
	private double tWeight;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private double rmse;
	private double mae;
	private int trainingCount;
	private int trainingTotal;

	public HybridRecommender(NeighborhoodRecommender neighborhoodRecommender, DayTimeRecommender dayTimeRecommender,
			RecommendersInformation recommendersInformation, TextRecommender textRecommender) {
		this.neighborhoodRecommender = neighborhoodRecommender;
		this.dayTimeRecommender = dayTimeRecommender;
		this.recommendersInformation = recommendersInformation;
		this.textRecommender = textRecommender;
	}

	public void init(double nWeight, double dtWeight, double tWeight) {
		this.nWeight = nWeight;
		this.dtWeight = dtWeight;
		this.tWeight = tWeight;
		trainingCount = 0;
		trainingTotal = recommendersInformation.getRandomUsers().size();
		long ini = System.currentTimeMillis();
		RMSEMAE();
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
		trainingCount = trainingTotal;
	}

	public List<Prediction> recommendItems(String userId, String neighborhood, int size, int day, int time, String text) {
		long ini = System.currentTimeMillis();
		List<Prediction> neighborhoodRecommendations = neighborhoodRecommender.recommendItems(userId, neighborhood, size);
		if(neighborhoodRecommendations != null && !neighborhoodRecommendations.isEmpty()){
			int aSize = neighborhoodRecommendations.size() > 1 ? 1 : neighborhoodRecommendations.size(); 
			neighborhoodRecommendations = neighborhoodRecommendations.subList(0, aSize);
		}
		
		List<Prediction> dayTimeRecommendations = dayTimeRecommender.recommendItems(userId, size, day, time);
		if(dayTimeRecommendations != null && !dayTimeRecommendations.isEmpty()){
			int aSize = dayTimeRecommendations.size() > 4 ? 4 : dayTimeRecommendations.size();
			dayTimeRecommendations = dayTimeRecommendations.subList(0, aSize);
		}
		
		List<Prediction> textRecommendations = textRecommender.recommendItems(text);
		if(textRecommendations != null && !textRecommendations.isEmpty()){
			int aSize = textRecommendations.size() > 5 ? 5 : textRecommendations.size();
			textRecommendations = textRecommendations.subList(0, aSize);
		}
		
		
		HashSet<Prediction> predictions = new HashSet<Prediction>();

		for (int i = 0; i < neighborhoodRecommendations.size(); i++) {
			Prediction p = neighborhoodRecommendations.get(i);
			predictions.add(new Prediction(p.getKey(), p.getValue() * nWeight));
		}

		for (int i = 0; i < dayTimeRecommendations.size(); i++) {
			Prediction p = dayTimeRecommendations.get(i);
			predictions.add(new Prediction(p.getKey(), p.getValue() * dtWeight));
		}

		for (int i = 0; i < textRecommendations.size(); i++) {
			Prediction p = textRecommendations.get(i);
			predictions.add(new Prediction(p.getKey(), p.getValue() * tWeight));
		}

		List<Prediction> result = new ArrayList<Prediction>(predictions);
		Collections.sort(result);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		result = new ArrayList<Prediction>(RecommendersUtils.normalizeScore(result));
		return result;
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		List<String> neighborhoods = recommendersInformation.getNeighborhoods(5);
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		Random r = new Random();
		for (String u : randomUsers) {
			int time = getTime();
			int day = getDay();
			String n = neighborhoods.get(r.nextInt(neighborhoods.size()));
			List<Prediction> items = recommendItems(u, n, 10, day, time, "");
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			precision += (double) goodRecommendations / (double) items.size();
			recall += (double) goodRecommendations / (double) goodBusiness;
			trainingCount++;
			getTrainingProgress();
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
		List<ReviewCF> estimatedReviews = new ArrayList<ReviewCF>();
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		for (ReviewCF r : reviews) {
			int time = getTime();
			int day = getDay();
			double estimate = dayTimeRecommender.getSimilarity(r.getBusinessId(), day, time) * dtWeight;
			double nEstimate = neighborhoodRecommender.estimatePreference(
					recommendersInformation.getUserGeneratedId(r.getUserId()),
					recommendersInformation.getBusinessGeneratedId(r.getBusinessId()))
					* nWeight;
			estimate += nEstimate;
			estimatedReviews.add(new ReviewCF(r.getBusinessId(), r.getUserId(), r.getStars(), (int) estimate, r.getItemStars()));
		}

		estimatedReviews = new ArrayList<ReviewCF>(RecommendersUtils.normalizeReviewsScore(estimatedReviews));

		for (int a = 0; a < estimatedReviews.size(); a++) {
			ReviewCF r = estimatedReviews.get(a);
			sumaRMSE += dtWeight * nWeight
					* ((r.getComputedStars() - (double) r.getStars()) * (r.getComputedStars() - (double) r.getStars()));
			sumaMAE += dtWeight * nWeight * Math.abs((r.getComputedStars() - (double) r.getStars()));
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

	public int getTrainingProgress() {
		int result = (trainingCount * 100 / trainingTotal);
		System.out.println("Hybrid Recommender: " + result + "%");
		return result;
	}
}