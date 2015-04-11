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

	private double mean;
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
		mean = 0.0D;
		recommendationTime = 0;
		recommendationCount = 0;
		train(size);
	}

	public void reTrain() {
		train(lastSize);
	}

	private void train(int size) {
		lastSize = size;
		trainingProgress = 0;
		datasetSize = (recommendersInformation.getReviewsSize() * size) / 100;
		System.out.println("CollaborativeRecommender: Training with " + size + "%");
		long ini = System.currentTimeMillis();
		List<ReviewCF> reviews = recommendersInformation.getReviewsSublist(datasetSize);
		mean = reviews.parallelStream().mapToInt(ReviewCF::getStars).average().getAsDouble();
		precisionRecall();
		trainingTime = System.currentTimeMillis() - ini;
		System.out.println("CollaborativeRecommender: End Training");
	}

	public List<Prediction> recommendItems(String userId) {
		long ini = System.currentTimeMillis();
		double userMean = recommendersInformation.getUserMean(userId);
		List<String> businessReviewed = recommendersInformation.getBusinessReviewed(userId);

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

	public double estimatePreference(String userId, String businessId) {
		double userMean = recommendersInformation.getUserMean(userId);
		double businessMean = recommendersInformation.getBusinessMean(businessId);
		return getPrediction(userMean, businessMean);
	}

	public double getMean() {
		return mean;
	}

	public int getDatasetSize() {
		return datasetSize;
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
		List<String> randomUsers = recommendersInformation.getRandomUsers();
		int goodBusiness = recommendersInformation.getAllGoodBusinessSize();
		precision = 0.0;
		recall = 0.0;
		int j = 0;
		for (String u : randomUsers) {
			List<Prediction> items = recommendItems(u);
			long goodRecommendations = items.parallelStream().filter(p -> Double.compare(p.getValue(), 4.0D) >= 0).count();
			precision += (double) goodRecommendations / (double) items.size();
			recall += (double) goodRecommendations / (double) goodBusiness;
			trainingProgress = j * 100 / randomUsers.size();
			System.out.println("Collaborative Recommender: Precision Recall: " + j + " de " + randomUsers.size());
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

	public int getTrainingProgress() {
		return trainingProgress;
	}
}