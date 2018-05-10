package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.exceptions.EstadoNaoPermitidoException;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.ssm.Estados;
import br.ufrn.dimap.dim0863.webserver.ssm.Eventos;
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
	
	StateMachine<Estados, Eventos> stateMachine;
	
	public ReservaChaveiroService(ReservaChaveiroRepository repositorio,
			StateMachine<Estados, Eventos> stateMachine) {
		this.repositorio = repositorio;
		this.stateMachine = stateMachine;
	}

	public ChaveiroResponse chaveiro(ChaveiroRequest request) {
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChaveiro().equals(request.getChaveiro());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return processarComando(reserva, Eventos.INTERAGIR_CHAVEIRO);
	}
	
	public ChaveiroResponse portao(PortaoRequest request) {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChave().equals(request.getChave());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return processarComando(reserva, Eventos.INTERAGIR_PORTAO);
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
	private ChaveiroResponse processarComando(ReservaChaveiro reserva, Eventos evento) {
		if( repositorio.change(reserva, evento ) ) { // Altera status local
			
			// Notificar FIWARE
			// TODO Refatorar
			/*try {
				//Send commands to FIWARE
				String params[] = new String[]{"OPEN", Integer.toString(reserva.getChave())};
				List<String> paramsList = (List<String>)Arrays.asList(params);
				FIWAREController.sendCommand(request.getChaveiro(), request.getChaveiro(), "change_state", paramsList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Failed to send command to FIWARE");
			}*/
			
			return  new ChaveiroResponse( reserva.getLogin(), reserva.getChaveiro(), reserva.getChave(), reserva.getStatus() );
		} else {
			throw new EstadoNaoPermitidoException();
		}
	}
	
}
