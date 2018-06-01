package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;

@Component
public class LocalizacaoUsuarioRepository {

	private static Map<String, List<Localizacao>> userLocationList;
	
	private LocalizacaoUsuarioRepository() {
		userLocationList = new HashMap<>();
		userLocationList.put("luksrn", new ArrayList<>());
		userLocationList.put("rafael", new ArrayList<>());
		userLocationList.put("lucascriistiano", new ArrayList<>());
	}
		
	public void add(String user, Localizacao location) {
		userLocationList.get(user).add(location);
	}
	
	public List<Localizacao> findAll(String user){
		return userLocationList.get(user);
	}

}
