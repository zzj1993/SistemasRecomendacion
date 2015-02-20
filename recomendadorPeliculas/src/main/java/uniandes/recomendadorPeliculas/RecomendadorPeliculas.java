package uniandes.recomendadorPeliculas;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

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
		addCORSSupport(environment);
		final LoginResource loginResource = getLoginResource();
		environment.jersey().register(loginResource);
	}

	private void addCORSSupport(Environment environment) {
		Dynamic filter = environment.servlets().addFilter("CORS",
				CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
				true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
				"GET,PUT,POST,DELETE,OPTIONS,PATCH");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(
				CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(
				"allowedHeaders",
				"Session-Id,Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
	}

	private LoginResource getLoginResource() {
		LoginBusiness loginBusiness = new LoginBusiness();
		final LoginResource loginResource = new LoginResource(loginBusiness);
		return loginResource;
	}

	public static void main(String[] args) {
		try {
			new RecomendadorPeliculas().run(new String[] { "server" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
