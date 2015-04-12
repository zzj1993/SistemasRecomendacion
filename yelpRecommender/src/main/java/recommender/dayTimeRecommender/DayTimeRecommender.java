package recommender.dayTimeRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.utils.RecommendersInformation;
import recommender.utils.RecommendersUtils;
import business.Recommenders;
import entity.Prediction;
import entity.ReviewCF;

public class DayTimeRecommender {

	private final RecommendersInformation recommendersInformation;
	private final ItemRecommender itemRecommender;
	private final CollaborativeRecommender collaborativeRecommender;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private double rmse;
	private double mae;
	private int trainingCount;
	private int trainingTotal;
	private String lastRecommender;

	public DayTimeRecommender(RecommendersInformation recommendersInformation, ItemRecommender itemRecommender,
			CollaborativeRecommender collaborativeRecommender) {
		this.recommendersInformation = recommendersInformation;
		this.itemRecommender = itemRecommender;
		this.collaborativeRecommender = collaborativeRecommender;
	}

	public void buildDataModel(String recommender) {
		lastRecommender = recommender;
		trainingCount = 0;
		trainingTotal = recommendersInformation.getRandomUsers().size();
		long ini = System.currentTimeMillis();
		System.out.println("Training DayTime Recommender");
		rmseMae();
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
		System.out.println("DayTimeRecommender: End Training");
		trainingCount = trainingTotal;
	}

	private void rmseMae() {
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		List<ReviewCF> estimatedReviews = new ArrayList<ReviewCF>();
		double sumaR = 0D;
		double sumaM = 0D;

		for (int a = 0; a < reviews.size(); a++) {
			ReviewCF r = reviews.get(a);
			int time = getTime();
			int day = getDay();
			double estimate = getSimilarity(r.getBusinessId(), day, time);
			estimatedReviews.add(new ReviewCF(r.getBusinessId(), r.getUserId(), r.getStars(), (int) estimate, r.getItemStars()));
		}
		estimatedReviews = new ArrayList<ReviewCF>(RecommendersUtils.normalizeReviewsScore(estimatedReviews));

		for (int a = 0; a < estimatedReviews.size(); a++) {
			ReviewCF r = estimatedReviews.get(a);
			sumaR += ((r.getComputedStars() - (double) r.getStars()) * (r.getComputedStars() - (double) r.getStars()));
			sumaM += Math.abs((r.getComputedStars() - (double) r.getStars()));
		}

		rmse = Math.sqrt(sumaR / (double) reviews.size());
		mae = Math.sqrt(sumaM / (double) reviews.size());
	}

	public List<Prediction> recommendItems(String userId, int size, int day, int time) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		List<Prediction> recommendations;

		if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
			recommendations = collaborativeRecommender.recommendItems(userId);
		} else {
			recommendations = itemRecommender.recommendItems(userId, size);
		}

		for (Prediction p : recommendations) {
			String business = p.getKey();
			double value = getSimilarity(business, day, time);
			result.add(new Prediction(business, value));
		}
		Collections.sort(result);
		recommendationCount++;
		recommendationTime += System.currentTimeMillis() - ini;
		result = new ArrayList<Prediction>(RecommendersUtils.normalizeScore(result));
		return result;
	}

	public double getSimilarity(String businessId, int day, int time) {
		List<DayTime> dayTime = recommendersInformation.getBusinessDayTime(businessId);
		double dayScore = 0.0;
		double timeScore = 0.0;
		if (dayTime != null) {
			for (DayTime d : dayTime) {
				if (day == d.getDay()) {
					dayScore += d.getValue();
					if (time == d.getHour()) {
						timeScore += 2 * (double) d.getValue();
					} else {
						timeScore += 0.5 * (double) d.getValue();
					}
				}
			}
		}
		dayScore *= 0.1;
		double score = dayScore + timeScore;
		return 5.0 / (1 + Math.exp(-score + 2.0D));
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;

		for (String u : randomUsers) {
			int time = getTime();
			int i = getDay();
			List<Prediction> items = recommendItems(u, 10, i, time);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			if (items != null && !items.isEmpty()) {
				precision += (double) goodRecommendations / (double) items.size();
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			trainingCount++;
			getTrainingProgress();
		}

		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
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
		return (double) trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}

	public int getTrainingProgress() {
		int result = (trainingCount * 100 / trainingTotal);
		System.out.println("Day Time Recommender: " + result + "%");
		return result;
	}

	public String getLastRecommender() {
		return lastRecommender;
	}
}
