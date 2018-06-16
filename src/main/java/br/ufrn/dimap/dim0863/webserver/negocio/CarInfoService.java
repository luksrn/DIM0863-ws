package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.Set;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;
import br.ufrn.dimap.dim0863.webserver.repositorio.CarInfoRepository;


@Component
public class CarInfoService {
	
	CarInfoRepository repository;
	
	public CarInfoService(CarInfoRepository repositorio) {
		this.repository = repositorio;
	}

	public CarInfo sendCarInfo(String licensePlate, CarInfo carInfo)  throws Exception  {
		repository.add(licensePlate, carInfo);
		return carInfo;
	}
	
	public Set<CarInfo> findCarInfo(String licensePlate)  throws Exception  {
		return repository.findAll(licensePlate);
	}
	
}
