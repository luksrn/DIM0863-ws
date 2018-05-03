package br.ufrn.dimap.dim0863.webserver.negocio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.exceptions.ChaveNaoDisponivelException;
import br.ufrn.dimap.dim0863.webserver.repositorio.ReservaChaveiroRepository;
import br.ufrn.dimap.dim0863.webserver.web.controller.FIWAREController;
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
		
		try {
			//Send commands to FIWARE
			String params[] = new String[]{"OPEN", Integer.toString(reserva.getChave())};
			List<String> paramsList = (List<String>)Arrays.asList(params);
			FIWAREController.sendCommand(request.getChaveiro(), request.getChaveiro(), "change_state", paramsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to send command to FIWARE");
		}
		
		return new ChaveiroResponse( reserva.getChave() );
	}
	
}
