package br.ufrn.dimap.dim0863.webserver.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.negocio.ReservaChaveiroService;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;

@Controller
public class ChaveiroController {
	
	ReservaChaveiroService reservaChaveiroService;
	
	public ChaveiroController(ReservaChaveiroService reservaChaveiro) {
		this.reservaChaveiroService = reservaChaveiro;
	}

	@PostMapping(name="/api/v1/chaveiro")
	public ResponseEntity<ChaveiroResponse> requisitarChave(
			@RequestBody ChaveiroRequest request) {

		try {
			ChaveiroResponse response = reservaChaveiroService.processar(request);
			return ResponseEntity.ok(response);
		} catch (ChaveNaoDisponivelException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
