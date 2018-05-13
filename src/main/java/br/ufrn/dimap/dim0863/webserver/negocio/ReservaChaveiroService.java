package br.ufrn.dimap.dim0863.webserver.negocio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.fiware.FIWAREController;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

/**
 * Serviço que controla o estado das reservas.
 */
@Component
public class ReservaChaveiroService {
	
	ReservaChaveiroRepository repositorio;
	
	FIWAREController fiwareController;
	
	public ReservaChaveiroService(ReservaChaveiroRepository repositorio,			
			Optional<FIWAREController> fiware) {
		this.repositorio = repositorio;
		if(fiware.isPresent()) {
			fiwareController = fiware.get();
		}
	}

	public ReservaChaveiro chaveiro(ChaveiroRequest request)  throws Exception  {
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChaveiro().equals(request.getChaveiro());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return reserva;
	}
	
	public ReservaChaveiro portao(PortaoRequest request)  throws Exception {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChave().equals(request.getChave());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return reserva;
	}
	
	public ReservaChaveiro sensorPortao(PortaoRequest request) throws Exception {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChave().equals(request.getChave());
		
		ReservaChaveiro reserva = repositorio.findBy(reservaParaUsuario.and(reservaNoChaveiro))
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
				
		return reserva;
	}
	
	/**
	 * Status das reservas.
	 * 
	 * @return
	 */
	public Optional<ReservaChaveiro> findByLogin(String login) {
		
		return repositorio.findAll()
				.stream()
				.filter( r -> r.getLogin() != null && login.equals(r.getLogin()))
				.findFirst();
		
	}

	private void notificarFiware(ReservaChaveiro reserva) {
		if( fiwareController == null ) {
			System.out.println("FIWARE Profile disabled.");
			return;
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
