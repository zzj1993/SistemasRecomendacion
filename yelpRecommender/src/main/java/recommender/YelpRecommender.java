package recommender;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import recommender.CollaborativeRecommender.CollaborativeRecommender;
import resources.ConfigureRecommendersResource;
import resources.EvaluationResource;
import business.ConfigureRecommendersBusiness;
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
		recommender.init(100);
		
		final EvaluationResource evaluationResource = getEvaluationResource(recommender);
		environment.jersey().register(evaluationResource);
		
		final ConfigureRecommendersResource configurationResource = getConfigurationResource(recommender);
		environment.jersey().register(configurationResource);
	}
	
	private ConfigureRecommendersResource getConfigurationResource(CollaborativeRecommender recommender) {
		ConfigureRecommendersBusiness business = new ConfigureRecommendersBusiness(recommender);
		ConfigureRecommendersResource resource= new ConfigureRecommendersResource(business);
		return resource;
	}

	private EvaluationResource getEvaluationResource(
			CollaborativeRecommender recommender) {
		EvaluationBusiness business = new EvaluationBusiness(recommender);
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
