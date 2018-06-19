package br.ufrn.dimap.dim0863.webserver.web.dto;

import br.ufrn.dimap.dim0863.webserver.dominio.Location;

public class UserLocationRequest {

	private String login;
	private Location location;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
