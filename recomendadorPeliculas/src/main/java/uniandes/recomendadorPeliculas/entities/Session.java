package uniandes.recomendadorPeliculas.entities;

import java.util.Hashtable;

public class Session {
	private final Hashtable<String, Long> sessions;

	public Session() {
		sessions = new Hashtable<String, Long>();
	}
	
	public void put(String key, Long value){
		
	}

	public Hashtable<String, Long> getSessions() {
		return sessions;
	}
}
