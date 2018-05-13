package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.negocio.ReservaChaveiroService;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

@Controller
@RequestMapping("/api/v1")
public class ChaveiroController {

	@Autowired
	ReservaChaveiroService reservaChaveiroService;

	@Autowired
	StateMachine<Situacao, Evento> stateMachine;
	
	@Autowired
	StateMachinePersister<Situacao, Evento, String> stateMachinePersister;

	
	@PostMapping(value="/chaveiro")
	public ResponseEntity<ChaveiroResponse> chave(
			@RequestBody ChaveiroRequest request) throws Exception {
		
		ReservaChaveiro reserva = reservaChaveiroService.chaveiro(request);
		change(reserva, Evento.INTERAGIR_CHAVEIRO);
		
		return ResponseEntity.ok(buildResponse(reserva));
	}
	
	@PostMapping(value="/portao")
	public ResponseEntity<ChaveiroResponse> portao(
			@RequestBody PortaoRequest request)  throws Exception  {
		
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_PORTAO);
		
		return ResponseEntity.ok(buildResponse(reserva));
	}

	@PostMapping(value="/sensor-portao")
	public ResponseEntity<ChaveiroResponse> sensorPortao(
			@RequestBody PortaoRequest request)  throws Exception {
		
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_SENSOR_PORTAO);
		
		return ResponseEntity.ok(buildResponse(reserva));
	}

	@GetMapping(value="/status/{login}")
	public ResponseEntity<?> status(@PathVariable("login") String login) throws Exception {
		
		Optional<ReservaChaveiro> reserva = reservaChaveiroService.findByLogin(login);
		if( reserva.isPresent() ) {
			ReservaChaveiro r = reserva.get();
			resetStateMachineFromStore(r.getLogin());
			return ResponseEntity.ok(buildResponse(r));
		}
		return ResponseEntity.unprocessableEntity().build();
	}


	protected ChaveiroResponse buildResponse(ReservaChaveiro reserva) {
		return  new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), stateMachine.getState().getId().name() );
	}
	
	// StateMachine
	
	private StateMachine<Situacao, Evento> resetStateMachineFromStore(String user) throws Exception {
			return stateMachinePersister.restore(stateMachine, "dim0863:" + user);
	}

	private boolean feedMachine(String user, Evento id) throws Exception {
		boolean eventSent = stateMachine.sendEvent(id);
		stateMachinePersister.persist(stateMachine, "dim0863:" + user);
		return eventSent;
	}
	
	public boolean change(ReservaChaveiro r, Evento event) throws Exception {
		resetStateMachineFromStore(r.getLogin());
		return feedMachine(r.getLogin(), event);
	}
}
