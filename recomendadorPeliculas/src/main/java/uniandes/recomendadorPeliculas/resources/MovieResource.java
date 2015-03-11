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

import uniandes.recomendadorPeliculas.business.MovieBusiness;
import uniandes.recomendadorPeliculas.entities.MovieRating;
import uniandes.recomendadorPeliculas.entities.Rating;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

	private final MovieBusiness movieBusiness;

	public MovieResource(MovieBusiness movieBusiness) {
		this.movieBusiness = movieBusiness;
	}

	@GET
	public Response getAllMovies() throws Exception {
		Response response = null;
		List<MovieRating> movies = movieBusiness.getAllMovies();
		if (movies != null) {
			response = Response.status(200).entity(movies).build();
		} else {
			movies = new ArrayList<MovieRating>();
			response = Response.status(200).entity(movies).build();
		}
		return response;
	}

	@POST
	public Response createMovieRating(Rating rating) {
		Response response = null;
		boolean createdRating = movieBusiness.createNewRating(rating);
		if (createdRating) {
			response = Response.status(200).build();
		} else {
			response = Response.status(500).build();
		}
		return response;
	}
	
	@GET
	@Path("/rated")
	public Response getRatedMovies(@QueryParam("userid") Integer userid,
			@QueryParam("type") Integer type ,@QueryParam("model") Integer modelType,
			@QueryParam("size") Integer size, @QueryParam("n") Integer n) throws Exception {
		Response response = null;
		List<MovieRating> movies = movieBusiness.getAllRatedMoviesWithRating(
				userid.longValue(), modelType, size, n,
				type);
		if (movies != null) {
			response = Response.status(200).entity(movies).build();
		} else {
			movies = new ArrayList<MovieRating>();
			response = Response.status(200).entity(movies).build();
		}
		return response;
	}
}