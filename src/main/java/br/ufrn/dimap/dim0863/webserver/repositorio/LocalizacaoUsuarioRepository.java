package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.Location;

@Component
public class LocalizacaoUsuarioRepository {

	private static Map<String, List<Location>> usersLocationMap;

	private LocalizacaoUsuarioRepository() {
		usersLocationMap = new HashMap<>();
		usersLocationMap.put("luksrn", new ArrayList<>());
		usersLocationMap.put("rafael", new ArrayList<>());
		usersLocationMap.put("lucascriistiano", new ArrayList<>());
	}

	public void add(String user, Location location) {
		usersLocationMap.get(user).add(location);
	}

	public List<Location> findAll(String user){
		List<Location> userLocationList = usersLocationMap.get(user);
		userLocationList.sort((loc1, loc2) -> loc1.compareTo(loc2));
		return userLocationList;
	}

}
