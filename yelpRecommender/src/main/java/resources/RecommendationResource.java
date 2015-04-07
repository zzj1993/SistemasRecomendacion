package resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.RecommendationBusiness;
import business.Recommenders;
import entity.Recommendation;
import entity.RecommendationParameters;
import entity.ReviewCF;
import entity.ShortRecommendation;

@Path("/recommendation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationResource {

	private final RecommendationBusiness business;

	public RecommendationResource(RecommendationBusiness business) {
		this.business = business;
	}

	@GET
	public Response getRecommendations(@QueryParam("name") String name, @QueryParam("userId") String userId,
			@QueryParam("neighborhood") String neighborhood, @QueryParam("day") Integer day, @QueryParam("time") Integer time,
			@QueryParam("text") String text) {
		RecommendationParameters params = new RecommendationParameters(userId, neighborhood, day, time, text);
		List<Recommendation> recommendations = null;
		if (Recommenders.COLLABORATIVE_RECOMMENDER.equals(name)) {
			recommendations = getCollaborativeRecommendations(params);
		} else if (Recommenders.ITEM_RECOMMENDER.equals(name)) {
			recommendations = getItemRecommendations(params);
		} else if (Recommenders.NEIGHBORHOOD_RECOMMENDER.equals(name)) {
			recommendations = getNeighborhoodRecommendations(params);
		} else if (Recommenders.DAYTIME_RECOMMENDER.equals(name)) {
			recommendations = getDayTimeRecommendations(params);
		} else if (Recommenders.HYBRID_RECOMMENDER.equals(params)) {
			recommendations = getHybridRecommendations(params);
		}
		int size = recommendations.size() >= 10 ? 10 : recommendations.size();
		Response response = Response.status(200).entity(recommendations.subList(0, size)).build();
		return response;
	}

	private List<Recommendation> getHybridRecommendations(RecommendationParameters params) {
		List<Recommendation> recommendations = business.getHybridRecommendations(params.getUserId(), params.getNeighborhood(),
				10, params.getDay(), params.getTime());
		return recommendations;
	}

	private List<Recommendation> getDayTimeRecommendations(RecommendationParameters params) {
		List<Recommendation> recommendations = business.getDayTimeRecommendations(params.getUserId(), params.getDay(),
				params.getTime());
		return recommendations;
	}

	private List<Recommendation> getNeighborhoodRecommendations(RecommendationParameters params) {
		List<Recommendation> recommendations = business.getNeighborhoodRecommendations(params.getUserId(),
				params.getNeighborhood());
		return recommendations;
	}

	private List<Recommendation> getItemRecommendations(RecommendationParameters params) {
		List<Recommendation> recommendations = business.getItemRecommendations(params.getUserId());
		return recommendations;
	}

	private List<Recommendation> getCollaborativeRecommendations(RecommendationParameters params) {
		List<Recommendation> recommendations = business.getCollaborativeRecommendations(params.getUserId());
		return recommendations;
	}

	@GET
	@Path("/user/{userId}")
	public Response getUserReviews(@PathParam("userId") String userId) {
		List<ShortRecommendation> review = business.getUserRatings(userId);
		Response response = Response.status(200).entity(review).build();
		return response;
	}

	@POST
	@Path("/review")
	public Response deleteReview(ShortRecommendation review) {
		business.deleteReview(review.getUserId(), review.getBusinessId());
		Response response = Response.status(201).build();
		return response;
	}

	@POST
	@Path("/rating")
	public Response addRating(ReviewCF review) {
		business.addRating(review.getUserId(), review.getBusinessId(), review.getStars());
		Response response = Response.status(201).build();
		return response;
	}
}