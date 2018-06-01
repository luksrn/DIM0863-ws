package br.ufrn.dimap.dim0863.webserver.dominio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Localizacao {

	private Date data;
	private double latitude;
	private double longitude;

	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Localizacao() {}
	
	public Localizacao(Date data, double latitude, double longitude) {
		this.data = data;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Localizacao(String data, double latitude, double longitude) {
		try {
			this.data = DATE_FORMATTER.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Date getData() {
		return data;
	}

	private String getFormattedData() {
		return DATE_FORMATTER.format(this.data);
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setData(String data) {
		try {
			this.data = DATE_FORMATTER.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return String.format("Hora = %s | Localizacao = (%f, %f)", getFormattedData(), latitude, longitude);
	}

}
