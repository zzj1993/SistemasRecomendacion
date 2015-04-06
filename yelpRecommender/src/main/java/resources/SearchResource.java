package resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.SearchBusiness;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResource {
	
	private SearchBusiness business;
	
	public SearchResource(SearchBusiness business){
		this.business = business;
	}

	@GET
	public Response search(@QueryParam("text") String text){
		business.search(text);
		Response response = Response.status(200).build();
		return response;
	}
}
