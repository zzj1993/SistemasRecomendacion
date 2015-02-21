package uniandes.recomendadorPeliculas.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uniandes.recomendadorPeliculas.business.LoginBusiness;
import uniandes.recomendadorPeliculas.entities.Login;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

	private final AtomicLong counter;
	private final LoginBusiness loginBusiness;
	
	public LoginResource(LoginBusiness loginBusiness) {
        this.loginBusiness = loginBusiness;
        this.counter = new AtomicLong();
    }

	@POST
	public Response login(Login login) throws Exception {
		Response response = null;
		if(loginBusiness.esUsuarioValido(login)){
			String jsonResponse = "{\"id\" : \"" + counter.incrementAndGet() + "\"}";
			response = Response.status(200).entity(jsonResponse).build();
		}else{
			String errorMessage = "Unathorized";
            response = Response.status(401).entity(errorMessage).build();
		}
		return response;
	}
}