package recommender.dayTimeRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.FileGenerator;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import entity.MeanCF;
import entity.Prediction;
import entity.ReviewCF;

public class DayTimeRecommender {

	private final NeighborhoodRecommender nRecommender;
	private final CollaborativeRecommender cfRecommender;
	private final FileGenerator generator;

	private long recommendationTime;
	private int recommendationCount;
	private double precision;
	private double recall;
	private long trainingTime;
	private double rmse;
	private double mae;

	public DayTimeRecommender(NeighborhoodRecommender nRecommender, CollaborativeRecommender cfRecommender,
			FileGenerator generator) {
		this.nRecommender = nRecommender;
		this.cfRecommender = cfRecommender;
		this.generator = generator;
	}

	public void buildDataModel() {
		long ini = System.currentTimeMillis();
		System.out.println("Training DayTime Recommender");
		precisionRecall();
		rmseMae();
		trainingTime = System.currentTimeMillis() - ini;
	}

	private void rmseMae() {
		List<ReviewCF> reviews = cfRecommender.getAllReviews();
		double sumaR = 0D;
		double sumaM = 0D;
		int size = (3 * reviews.size()) / 100;
		for (int a = 0; a < size; a++) {
			ReviewCF r = reviews.get(a);
			double iniValue = nRecommender.estimatePreference(generator.getBusinessGeneratedId(r.getBusinessId()),
					generator.getUserGeneratedId(r.getUserId()));
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

	public List<Prediction> recommendItems(String userId, String neighborhood, int size, int day, int time) {
		long ini = System.currentTimeMillis();
		List<Prediction> result = new ArrayList<Prediction>();
		List<Prediction> recommendations = nRecommender.recommendItems(userId, neighborhood, size);
		for (Prediction p : recommendations) {
			String business = p.getKey();
			double value = getSimilarity(business, day, time);
			result.add(new Prediction(business, p.getValue() + value));
		}
		Collections.sort(result);
		recommendationCount++;
		recommendationTime += System.currentTimeMillis() - ini;
		size = result.size() < size ? result.size() : size;
		return result.subList(0, size);
	}

	private double getSimilarity(String businessId, int day, int time) {
		List<DayTime> dayTime = generator.getBusinessDayTime(businessId);
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
		List<String> randomUsers = getUsers();
		List<String> neighborhoods = getNeighborhoods();
		precision = 0.0;
		recall = 0.0;
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 10; j++) {
				int time = getTime();
				for (String n : neighborhoods) {
					List<String> goodBusiness = getAllGoodBusiness(n);
					for (String u : randomUsers) {
						int goodRecommendations = 0;
						List<Prediction> items = recommendItems(u, n, 10, i, time);
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
			}
		}

		precision = precision / ((double) randomUsers.size() * neighborhoods.size() * 40);
		recall = recall / ((double) randomUsers.size() * neighborhoods.size() * 40);
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
}
