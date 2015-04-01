package recommender.CollaborativeRecommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import recommender.utils.RecommendersInformation;
import entity.Prediction;
import entity.ReviewCF;

/**
 * Filtro colaborativo basado en reviews de yelp El calculo de la prediccion se
 * realiza r = u + Bu + Bi Donde u es el promedio general, Bu es el promedio
 * para el usuario Bi es el promedio para el negocio. La idea es mantener todo
 * en memoria para faciliar el recalculo de los promedios y la generacion de las
 * predicciones.
 * 
 * TODO Se propone agregar dos funciones adicionales para probar: r = u + Bu +
 * Bi + f(x)
 * 
 */
public class CollaborativeRecommender {

	private double sum;
	private double count;
	private final RecommendersInformation recommendersInformation;

	private int datasetSize;
	private long trainingTime;
	private long recommendationTime;
	private int recommendationCount;
	private int lastSize;
	private double precision;
	private double recall;
	private int trainingProgress;

	public CollaborativeRecommender(RecommendersInformation recommendersInformation) {
		this.recommendersInformation = recommendersInformation;
	}

	public void init(int size) {
		sum = 0.0D;
		count = 0.0D;
		recommendationTime = 0;
		recommendationCount = 0;
		train(size);
	}

	private void train(int size) {
		lastSize = size;
		trainingProgress = 0;
		datasetSize = (recommendersInformation.getReviewsSize() * size) / 100;
		System.out.println("CollaborativeRecommender: Training with " + size + "%");
		long ini = System.currentTimeMillis();
		List<ReviewCF> reviews = recommendersInformation.getReviewsSublist(datasetSize);
		for (ReviewCF r : reviews) {
			addGlobalMean(r.getStars());
		}
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
		System.out.println("CollaborativeRecommender: End Training");
	}

	public List<Prediction> recommendItems(String userId) {
		long ini = System.currentTimeMillis();
		double userMean = recommendersInformation.getUserMean(userId);
		List<String> businessReviewed = getBusinessReviewed(userId);

		// Recorrer los business y calcular la prediccion
		Iterator<String> it = recommendersInformation.getAllBusinessKeys().iterator();
		List<Prediction> predictions = new ArrayList<Prediction>();
		while (it.hasNext()) {
			String businessId = it.next();
			if (!businessReviewed.contains(businessId)) {
				double businessMean = recommendersInformation.getBusinessMean(businessId);
				double prediction = getPrediction(userMean, businessMean);
				predictions.add(new Prediction(businessId, prediction));
			}
		}
		Collections.sort(predictions);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		return predictions;
	}

	private double getPrediction(double userMean, double businessMean) {
		return getMean() + (userMean - getMean()) + (businessMean - getMean());
	}

	private List<String> getBusinessReviewed(String userId) {
		List<String> businessReviewed = new ArrayList<String>();
		if (recommendersInformation.userReviewsContainsUser(userId)) {
			List<ReviewCF> userReviews = recommendersInformation.getUserReviews(userId);
			for (int i = 0; i < userReviews.size(); i++) {
				ReviewCF review = userReviews.get(i);
				String businessId = review.getBusinessId();
				if (!businessReviewed.contains(businessId)) {
					businessReviewed.add(businessId);
				}
			}
		}
		return businessReviewed;
	}

	public double getMean() {
		return sum / (double) count;
	}

	public int getDatasetSize() {
		return datasetSize;
	}

	public void addRating(String userId, String businessId, int stars) {
		double computedStars = getPrediction(recommendersInformation.getUserMean(userId), recommendersInformation.getBusinessMean(businessId));
		ReviewCF review = new ReviewCF(businessId, userId, stars, computedStars);
		recommendersInformation.addReview(review);;
		// Agregar a las listas
		recommendersInformation.addUserReview(review);
		recommendersInformation.addBusinessReview(review);
		// Recalcular los promedios
		addGlobalMean(stars);
		recommendersInformation.addUserMean(userId, stars);
		recommendersInformation.addBusinessMean(businessId, stars);
	}

	private void addGlobalMean(double stars) {
		sum += stars;
		count++;
	}

	public double getRMSE() {
		double suma = 0D;
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		for (ReviewCF r : reviews) {
			suma += ((r.getComputedStars() - (double) r.getStars()) * (r.getComputedStars() - (double) r.getStars()));
		}
		return Math.sqrt(suma / (double) reviews.size());
	}

	public double getMAE() {
		double suma = 0D;
		List<ReviewCF> reviews = recommendersInformation.getReviews();
		for (ReviewCF r : reviews) {
			suma += Math.abs((r.getComputedStars() - (double) r.getStars()));
		}
		return Math.sqrt(suma / (double) reviews.size());
	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	private void precisionRecall() {
		List<String> randomUsers = recommendersInformation.getRandomUsers(0.3);
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
//		int topN = 10;
		int j = 0;
		for (String u : randomUsers) {
			int goodRecommendations = 0;
			List<Prediction> items = recommendItems(u);
			// && i < topN
			for (int i = 0 ; i < items.size() ; i++) {
			    Prediction p = items.get(i);
				if (Double.compare(p.getValue(), 4.0D) >= 0) {
					goodRecommendations++;
				}
			}
			precision += (double) goodRecommendations / (double) items.size();				
			recall += (double) goodRecommendations / (double) goodBusiness;
			trainingProgress = j * 100 / randomUsers.size();
			System.out.println("Collaborative Recommender: Precision Recall: "+j+" de "+randomUsers.size());
			j++;
		}

		precision = precision / (double) randomUsers.size();
		recall = recall / (double) randomUsers.size();
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

	public int getLastSize() {
		return lastSize;
	}
	
	public int getTrainingProgress(){
		return trainingProgress;
	}
}