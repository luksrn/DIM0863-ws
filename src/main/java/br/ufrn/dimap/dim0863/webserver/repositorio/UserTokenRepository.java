package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class UserTokenRepository {

	private static Map<String, String> userTokenMap;

	private UserTokenRepository() {
		userTokenMap = new HashMap<>();
		userTokenMap.put("rafael", "");
		userTokenMap.put("luksrn", "");
		userTokenMap.put("lucascriistiano", "");
	}

	public void update(String login, String token) {
		userTokenMap.put(login, token);
	}

	public String find(String login) {
		return userTokenMap.get(login);
	}

}
