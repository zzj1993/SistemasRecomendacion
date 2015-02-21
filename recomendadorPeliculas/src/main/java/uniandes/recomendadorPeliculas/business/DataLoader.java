package uniandes.recomendadorPeliculas.business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uniandes.recomendadorPeliculas.config.DataConfig;
import uniandes.recomendadorPeliculas.entities.Genre;
import uniandes.recomendadorPeliculas.entities.Movie;
import uniandes.recomendadorPeliculas.entities.MoviesData;
import uniandes.recomendadorPeliculas.entities.Ratting;

public class DataLoader {

	private final DataConfig dataConfig;
	
	public DataLoader(DataConfig dataConfig){
		this.dataConfig = dataConfig;
	}
	
	public MoviesData getMoviesData(){
		try {
			List<Ratting> rattings = loadRattings();
			List<Movie> movies = loadMovies();
			return new MoviesData(rattings, movies);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Ratting> loadRattings() throws IOException{
		String dir = dataConfig.getDir();
		String file = dataConfig.getRattings();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();
		List<Ratting> rattings = new ArrayList<Ratting>();
		while(str != null){
			String[] split = str.split("::");
			Long user = Long.parseLong(split[0]);
			Long item = Long.parseLong(split[1]);
			Integer ratting = Integer.parseInt(split[2]);
			rattings.add(new Ratting(user, item, ratting));
			str = bf.readLine();
		}
		bf.close();
		return rattings;
	}
	
	private List<Movie> loadMovies() throws IOException{
		String dir = dataConfig.getDir();
		String file = dataConfig.getMovies();
		BufferedReader bf = new BufferedReader(new FileReader(dir+file));
		String str = bf.readLine();
		List<Movie> movies = new ArrayList<Movie>();
		while(str != null){
			String[] split = str.split("::");
			Long id = Long.parseLong(split[0]);
			String title = split[1];
			String strGenres = split[2];
			String[] splitGenres = strGenres.split("|");
			
			List<Genre> genres = new ArrayList<Genre>();
			for(int i = 0 ; i < splitGenres.length ; i++){
				genres.add(new Genre(splitGenres[i]));
			}
			
			movies.add(new Movie(id, title, genres));
			str = bf.readLine();
		}
		bf.close();
		return movies;
	}
	
}
