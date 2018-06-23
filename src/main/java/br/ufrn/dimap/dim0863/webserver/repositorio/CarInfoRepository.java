package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;

@Component
public class CarInfoRepository {

	private static Map<String, Set<CarInfo>> carInfoMap;

	private CarInfoRepository() {
		carInfoMap = new HashMap<>();
		carInfoMap.put("ABC-1234", new HashSet<>());
		carInfoMap.put("DEF-1234", new HashSet<>());
		carInfoMap.put("GH1-1234", new HashSet<>());
	}

	public void add(String licensePlate, CarInfo carInfo) {
		carInfoMap.get(licensePlate).add(carInfo);
	}

	public List<CarInfo> findAll(String licensePlate){
		List<CarInfo> carInfoList = new ArrayList<>();
		carInfoList.addAll(carInfoMap.get(licensePlate));
		carInfoList.sort((info1, info2) -> info1.compareTo(info2));
		return carInfoList;
	}

}
