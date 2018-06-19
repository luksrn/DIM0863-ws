package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;
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

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;
import br.ufrn.dimap.dim0863.webserver.dominio.Location;
import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.negocio.CarInfoService;
import br.ufrn.dimap.dim0863.webserver.negocio.FirebaseService;
import br.ufrn.dimap.dim0863.webserver.negocio.LocalizacaoUsuarioService;
import br.ufrn.dimap.dim0863.webserver.negocio.ReservaChaveiroService;
import br.ufrn.dimap.dim0863.webserver.ssm.AppNotification;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.web.dto.CarInfoListResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.CarInfoRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.UpdateTokenRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.UserLocationListResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.UserLocationRequest;

@Controller
@RequestMapping("/api/v1")
public class ChaveiroController {

	@Autowired
	ReservaChaveiroService reservaChaveiroService;

	@Autowired
	LocalizacaoUsuarioService localizacaoUsuarioService;

	@Autowired
	CarInfoService carInfoService;

	@Autowired
	FirebaseService firebaseService;

	@Autowired
	StateMachine<Situacao, Evento> stateMachine;

	@Autowired
	StateMachinePersister<Situacao, Evento, String> stateMachinePersister;


	@PostMapping(value="/chaveiro")
	public ResponseEntity<ChaveiroResponse> chave(@RequestBody ChaveiroRequest request) throws Exception {
		ReservaChaveiro reserva = reservaChaveiroService.chaveiro(request);
		change(reserva, Evento.INTERAGIR_CHAVEIRO);

		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}

	@PostMapping(value="/portao")
	public ResponseEntity<ChaveiroResponse> portao(@RequestBody PortaoRequest request)  throws Exception  {
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_PORTAO);

		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}

	@PostMapping(value="/sensor-portao")
	public ResponseEntity<ChaveiroResponse> sensorPortao(@RequestBody PortaoRequest request)  throws Exception {
		ReservaChaveiro reserva = reservaChaveiroService.portao(request);
		change(reserva, Evento.INTERAGIR_SENSOR_PORTAO);

		return ResponseEntity.ok(buildChaveiroResponse(reserva));
	}

	@PostMapping(value="/sensor-portao/change")
	public ResponseEntity<ChaveiroResponse> sensorPortaoChange(@RequestBody String request)  throws Exception {
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
	public ResponseEntity<ChaveiroResponse> leitorRFIDChange(@RequestBody String request)  throws Exception {
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

	@PostMapping(value="/location")
	public ResponseEntity<String> enviarLocalizacao(@RequestBody UserLocationRequest request)  throws Exception {
		localizacaoUsuarioService.enviarLocalizacao(request);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	@GetMapping(value="/location/{login}")
	public ResponseEntity<?> listarLocalizacao(@PathVariable("login") String login) throws Exception {
		List<Location> localizacaoList = localizacaoUsuarioService.findLocalizacao(login);

		if(localizacaoList != null) {
			return ResponseEntity.ok(buildLocalizacaoListResponse(login, localizacaoList));
		}
		return ResponseEntity.unprocessableEntity().build();
	}

	@PostMapping(value="/car/data")
	public ResponseEntity<String> postCarInfo(@RequestBody CarInfoRequest request)  throws Exception {
		String licensePlate = request.getLicensePlate();

		CarInfo carInfo = request.getCarInfo();
		System.out.println(licensePlate + " - " + carInfo.toString());

		carInfoService.sendCarInfo(licensePlate, carInfo);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	@GetMapping(value="/car/data/{licensePlate}")
	public ResponseEntity<?> listCarInfo(@PathVariable("licensePlate") String licensePlate) throws Exception {
		Set<CarInfo> carInfoList = carInfoService.findCarInfo(licensePlate);

		if(carInfoList != null) {
			return ResponseEntity.ok(buildCarInfoListResponse(licensePlate, carInfoList));
		}

		return ResponseEntity.unprocessableEntity().build();
	}

	@PostMapping(value="/firebase/update-token")
	public ResponseEntity<String> updateFirebaseToken(@RequestBody UpdateTokenRequest request) throws Exception {
		String login = request.getLogin();
		String token = request.getToken();
		firebaseService.updateToken(login, token);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	//TODO Remove this test method
	@PostMapping(value="/firebase/notify/{login}/{action}")
	public ResponseEntity<String> testUserNotification(@PathVariable("login") String login, @PathVariable("action") String action) throws Exception {
		JSONObject responseJson = new JSONObject();

		if(action != null) {
			if (action.equals("start")) {
				firebaseService.notifyUser(login, AppNotification.START_COLLECT);
				responseJson.put("result", "success");
			} else if (action.equals("stop")) {
				firebaseService.notifyUser(login, AppNotification.STOP_COLLECT);
				responseJson.put("result", "success");
			} else {
				responseJson.put("result", "error");
				responseJson.put("error", "Unknown action '" + action + "'");
			}
		} else {
			responseJson.put("result", "error");
			responseJson.put("error", "No action provided");
		}

		return ResponseEntity.ok(responseJson.toString());
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
		return new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), stateMachine.getState().getId().name() );
	}

	protected UserLocationListResponse buildLocalizacaoListResponse(String login, List<Location> localizacaoList) {
		return new UserLocationListResponse(login, localizacaoList);
	}

	protected CarInfoListResponse buildCarInfoListResponse(String licensePlate, Set<CarInfo> carInfoList) {
		return new CarInfoListResponse(licensePlate, carInfoList);
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
