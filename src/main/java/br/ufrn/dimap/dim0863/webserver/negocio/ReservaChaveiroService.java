package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.PortaoRequest;

/**
 * Serviço que controla o estado das reservas.
 */
@Component
public class ReservaChaveiroService {
	
	ReservaChaveiroRepository repositorio;
	
	public ReservaChaveiroService(ReservaChaveiroRepository repositorio) {
		this.repositorio = repositorio;
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

	public Optional<ReservaChaveiro> findByRFIDTag(String rfidTag) {
		return repositorio.findAll()
				.stream()
				.filter( r -> r.getRfidTag().equals(rfidTag))
				.findFirst();
		
	}

}
