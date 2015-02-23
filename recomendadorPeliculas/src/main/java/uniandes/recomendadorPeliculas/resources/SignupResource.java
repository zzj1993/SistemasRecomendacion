package uniandes.recomendadorPeliculas.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uniandes.recomendadorPeliculas.business.SignupBusiness;
import uniandes.recomendadorPeliculas.entities.Signup;

@Path("/signup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SignupResource {

	private final SignupBusiness signupBusiness;
	private final AtomicLong counter;

	public SignupResource(SignupBusiness signupBusiness) {
		this.signupBusiness = signupBusiness;
		this.counter = new AtomicLong();
	}

	@POST
	public Response signup(Signup signup) throws Exception {
		Response response = null;
		boolean userWasCreated = signupBusiness.signup(signup);
		String jsonResponse = "{\"id\" : \"" + counter.incrementAndGet()
				+ "\"}";
		response = Response.status(201).entity(jsonResponse).build();
		return response;
	}
}
