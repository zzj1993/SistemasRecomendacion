package recommender;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import recommender.CollaborativeRecommender.FileGenerator;
import recommender.CollaborativeRecommender.ItemRecommender;
import resources.ConfigureRecommendersResource;
import resources.EvaluationResource;
import business.ConfigureRecommendersBusiness;
import business.Correlations;
import business.EvaluationBusiness;
import configuration.YelpConfiguration;

public class YelpRecommender extends Application<YelpConfiguration>{

	@Override
	public void initialize(Bootstrap<YelpConfiguration> bootstrap) {
	}

	@Override
	public void run(YelpConfiguration configuration, Environment environment)
			throws Exception {
		addCORSSupport(environment);
		
		CollaborativeRecommender recommender = new CollaborativeRecommender(configuration.getDataConfiguration().getCollaborativeFile());
		recommender.init(Integer.parseInt(configuration.getRecommendersConfiguration().getCfInitialSize()));
		
		FileGenerator fileGenerator = new FileGenerator();
		fileGenerator.generateFiles(configuration.getDataConfiguration()
				.getFileGeneratorInDir(), configuration.getDataConfiguration()
				.getFileGeneratorOutDir());
		
		ItemRecommender itemRecommender = new ItemRecommender(configuration.getDataConfiguration().getDir(), fileGenerator);
		itemRecommender.buildDataModel(Integer.parseInt(configuration.getRecommendersConfiguration().getItemInitialSize()), Correlations.PEARSON_DISTANCE);
		
		final EvaluationResource evaluationResource = getEvaluationResource(recommender, itemRecommender);
		environment.jersey().register(evaluationResource);
		
		final ConfigureRecommendersResource configurationResource = getConfigurationResource(recommender, itemRecommender);
		environment.jersey().register(configurationResource);
	}
	
	private ConfigureRecommendersResource getConfigurationResource(CollaborativeRecommender recommender, ItemRecommender itemRecommender) {
		ConfigureRecommendersBusiness business = new ConfigureRecommendersBusiness(recommender, itemRecommender);
		ConfigureRecommendersResource resource= new ConfigureRecommendersResource(business);
		return resource;
	}

	private EvaluationResource getEvaluationResource(
			CollaborativeRecommender recommender, ItemRecommender itemRecommender) {
		EvaluationBusiness business = new EvaluationBusiness(recommender, itemRecommender);
		EvaluationResource resource = new EvaluationResource(business);
		return resource;
	}

	private void addCORSSupport(Environment environment) {
		Dynamic filter = environment.servlets().addFilter("CORS",
				CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
				true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
				"GET,PUT,POST,DELETE,OPTIONS,PATCH");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(
				CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(
				"allowedHeaders",
				"Session-Id,Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
	}

	public static void main(String[] args) throws Exception {
		YelpRecommender recommender = new YelpRecommender();
		recommender.run(args);
	}
}
