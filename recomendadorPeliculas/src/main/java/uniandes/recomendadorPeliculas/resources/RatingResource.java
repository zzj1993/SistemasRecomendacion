package uniandes.recomendadorPeliculas.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uniandes.recomendadorPeliculas.business.MovieBusiness;
import uniandes.recomendadorPeliculas.entities.Rating;

@Path("/rating")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {
	
	private final MovieBusiness movieBusiness;
	
	public RatingResource(MovieBusiness movieBusiness) {
		this.movieBusiness = movieBusiness;
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
}
