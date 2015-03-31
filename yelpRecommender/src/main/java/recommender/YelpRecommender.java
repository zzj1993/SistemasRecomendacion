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
import business.ConfigureRecommendersBusiness;
import business.Correlations;
import business.EvaluationBusiness;
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

		CollaborativeRecommender recommender = new CollaborativeRecommender(recommendersInformation);
		recommender.init(Integer.parseInt(configuration.getRecommendersConfiguration().getCfInitialSize()));


		ItemRecommender itemRecommender = new ItemRecommender(recommendersInformation);
		itemRecommender.buildDataModel(Integer.parseInt(configuration.getRecommendersConfiguration().getItemInitialSize()),
				Correlations.PEARSON_DISTANCE);

		NeighborhoodRecommender nRecommender = new NeighborhoodRecommender(recommendersInformation, itemRecommender);
		nRecommender.buildDataModel();

		DayTimeRecommender dayTimeRecommender = new DayTimeRecommender(recommendersInformation, nRecommender);
		dayTimeRecommender.buildDataModel();

		final EvaluationResource evaluationResource = getEvaluationResource(recommender, itemRecommender, nRecommender,
				dayTimeRecommender);
		environment.jersey().register(evaluationResource);

		final ConfigureRecommendersResource configurationResource = getConfigurationResource(recommender, itemRecommender,
				nRecommender, dayTimeRecommender);
		environment.jersey().register(configurationResource);
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
