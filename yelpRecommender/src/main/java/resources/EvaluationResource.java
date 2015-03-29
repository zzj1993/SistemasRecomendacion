package resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.EvaluationBusiness;
import entity.EvaluationStatistics;

@Path("/evaluation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {
	
	private final EvaluationBusiness business;

	public EvaluationResource(EvaluationBusiness business) {
		this.business = business;
	}
	
	@GET
	public Response getEvaluations(){
		List<EvaluationStatistics> statistics = business.getStatistics();
		Response response = Response.status(200).entity(statistics).build();
		return response;
	}
}
