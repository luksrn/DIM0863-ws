package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;
import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.negocio.LocalizacaoUsuarioService;
import br.ufrn.dimap.dim0863.webserver.negocio.ReservaChaveiroService;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.LocalizacaoListResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.LocalizacaoRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.LocalizacaoResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

@Controller
@RequestMapping("/api/v1")
public class ChaveiroController {

	@Autowired
	ReservaChaveiroService reservaChaveiroService;
	
	@Autowired
	LocalizacaoUsuarioService localizacaoUsuarioService;

	@Autowired
	StateMachine<Situacao, Evento> stateMachine;
	
	@Autowired
	StateMachinePersister<Situacao, Evento, String> stateMachinePersister;

	
	@PostMapping(value="/chaveiro")
	public ResponseEntity<ChaveiroResponse> chave(
			@RequestBody ChaveiroRequest request) throws Exception {
		
		ReservaChaveiro reserva = reservaChaveiroService.chaveiro(request);
		change(reserva, Evento.INTERAGIR_CHAVEIRO);
		
		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}
	
	@PostMapping(value="/portao")
	public ResponseEntity<ChaveiroResponse> portao(
			@RequestBody PortaoRequest request)  throws Exception  {
		
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_PORTAO);
		
		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}

	@PostMapping(value="/sensor-portao")
	public ResponseEntity<ChaveiroResponse> sensorPortao(
			@RequestBody PortaoRequest request)  throws Exception {
		
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_SENSOR_PORTAO);
		
		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}
	
	@PostMapping(value="/localizacao")
	public ResponseEntity<LocalizacaoResponse> enviarLocalizacao(
			@RequestBody LocalizacaoRequest request)  throws Exception {
		
		Localizacao localizacao = localizacaoUsuarioService.enviarLocalizacao(request);
		return ResponseEntity.ok(buildLocalizacaoResponse(request.getLogin(), localizacao));
	}
	
	@GetMapping(value="/localizacao/{login}")
	public ResponseEntity<?> listarLocalizacao(@PathVariable("login") String login) throws Exception {
		List<Localizacao> localizacaoList = localizacaoUsuarioService.findLocalizacao(login);

		if(localizacaoList != null) {
			return ResponseEntity.ok(buildLocalizacaoListResponse(login, localizacaoList));
		}
		return ResponseEntity.unprocessableEntity().build();
	}
	
	@PostMapping(value="/sensor-portao/change")
	public ResponseEntity<ChaveiroResponse> sensorPortaoChange(
			@RequestBody String request)  throws Exception {
		JsonNode jsonNode = new ObjectMapper().readTree(request);
		JsonNode tagNode = jsonNode.get("contextResponses").get(0).get("contextElement").get("attributes").get(0).get("value");
		double distance = tagNode.asDouble();
	
		System.out.println(distance);
		
//		Optional<ReservaChaveiro> reserva = reservaChaveiroService.findByRFIDTag(rfidTag);
//		if( reserva.isPresent() ) {
//			ReservaChaveiro r = reserva.get();
//			change(r, Evento.INTERAGIR_PORTAO);			
//			change(r, Evento.INTERAGIR_SENSOR_PORTAO);
//			
//			return ResponseEntity.ok(buildResponse(r));
//		}
		return ResponseEntity.unprocessableEntity().build();
	}
	
	@PostMapping(value="/leitor-rfid/change")
	public ResponseEntity<ChaveiroResponse> leitorRFIDChange(
			@RequestBody String request)  throws Exception {
		JsonNode jsonNode = new ObjectMapper().readTree(request);
		JsonNode tagNode = jsonNode.get("contextResponses").get(0).get("contextElement").get("attributes").get(0).get("value");
		String rfidTag = tagNode.textValue();
	
		System.out.println(rfidTag);
		
		Optional<ReservaChaveiro> reserva = reservaChaveiroService.findByRFIDTag(rfidTag);
		if( reserva.isPresent() ) {
			ReservaChaveiro r = reserva.get();
			change(r, Evento.INTERAGIR_PORTAO);			
			change(r, Evento.INTERAGIR_SENSOR_PORTAO);
			
			return ResponseEntity.ok(buildChaveiroResponse(r));
		}
		return ResponseEntity.unprocessableEntity().build();
	}

	@GetMapping(value="/status/{login}")
	public ResponseEntity<?> status(@PathVariable("login") String login) throws Exception {
		
		Optional<ReservaChaveiro> reserva = reservaChaveiroService.findByLogin(login);
		if( reserva.isPresent() ) {
			ReservaChaveiro r = reserva.get();
			resetStateMachineFromStore(r.getLogin());
			return ResponseEntity.ok(buildChaveiroResponse(r));
		}
		return ResponseEntity.unprocessableEntity().build();
	}


	protected ChaveiroResponse buildChaveiroResponse(ReservaChaveiro reserva) {
		return  new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), stateMachine.getState().getId().name() );
	}
	
	protected LocalizacaoResponse buildLocalizacaoResponse(String login, Localizacao localizacao) {
		return new LocalizacaoResponse(login, localizacao);
	}
	
	protected LocalizacaoListResponse buildLocalizacaoListResponse(String login, List<Localizacao> localizacaoList) {
		return new LocalizacaoListResponse(login, localizacaoList);
	}
	
	// StateMachine
	
	private StateMachine<Situacao, Evento> resetStateMachineFromStore(String user) throws Exception {
			return stateMachinePersister.restore(stateMachine, "dim0863:" + user);
	}

	private boolean feedMachine(ReservaChaveiro reserva, Evento id) throws Exception {
		Message<Evento> event = MessageBuilder
								        .withPayload(id)
								        .setHeader(ReservaChaveiro.class.getName(), reserva).build();
		
		boolean eventSent = stateMachine.sendEvent(event);
		stateMachinePersister.persist(stateMachine, "dim0863:" + reserva.getLogin());
		return eventSent;
	}
	
	public boolean change(ReservaChaveiro r, Evento event) throws Exception {
		resetStateMachineFromStore(r.getLogin());
		return feedMachine(r, event);
	}
}
