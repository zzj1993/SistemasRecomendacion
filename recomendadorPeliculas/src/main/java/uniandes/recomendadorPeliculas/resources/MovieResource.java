package uniandes.recomendadorPeliculas.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uniandes.recomendadorPeliculas.business.MovieBusiness;
import uniandes.recomendadorPeliculas.entities.MovieRating;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {
	
	private final MovieBusiness movieBusiness;

	public MovieResource(MovieBusiness movieBusiness) {
		this.movieBusiness = movieBusiness;
	}
	
	@GET
	public Response query() throws Exception {
		Response response = null;
		List<MovieRating> movies = movieBusiness.getAllMovies();
		if (movies!=null) {
			response = Response.status(200).entity(movies).build();
		} else {
			movies = new ArrayList<MovieRating>();
			response = Response.status(200).entity(movies).build();
		}
		return response;
	}
}