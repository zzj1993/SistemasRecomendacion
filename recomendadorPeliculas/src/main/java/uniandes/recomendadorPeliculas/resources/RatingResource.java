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

import org.apache.mahout.cf.taste.common.TasteException;
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
	public Response createMovieRating(Long userId, long itemId, int rating){
		Response response = null;
		Rating r = new Rating(userId, itemId, rating);
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
			@QueryParam("type") Integer type ,@QueryParam("model") Integer modelType,
			@QueryParam("size") Integer size, @QueryParam("n") Integer n)
			throws Exception {
		Response response = null;
		List<MovieRating> ratedMovies = movieBusiness.getAllUserRatedMovies(userid.longValue());
		List<MovieRating> movies = getRecommendedItems(userid, type, modelType, size, n+ratedMovies.size());
		List<MovieRating> resultMovies = new ArrayList<MovieRating>();
		int i = 0;
		int j = 0;
		while(i < n){
			if(!ratedMovies.contains(movies.get(j))){
				resultMovies.add(movies.get(j));
				i++;
			}
			j++;
		}

		response = Response.status(200).entity(resultMovies).build();
		return response;
	}
	
	private List<MovieRating> getRecommendedItems(Integer userid,
			Integer type ,Integer modelType,
			Integer size, Integer n) throws TasteException{
		List<RecommendedItem> recommendations = recommenders.getRecommendation(userid, modelType, size, n, type);
		List<MovieRating> movies = new ArrayList<MovieRating>();
		for (int i = 0; i < recommendations.size(); i++) {
			RecommendedItem r = recommendations.get(i);
			MovieRating m = movieBusiness.getMovieRating(r.getItemID());
			MovieRating m1 = new MovieRating(m.getId(), m.getTitle(),
					m.getGenres(), (int) r.getValue());
			movies.add(m1);
		}
		return movies;
	}
}
