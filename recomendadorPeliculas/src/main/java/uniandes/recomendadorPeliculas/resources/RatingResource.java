package uniandes.recomendadorPeliculas.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import uniandes.recomendadorPeliculas.business.MovieBusiness;
import uniandes.recomendadorPeliculas.business.Recommenders;
import uniandes.recomendadorPeliculas.entities.MovieRating;
import uniandes.recomendadorPeliculas.entities.Rating;

@Path("/rating")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {
	
	private final MovieBusiness movieBusiness;
	private final Recommenders recommenders;
	
	public RatingResource(MovieBusiness movieBusiness, Recommenders recommenders) {
		this.movieBusiness = movieBusiness;
		this.recommenders = recommenders;
	}
	
	@POST
	public Response createMovieRating(String username, long itemId, int rating){
		Response response = null;
		Rating r = new Rating(username, itemId, rating);
		boolean createdRating = movieBusiness.createNewRating(r);
		if(createdRating){
			response = Response.status(200).entity(createdRating).build();			
		}else{
			response = Response.status(500).entity(createdRating).build();
		}
		return response;
	}
	
	@GET
	public Response getAllUserMovies(@QueryParam("userid") Integer userid,
			@QueryParam("model") Integer modelType,
			@QueryParam("size") Integer size, @QueryParam("n") Integer n)
			throws Exception {
		Response response = null;
		List<RecommendedItem> recommendations = recommenders
				.getUserBasedRecomemndations(userid, modelType, size, n);
		

		List<MovieRating> movies = new ArrayList<MovieRating>();
		for (int i = 0; i < recommendations.size(); i++) {
			RecommendedItem r = recommendations.get(i);
			MovieRating m = movieBusiness.getMovieRating(r.getItemID());
			MovieRating m1 = new MovieRating(m.getId(), m.getTitle(),
					m.getGenres(), (int) r.getValue());
			movies.add(m1);
		}
		response = Response.status(200).entity(movies).build();
		return response;
	}
}
