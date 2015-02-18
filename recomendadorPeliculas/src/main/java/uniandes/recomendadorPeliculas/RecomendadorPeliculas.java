package uniandes.recomendadorPeliculas;

import uniandes.recomendadorPeliculas.config.RecomendadorPeliculasConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RecomendadorPeliculas extends Application<RecomendadorPeliculasConfig>{
	

	@Override
	public void initialize(Bootstrap<RecomendadorPeliculasConfig> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(RecomendadorPeliculasConfig arg0, Environment arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		try {
			new RecomendadorPeliculas().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
