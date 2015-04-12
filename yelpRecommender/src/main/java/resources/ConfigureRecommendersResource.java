package resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.ConfigureRecommendersBusiness;
import business.Recommenders;
import entity.Training;

@Path("/configuration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigureRecommendersResource {

	private final ConfigureRecommendersBusiness business;

	public ConfigureRecommendersResource(ConfigureRecommendersBusiness business) {
		this.business = business;
	}

	@POST
	@Path("/{name}")
	public Response trainRecommender(@PathParam("name") String name, Training training) {
		Response response = Response.status(201).build();
		if (name.equals(Recommenders.COLLABORATIVE_RECOMMENDER)) {
			business.trainCollaborativeRecommender(training.getSize());
		} else if (name.equals(Recommenders.ITEM_RECOMMENDER)) {
			business.trainItemRecommender(training.getSize(), training.getCorrelation());
		} else if (name.equals(Recommenders.NEIGHBORHOOD_RECOMMENDER)) {
			business.trainNeighborhoodRecommender(training.getCorrelation());
		} else if(name.equals(Recommenders.DAYTIME_RECOMMENDER)){
			business.trainDayTimeRecommender(training.getCorrelation());
		} else if (name.equals(Recommenders.HYBRID_RECOMMENDER)){
			business.trainHybridRecommender(training.getCorrelation());
		}
		return response;
	}
	
	@GET
	@Path("/{name}")
	public Response getTrainingProgress(@PathParam("name") String name){
		Response response = Response.status(200).entity(business.getTrainingProgress(name)).build();
		return response;
	}
}