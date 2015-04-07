package recommender.dayTimeRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.utils.RecommendersInformation;
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
	private int trainingProgress;
	private double randomUsers;
	private double rmseMaeSize;
	private String lastRecommender;

	public DayTimeRecommender(RecommendersInformation recommendersInformation, double randomUsers, double rmseMaeSize,
			ItemRecommender itemRecommender, CollaborativeRecommender collaborativeRecommender) {
		this.recommendersInformation = recommendersInformation;
		this.randomUsers = randomUsers;
		this.rmseMaeSize = rmseMaeSize;
		this.itemRecommender = itemRecommender;
		this.collaborativeRecommender = collaborativeRecommender;
	}

	public void buildDataModel(String recommender) {
		lastRecommender = recommender;
		trainingProgress = 0;
		long ini = System.currentTimeMillis();
		System.out.println("Training DayTime Recommender");
		precisionRecall();
		rmseMae();
		trainingTime = System.currentTimeMillis() - ini;
		System.out.println("DayTimeRecommender: End Training");
		trainingProgress = 100;
	}

	private void rmseMae() {
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		double sumaR = 0D;
		double sumaM = 0D;
		int size = (int) ((rmseMaeSize * reviews.size()) / 100);
		for (int a = 0; a < size; a++) {
			ReviewCF r = reviews.get(a);
			double iniValue;
			if (lastRecommender.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
				iniValue = collaborativeRecommender.estimatePreference(r.getUserId(), r.getBusinessId());
			} else {
				iniValue = itemRecommender.estimatePreference(recommendersInformation.getBusinessGeneratedId(r.getBusinessId()),
						recommendersInformation.getUserGeneratedId(r.getUserId()));
			}

			if (iniValue == Double.NaN || Double.compare(Double.NaN, iniValue) == 0 || Double.isNaN(iniValue)) {
				iniValue = 0.0;
			}
			for (int i = 1; i < 4; i++) {
				for (int j = 0; j < 10; j++) {
					int time = getTime();
					double estimate = getSimilarity(r.getBusinessId(), i, time);
					sumaR += (((iniValue + estimate) - (double) r.getStars()) * ((iniValue + estimate) - (double) r.getStars()));
					sumaM += Math.abs(((iniValue + estimate) - (double) r.getStars()));
				}
			}
		}
		rmse = Math.sqrt(sumaR / ((double) size * 40.0));
		mae = Math.sqrt(sumaM / ((double) size * 40.0));
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
			result.add(new Prediction(business, p.getValue() + value));
		}
		Collections.sort(result);
		recommendationCount++;
		recommendationTime += System.currentTimeMillis() - ini;
		return result;
	}

	private double getSimilarity(String businessId, int day, int time) {
		List<DayTime> dayTime = recommendersInformation.getBusinessDayTime(businessId);
		double dayScore = 0.0;
		double timeScore = 0.0;
		if (dayTime != null) {
			for (DayTime d : dayTime) {
				if (day == d.getDay() && time == d.getHour()) {
					dayScore += 0.5;
					timeScore += 0.1 * (double) d.getValue();
					break;
				}
			}
		}
		if (Double.compare(dayScore + timeScore, 0.0) == 0) {
			return 0;
		}
		return Math.log(dayScore + timeScore) / 15.0;
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers(this.randomUsers);
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int a = 0;
		for (String u : randomUsers) {
			int time = getTime();
			int i = getDay();
			List<Prediction> items = recommendItems(u, 10, i, time);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			if(items != null && !items.isEmpty()){
				precision += (double) goodRecommendations / (double) items.size();
			}
			recall += (double) goodRecommendations / (double) goodBusiness;
			trainingProgress = a * 100 / randomUsers.size();
			System.out.println("Day Time Recommender: Precision Recall: " + a + " de " + randomUsers.size());
			a++;
		}

		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
		trainingProgress = 100;
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
		return trainingProgress;
	}

	public String getLastRecommender() {
		return lastRecommender;
	}
}
