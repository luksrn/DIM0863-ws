package br.ufrn.dimap.dim0863.webserver.dominio;

import java.util.Date;

import br.ufrn.dimap.dim0863.webserver.util.DateUtil;

public class Location {

	private Date date;
	private double lat;
	private double lon;

	public Location() {}

	public Location(String date, double lat, double lon) {
		this.date = DateUtil.convertFromString(date);
		this.lat = lat;
		this.lon = lon;
	}

	public String getDate() {
		return DateUtil.convertToString(date);
	}

	public void setDate(String date) {
		this.date = DateUtil.convertFromString(date);
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return String.format("%s | Localizacao = (%f, %f)", DateUtil.convertToString(date), lat, lon);
	}

}
