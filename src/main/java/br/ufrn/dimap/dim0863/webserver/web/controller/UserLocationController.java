package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.dimap.dim0863.webserver.dominio.Location;
import br.ufrn.dimap.dim0863.webserver.negocio.LocalizacaoUsuarioService;
import br.ufrn.dimap.dim0863.webserver.web.dto.UserLocationListResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.UserLocationRequest;

@Controller
@RequestMapping("/api/v1/location")
public class UserLocationController {

	@Autowired
	LocalizacaoUsuarioService localizacaoUsuarioService;

	@PostMapping(value="")
	public ResponseEntity<String> enviarLocalizacao(@RequestBody UserLocationRequest request)  throws Exception {
		localizacaoUsuarioService.enviarLocalizacao(request);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	@GetMapping(value="/{login}")
	public ResponseEntity<?> listarLocalizacao(@PathVariable("login") String login) throws Exception {
		List<Location> localizacaoList = localizacaoUsuarioService.findLocalizacao(login);

		if(localizacaoList != null) {
			return ResponseEntity.ok(buildLocalizacaoListResponse(login, localizacaoList));
		}
		return ResponseEntity.unprocessableEntity().build();
	}

	protected UserLocationListResponse buildLocalizacaoListResponse(String login, List<Location> localizacaoList) {
		return new UserLocationListResponse(login, localizacaoList);
	}

}
