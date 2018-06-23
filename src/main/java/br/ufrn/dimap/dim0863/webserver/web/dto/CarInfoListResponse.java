package br.ufrn.dimap.dim0863.webserver.web.dto;

import java.util.List;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;


public class CarInfoListResponse {

	private String licensePlate;
	private List<CarInfo> carInfoList;

	public CarInfoListResponse(String licensePlate, List<CarInfo> carInfoList) {
		this.licensePlate = licensePlate;
		this.carInfoList = carInfoList;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public List<CarInfo> getCarInfoList() {
		return carInfoList;
	}

}
