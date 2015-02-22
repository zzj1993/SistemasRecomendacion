package uniandes.recomendadorPeliculas;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;
import java.util.Hashtable;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.apache.commons.dbcp2.BasicDataSource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import uniandes.recomendadorPeliculas.DAO.MovieDAO;
import uniandes.recomendadorPeliculas.DAO.RattingsDAO;
import uniandes.recomendadorPeliculas.DAO.UserDAO;
import uniandes.recomendadorPeliculas.business.LoginBusiness;
import uniandes.recomendadorPeliculas.business.MovieBusiness;
import uniandes.recomendadorPeliculas.business.SignupBusiness;
import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.config.H2Config;
import uniandes.recomendadorPeliculas.config.RecomendadorPeliculasConfig;
import uniandes.recomendadorPeliculas.resources.LoginResource;
import uniandes.recomendadorPeliculas.resources.MovieResource;
import uniandes.recomendadorPeliculas.resources.SignupResource;
import uniandes.recomendadorPeliculas.utils.SqlUtils;

public class RecomendadorPeliculas extends
		Application<RecomendadorPeliculasConfig> {

	@Override
	public void initialize(Bootstrap<RecomendadorPeliculasConfig> bootstrap) {

	}

	@Override
	public void run(RecomendadorPeliculasConfig recomendadorPeliculasConfig,
			Environment environment) throws Exception {

		BasicDataSource dataSource = getInitializedDataSource(recomendadorPeliculasConfig.getH2Config());
		
		if(recomendadorPeliculasConfig.getDataConfig().getLoad().equals("true")){
			createTablesIfNeeded(dataSource.getConnection(), recomendadorPeliculasConfig.getDataConfig());						
		}
		
		addCORSSupport(environment);
		
		Hashtable<String, Long> sessions = new Hashtable<String, Long>();

		final LoginResource loginResource = getLoginResource(dataSource, sessions);
		environment.jersey().register(loginResource);
		
		final SignupResource signupResource = getSignupResource(dataSource, sessions);
		environment.jersey().register(signupResource);
		
		final MovieResource movieResource = getMovieResource(dataSource);
		environment.jersey().register(movieResource);
	}

	private MovieResource getMovieResource(BasicDataSource dataSource) {
		MovieDAO movieDAO = new MovieDAO();
		MovieBusiness movieBusiness = new MovieBusiness(dataSource, movieDAO);
		final MovieResource movieResource = new MovieResource(movieBusiness);
		return movieResource;
	}

	private LoginResource getLoginResource(BasicDataSource dataSource, Hashtable<String, Long> sessions) {
		UserDAO userDAO = new UserDAO();
		LoginBusiness loginBusiness = new LoginBusiness(dataSource, userDAO, sessions);
		final LoginResource loginResource = new LoginResource(loginBusiness);
		return loginResource;
	}

	private SignupResource getSignupResource(BasicDataSource dataSource, Hashtable<String, Long> sessions) {
		UserDAO userDAO = new UserDAO();
		SignupBusiness signupBusiness = new SignupBusiness(dataSource, userDAO, sessions);
		final SignupResource signupResource = new SignupResource(signupBusiness);
		return signupResource;
	}
	
	private BasicDataSource getInitializedDataSource(H2Config h2Config) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(h2Config.getDriverClass());
		dataSource.setUrl(h2Config.getUrl());
		dataSource.setUsername(h2Config.getUser());
		dataSource.setPassword(h2Config.getPassword());
		return dataSource;
	}
	
	private void createTablesIfNeeded(java.sql.Connection dbConnection, DataConfig dataConfig) {
		UserDAO userDAO = new UserDAO();
		userDAO.createUsersTablesIfNeeded(dbConnection);
		
		MovieDAO movieDAO = new MovieDAO();
		movieDAO.createTablesIfNeeded(dbConnection, dataConfig);
		
		RattingsDAO rattingsDAO = new RattingsDAO();
		rattingsDAO.createTablesIfNeeded(dbConnection, dataConfig);
		SqlUtils.closeDbConnection(dbConnection);
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
	
	public static void main(String[] args) {
		try {//new String[] { "server" }
			new RecomendadorPeliculas().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
