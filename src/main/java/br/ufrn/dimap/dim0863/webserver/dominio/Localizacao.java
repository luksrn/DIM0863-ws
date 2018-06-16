package br.ufrn.dimap.dim0863.webserver.dominio;

import java.util.Date;

import br.ufrn.dimap.dim0863.webserver.util.DateUtil;

public class Localizacao {

	private Date data;
	private double latitude;
	private double longitude;

	public Localizacao() {}
	
	public Localizacao(String data, double latitude, double longitude) {
		this.data = DateUtil.convertFromString(data);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getData() {
		return DateUtil.convertToString(data);
	}

	public void setData(String data) {
		this.data = DateUtil.convertFromString(data);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return String.format("%s | Localizacao = (%f, %f)", DateUtil.convertToString(data), latitude, longitude);
	}

}
