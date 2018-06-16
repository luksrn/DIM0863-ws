package br.ufrn.dimap.dim0863.webserver.web.dto;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;

public class CarInfoRequest {

	private String licensePlate;
	private CarInfo carInfo;
	
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public CarInfo getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(CarInfo carInfo) {
		this.carInfo = carInfo;
	}
	
}
