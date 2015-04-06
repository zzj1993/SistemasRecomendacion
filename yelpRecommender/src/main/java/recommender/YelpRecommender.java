package recommender;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.ItemRecommender;
import recommender.dayTimeRecommender.DayTimeRecommender;
import recommender.neighborhoodRecommender.NeighborhoodRecommender;
import recommender.utils.FileGenerator;
import recommender.utils.RecommendersInformation;
import resources.ConfigureRecommendersResource;
import resources.EvaluationResource;
import resources.NeighborhoodResource;
import resources.RecommendationResource;
import resources.SearchResource;
import resources.UserResource;
import business.ConfigureRecommendersBusiness;
import business.Correlations;
import business.EvaluationBusiness;
import business.NeighborhoodBusiness;
import business.RecommendationBusiness;
import business.SearchBusiness;
import business.UserBusiness;
import configuration.YelpConfiguration;

public class YelpRecommender extends Application<YelpConfiguration> {

	@Override
	public void initialize(Bootstrap<YelpConfiguration> bootstrap) {
	}

	@Override
	public void run(YelpConfiguration configuration, Environment environment) throws Exception {
		addCORSSupport(environment);

		RecommendersInformation recommendersInformation = new RecommendersInformation(configuration.getDataConfiguration()
				.getDir());
		recommendersInformation.init(configuration.getDataConfiguration().getCollaborativeFile());

		FileGenerator fileGenerator = new FileGenerator(recommendersInformation);
		fileGenerator.generateFiles(configuration.getDataConfiguration().getFileGeneratorInDir(), configuration
				.getDataConfiguration().getFileGeneratorOutDir());

		double randomUsers = Double.parseDouble(configuration.getRecommendersConfiguration().getRandomUsers());
		double neighborhoodSize = Double.parseDouble(configuration.getRecommendersConfiguration().getNeighborhoodSize());
		double rmseMaeSize = Double.parseDouble(configuration.getRecommendersConfiguration().getRmseMaeSize());

		CollaborativeRecommender recommender = new CollaborativeRecommender(recommendersInformation, randomUsers);
		recommender.init(Integer.parseInt(configuration.getRecommendersConfiguration().getCfInitialSize()));

		ItemRecommender itemRecommender = new ItemRecommender(recommendersInformation, randomUsers);
		itemRecommender.buildDataModel(Integer.parseInt(configuration.getRecommendersConfiguration().getItemInitialSize()),
				Correlations.PEARSON_DISTANCE);

//		UserRecommender userRecommender = new UserRecommender(recommendersInformation, randomUsers);
//		userRecommender.buildDataModel(Integer.parseInt(configuration.getRecommendersConfiguration().getItemInitialSize()),
//				Correlations.PEARSON_DISTANCE,
//				Integer.parseInt(configuration.getRecommendersConfiguration().getUserNeighborhoodSize()));

		NeighborhoodRecommender nRecommender = new NeighborhoodRecommender(recommendersInformation, itemRecommender, randomUsers,
				neighborhoodSize);
		nRecommender.buildDataModel();

		DayTimeRecommender dayTimeRecommender = new DayTimeRecommender(recommendersInformation, nRecommender, randomUsers,
				neighborhoodSize, rmseMaeSize);
		dayTimeRecommender.buildDataModel();

		final EvaluationResource evaluationResource = getEvaluationResource(recommender, itemRecommender, nRecommender,
				dayTimeRecommender);
		environment.jersey().register(evaluationResource);

		final ConfigureRecommendersResource configurationResource = getConfigurationResource(recommender, itemRecommender,
				nRecommender, dayTimeRecommender);
		environment.jersey().register(configurationResource);

		final NeighborhoodResource neighborhoodResource = getNeighborhoodResource(recommendersInformation);
		environment.jersey().register(neighborhoodResource);

		final UserResource userResource = getUserResource(recommendersInformation);
		environment.jersey().register(userResource);

		final RecommendationResource recommendationResource = getRecommendationResource(recommendersInformation, recommender,
				itemRecommender, nRecommender, dayTimeRecommender);
		environment.jersey().register(recommendationResource);
		
		final SearchResource searchResource = getSearchResource(recommendersInformation);
		environment.jersey().register(searchResource);
	}

	private SearchResource getSearchResource(RecommendersInformation recommendersInformation) {
		SearchBusiness business = new SearchBusiness(recommendersInformation);
		SearchResource resource = new SearchResource(business);
		return resource;
	}

	private RecommendationResource getRecommendationResource(RecommendersInformation recommendersInformation,
			CollaborativeRecommender collaborativeRecommender, ItemRecommender itemRecommender,
			NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender) {
		RecommendationBusiness business = new RecommendationBusiness(recommendersInformation, collaborativeRecommender,
				nRecommender, dayTimeRecommender, itemRecommender);
		RecommendationResource resource = new RecommendationResource(business);
		return resource;
	}

	private UserResource getUserResource(RecommendersInformation recommendersInformation) {
		UserBusiness userBusiness = new UserBusiness(recommendersInformation);
		UserResource userResource = new UserResource(userBusiness);
		return userResource;
	}

	private NeighborhoodResource getNeighborhoodResource(RecommendersInformation recommendersInformation) {
		NeighborhoodBusiness business = new NeighborhoodBusiness(recommendersInformation);
		NeighborhoodResource neighborhoodResource = new NeighborhoodResource(business);
		return neighborhoodResource;
	}

	private ConfigureRecommendersResource getConfigurationResource(CollaborativeRecommender recommender,
			ItemRecommender itemRecommender, NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender) {
		ConfigureRecommendersBusiness business = new ConfigureRecommendersBusiness(recommender, itemRecommender, nRecommender,
				dayTimeRecommender);
		ConfigureRecommendersResource resource = new ConfigureRecommendersResource(business);
		return resource;
	}

	private EvaluationResource getEvaluationResource(CollaborativeRecommender recommender, ItemRecommender itemRecommender,
			NeighborhoodRecommender nRecommender, DayTimeRecommender dayTimeRecommender) {
		EvaluationBusiness business = new EvaluationBusiness(recommender, itemRecommender, nRecommender, dayTimeRecommender);
		EvaluationResource resource = new EvaluationResource(business);
		return resource;
	}

	private void addCORSSupport(Environment environment) {
		Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,PATCH");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter("allowedHeaders",
				"Session-Id,Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
	}

	public static void main(String[] args) throws Exception {
		YelpRecommender recommender = new YelpRecommender();
		recommender.run(args);
	}
}
