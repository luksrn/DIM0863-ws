package br.ufrn.dimap.dim0863.webserver.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.dimap.dim0863.webserver.negocio.ReservaChaveiroService;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

@Controller
@RequestMapping("/api/v1")
public class ChaveiroController {
	
	ReservaChaveiroService reservaChaveiroService;
	
	public ChaveiroController(ReservaChaveiroService reservaChaveiro) {
		this.reservaChaveiroService = reservaChaveiro;
	}

	@PostMapping(value="/chaveiro")
	public ResponseEntity<ChaveiroResponse> chave(
			@RequestBody ChaveiroRequest request) throws Exception {
		return ResponseEntity.ok(reservaChaveiroService.chaveiro(request));
	}
	
	@PostMapping(value="/portao")
	public ResponseEntity<ChaveiroResponse> portao(
			@RequestBody PortaoRequest request)  throws Exception  {
		return ResponseEntity.ok(reservaChaveiroService.portao(request));
	}

	@PostMapping(value="/sensor-portao")
	public ResponseEntity<ChaveiroResponse> sensorPortao(
			@RequestBody PortaoRequest request)  throws Exception {
		return ResponseEntity.ok(reservaChaveiroService.sensorPortao(request));
	}

	@GetMapping(value="/status")
	public ResponseEntity<?> status() {
		return ResponseEntity.ok(reservaChaveiroService.statusServico());
	}

}
