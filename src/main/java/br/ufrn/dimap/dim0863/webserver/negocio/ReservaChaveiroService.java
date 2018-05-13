package br.ufrn.dimap.dim0863.webserver.negocio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.exceptions.EstadoNaoPermitidoException;
import br.ufrn.dimap.dim0863.webserver.fiware.FIWAREController;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.ssm.StateMachineLogListener;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

/**
 * Serviço que controla o estado das reservas.
 *
 */
@Component
public class ReservaChaveiroService {
	
	ReservaChaveiroRepository repositorio;
	
	StateMachine<Situacao, Evento> stateMachine;
	
	FIWAREController fiwareController;
	
	public ReservaChaveiroService(ReservaChaveiroRepository repositorio,
			StateMachine<Situacao, Evento> stateMachine,
			Optional<FIWAREController> fiware) {
		this.repositorio = repositorio;
		this.stateMachine = stateMachine;
		if(fiware.isPresent()) {
			fiwareController = fiware.get();
		}
		stateMachine.addStateListener(new StateMachineLogListener());
	}

	public ChaveiroResponse chaveiro(ChaveiroRequest request) {
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChaveiro().equals(request.getChaveiro());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return processarComando(reserva, Evento.INTERAGIR_CHAVEIRO);
	}
	
	public ChaveiroResponse portao(PortaoRequest request) {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChave().equals(request.getChave());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return processarComando(reserva, Evento.INTERAGIR_PORTAO);
	}
	
	public ChaveiroResponse sensorPortao(PortaoRequest request) {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChave().equals(request.getChave());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
				
		return processarComando(reserva, Evento.INTERAGIR_SENSOR_PORTAO);
	}
	
	
	/**
	 * Status das reservas.
	 * 
	 * @return
	 */
	public List<ChaveiroResponse> statusServico() {
		
		return repositorio.findAll()
				.stream()
				.map( reserva -> new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), reserva.getStatus() ))
				.collect(Collectors.toList());
		
	}

	/**
	 * TODO Verificar feedback do FIWARE para integridade de status.
	 * @return
	 */
	private ChaveiroResponse processarComando(ReservaChaveiro reserva, Evento evento) {
		
		if( repositorio.change(reserva, evento ) ) { // Altera status local
			
			notificarFiware(reserva);
			
			return  new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), reserva.getStatus() );
		} else {
			throw new EstadoNaoPermitidoException();
		}
	}

	private void notificarFiware(ReservaChaveiro reserva) {
		if( fiwareController == null ) {
			System.out.println("FIWARE Profile disabled.");
		}
		
		try {
			//Send commands to FIWARE
			String params[] = new String[]{"OPEN", Integer.toString(reserva.getChave())};
			List<String> paramsList = (List<String>)Arrays.asList(params);
			fiwareController.sendCommand(reserva.getChaveiro(), reserva.getChaveiro(), "change_state", paramsList);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to send command to FIWARE");
		}
		
	}
	
}
