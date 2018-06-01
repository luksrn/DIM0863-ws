package br.ufrn.dimap.dim0863.webserver.dominio;

import java.util.Date;

public class Localizacao {

	private Date data;
	private double latitude;
	private double longitude;
	
	public Localizacao(Date data, double latitude, double longitude) {
		this.data = data;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Date getData() {
		return data;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	
}
