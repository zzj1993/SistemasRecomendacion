package uniandes.recomendadorPeliculas.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uniandes.recomendadorPeliculas.business.SignupBusiness;
import uniandes.recomendadorPeliculas.entities.User;

@Path("/signup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SignupResource {

	private final SignupBusiness signupBusiness;

	public SignupResource(SignupBusiness signupBusiness) {
		this.signupBusiness = signupBusiness;
	}

	@POST
	public Response signup(User user) throws Exception {
		Response response = null;
		boolean userWasCreated = signupBusiness.signup(user);
		String jsonResponse = "{\"id\" : \"" + user.getUserId()
				+ "\"}";
		response = Response.status(201).entity(jsonResponse).build();
		return response;
	}
}