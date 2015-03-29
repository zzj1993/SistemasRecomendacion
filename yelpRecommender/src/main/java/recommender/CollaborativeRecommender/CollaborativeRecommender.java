package recommender.CollaborativeRecommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import entity.MeanCF;
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

	private String file;
	private Hashtable<String, List<ReviewCF>> business;
	private Hashtable<String, List<ReviewCF>> user;
	private Hashtable<String, MeanCF> businessMeans;
	private Hashtable<String, MeanCF> userMeans;
	private List<ReviewCF> reviews;
	private double suma;
	private double count;

	private long datasetSize;
	private long trainingTime;
	private long recommendationTime;
	private int recommendationCount;
	private int lastSize;

	public CollaborativeRecommender(String file) {
		this.file = file;
		initDatasetSize();
	}

	public void init(int size) {
		business = new Hashtable<String, List<ReviewCF>>();
		businessMeans = new Hashtable<String, MeanCF>();
		user = new Hashtable<String, List<ReviewCF>>();
		userMeans = new Hashtable<String, MeanCF>();
		reviews = new ArrayList<ReviewCF>();
		suma = 0.0D;
		count = 0.0D;
		recommendationTime = 0;
		recommendationCount = 0;
		train(size);
	}

	private void train(int size) {
		try {
			lastSize = size;
			long i = 1;
			long tamano = (datasetSize * size) / 100;
			System.out.println("CollaborativeRecommender: Training with "+size+"%");
			long ini = System.currentTimeMillis();
			BufferedReader bf = new BufferedReader(new FileReader(
					new File(file)));
			String str = bf.readLine();// Encabezado
			str = bf.readLine();
			while (str != null && i <= tamano) {
				String[] linea = str.split(";");
				String userId = linea[1].replace("\"", "");
				String businessId = linea[2].replace("\"", "");
				int stars = Integer.parseInt(linea[3]);
				double businessStars = Double.parseDouble(linea[4]);
				double userStars = Double.parseDouble(linea[5]);
				double computedStars = Double.parseDouble(linea[6]);
				ReviewCF review = new ReviewCF(businessId, userId, stars,
						computedStars);
				reviews.add(review);
				addBusinessReview(review);
				createBusinessMean(businessId, businessStars);
				addUserReview(review);
				createUserMean(userId, userStars);
				addGlobalMean(stars);
				str = bf.readLine();
				i++;
			}
			bf.close();
			trainingTime = System.currentTimeMillis() - ini;
			System.out.println("CollaborativeRecommender: End Training");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDatasetSize() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(
					new File(file)));
			String str = bf.readLine();// Encabezado
			str = bf.readLine();
			datasetSize = 1;
			while (str != null) {
				datasetSize++;
				str = bf.readLine();
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createUserMean(String userId, double userStars) {
		if (!userMeans.containsKey(userId)) {
			MeanCF mean = new MeanCF(userStars, 1);
			userMeans.put(userId, mean);
		}
	}

	private void addUserMean(String userId, double userStars) {
		if (userMeans.containsKey(userId)) {
			MeanCF oldMean = userMeans.get(userId);
			MeanCF newMean = new MeanCF(oldMean.getSuma() + userStars,
					oldMean.getCount() + 1);
			userMeans.put(userId, newMean);
		} else {
			createUserMean(userId, userStars);
		}
	}

	private void createBusinessMean(String businessId, double businessStars) {
		if (!businessMeans.containsKey(businessId)) {
			MeanCF mean = new MeanCF(businessStars, 1);
			businessMeans.put(businessId, mean);
		}
	}

	private void addBusinessMean(String businessId, double businessStars) {
		if (businessMeans.containsKey(businessId)) {
			MeanCF oldMean = businessMeans.get(businessId);
			MeanCF newMean = new MeanCF(oldMean.getSuma() + businessStars,
					oldMean.getCount() + 1);
			businessMeans.put(businessId, newMean);
		} else {
			createBusinessMean(businessId, businessStars);
		}
	}

	private void addUserReview(ReviewCF review) {
		List<ReviewCF> lista;
		if (user.containsKey(review.getUserId())) {
			lista = user.get(review.getUserId());
		} else {
			lista = new ArrayList<ReviewCF>();
		}
		lista.add(review);
		user.put(review.getUserId(), lista);
	}

	private void addBusinessReview(ReviewCF review) {
		List<ReviewCF> lista;
		if (business.containsKey(review.getBusinessId())) {
			lista = business.get(review.getBusinessId());
		} else {
			lista = new ArrayList<ReviewCF>();
		}
		lista.add(review);
		business.put(review.getBusinessId(), lista);
	}

	/**
	 * Genera un recomendacion de tamaño size para un usuario dado
	 * 
	 * @param userId
	 * @param size
	 * @return La lista con la lista de ids de los negocios
	 */
	public List<Prediction> recommendItems(String userId, int size) {
		long ini = System.currentTimeMillis();
		double userMean = userMeans.get(userId).getMean();
		List<String> businessReviewed = getBusinessReviewed(userId);

		// Recorrer los business y calcular la prediccion
		Iterator<String> it = business.keySet().iterator();
		List<Prediction> predictions = new ArrayList<Prediction>();
		int i = 0;
		while (it.hasNext() && i < size) {
			String businessId = it.next();
			if (!businessReviewed.contains(businessId)) {
				double businessMean = businessMeans.get(businessId).getMean();
				double prediction = getPrediction(userMean, businessMean);
				predictions.add(new Prediction(businessId, prediction));
				i++;
			}
		}
		Collections.sort(predictions);
		recommendationTime += System.currentTimeMillis() - ini;
		recommendationCount++;
		return predictions.subList(0, size);
	}

	private double getPrediction(double userMean, double businessMean) {
		return getMean() + (userMean - getMean()) + (businessMean - getMean());
	}

	private List<String> getBusinessReviewed(String userId) {
		List<String> businessReviewed = new ArrayList<String>();
		if (user.containsKey(userId)) {
			List<ReviewCF> userReviews = user.get(userId);
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
		return suma / (double) count;
	}

	public int getDatasetSize() {
		return reviews.size();
	}

	public void addRating(String userId, String businessId, int stars) {
		double computedStars = getPrediction(userMeans.get(userId).getMean(),
				businessMeans.get(businessId).getMean());
		ReviewCF review = new ReviewCF(businessId, userId, stars, computedStars);
		reviews.add(review);
		// Agregar a las listas
		addUserReview(review);
		addBusinessReview(review);
		// Recalcular los promedios
		addGlobalMean(stars);
		addUserMean(userId, stars);
		addBusinessMean(businessId, stars);
	}

	private void addGlobalMean(double stars) {
		suma += stars;
		count++;
	}

	public double getRMSE() {
		double suma = 0D;
		for (ReviewCF r : reviews) {
			suma += ((r.getComputedStars() - (double) r.getStars()) * (r
					.getComputedStars() - (double) r.getStars()));
		}
		return Math.sqrt(suma / (double) reviews.size());
	}

	public double getMAE() {
		double suma = 0D;
		for (ReviewCF r : reviews) {
			suma += Math.abs((r.getComputedStars() - (double) r.getStars()));
		}
		return Math.sqrt(suma / (double) reviews.size());
	}

	public double getPrecision() {
		return 0;
	}

	public double getRecall() {
		return 0;
	}

	public double getTrainingTime() {
		return (double) trainingTime / 1000.0;
	}

	public double getRecommendationTime() {
		if (recommendationCount == 0)
			return 0;
		return recommendationTime / (double) recommendationCount;
	}
	
	public int getLastSize(){
		return lastSize;
	}

	public static void main(String[] args) throws IOException {
		// CollaborativeRecommender r = new CollaborativeRecommender();
		// long ini = System.currentTimeMillis();
		// r.init();
		// System.out.println("Time: "
		// + ((System.currentTimeMillis() - ini) / 1000));
		// System.out.println("Mean: " + r.getMean());
		// System.out.println("RMSE: "+r.getRMSE());
		// System.out.println("MAE: "+r.getMAE());
	}
}