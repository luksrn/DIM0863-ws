package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroRequest;
import br.ufrn.dimap.dim0863.webserver.web.dto.ChaveiroResponse;

@Component
public class ReservaChaveiroService {
	
	ReservaChaveiroRepository repositorio;
	
	public ReservaChaveiroService(ReservaChaveiroRepository repositorio) {
		this.repositorio = repositorio;
	}

	/**
	 * Recupera uma chave reservada no chaveiro.
	 * 
	 * @param request
	 * 
	 * @throws ChaveNaoDisponivelException
	 * 
	 * @return
	 */
	public ChaveiroResponse processar(ChaveiroRequest request) {
		
		Predicate<ReservaChaveiro> reservaParaUsuario = r -> r.getLogin().equals(request.getLogin());
		Predicate<ReservaChaveiro> reservaNoChaveiro = r -> r.getChaveiro().equals(request.getChaveiro());
		
		ReservaChaveiro reserva = repositorio.findAll()
				.stream()
				.filter( reservaParaUsuario.and(reservaNoChaveiro) )
				.findAny()
				.orElseThrow( () -> new ChaveNaoDisponivelException() );
		
		return new ChaveiroResponse( reserva.getChave() );
	}
	
}
