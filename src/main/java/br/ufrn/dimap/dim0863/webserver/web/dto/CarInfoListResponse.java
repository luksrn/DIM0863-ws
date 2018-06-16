package br.ufrn.dimap.dim0863.webserver.web.dto;

import java.util.Set;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;


public class CarInfoListResponse {

	private String licensePlate;
	private Set<CarInfo> carInfoList;
	
	public CarInfoListResponse(String licensePlate, Set<CarInfo> carInfoList) {
		this.licensePlate = licensePlate;
		this.carInfoList = carInfoList;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public Set<CarInfo> getCarInfoList() {
		return carInfoList;
	}

}
