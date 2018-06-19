package br.ufrn.dimap.dim0863.webserver.web.dto;

import java.util.List;

import br.ufrn.dimap.dim0863.webserver.dominio.Location;


public class UserLocationListResponse {

	private String login;
	private List<Location> locations;

	public UserLocationListResponse(String login, List<Location> locations) {
		super();
		this.login = login;
		this.locations = locations;
	}

	public String getLogin() {
		return login;
	}

	public List<Location> getLocations() {
		return locations;
	}

}
