package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;

@Component
public class CarInfoRepository {

	private static Map<String, Set<CarInfo>> carInfoList;
	
	private CarInfoRepository() {
		carInfoList = new HashMap<>();
		carInfoList.put("ABC-1234", new HashSet<>());
		carInfoList.put("DEF-1234", new HashSet<>());
		carInfoList.put("GH1-1234", new HashSet<>());
	}
		
	public void add(String licensePlate, CarInfo carInfo) {
		carInfoList.get(licensePlate).add(carInfo);
	}
	
	public Set<CarInfo> findAll(String licensePlate){
		return carInfoList.get(licensePlate);
	}

}
