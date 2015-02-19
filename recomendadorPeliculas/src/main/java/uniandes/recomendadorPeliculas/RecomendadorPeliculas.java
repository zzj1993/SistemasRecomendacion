package uniandes.recomendadorPeliculas;

import uniandes.recomendadorPeliculas.business.LoginBusiness;
import uniandes.recomendadorPeliculas.config.RecomendadorPeliculasConfig;
import uniandes.recomendadorPeliculas.resources.LoginResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RecomendadorPeliculas extends
		Application<RecomendadorPeliculasConfig> {

	@Override
	public void initialize(Bootstrap<RecomendadorPeliculasConfig> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(RecomendadorPeliculasConfig recomendadorPeliculasConfig,
			Environment environment) throws Exception {
		final LoginResource loginResource = getLoginResource();
		environment.jersey().register(loginResource);
	}
	
	private LoginResource getLoginResource(){
		LoginBusiness loginBusiness = new LoginBusiness();
		final LoginResource loginResource = new LoginResource(loginBusiness);
		return loginResource;
	}

	public static void main(String[] args) {
		try {
			new RecomendadorPeliculas().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
