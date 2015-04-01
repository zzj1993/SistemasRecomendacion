package resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.NeighborhoodBusiness;

@Path("/neighborhood")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NeighborhoodResource {
	
	private final NeighborhoodBusiness neighborhoodBusiness;
	
	public NeighborhoodResource(NeighborhoodBusiness neighborhoodBusiness){
		this.neighborhoodBusiness = neighborhoodBusiness;
	}

	@GET
	public Response getNeighborhoods(){
		Response response = Response.status(200).entity(neighborhoodBusiness.getAllNeighborhoods()).build();
		return response;
	}
}