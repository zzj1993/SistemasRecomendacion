package resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.UserBusiness;
import entity.User;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private final UserBusiness userBusiness;
	
	public UserResource(UserBusiness userBusiness){
		this.userBusiness = userBusiness;
	}
	
	@GET
	public Response getUsers(){
		List<User> users = userBusiness.getUsers();
		Response response = Response.status(200).entity(users).build();
		return response;
	}
}
