package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;

@Component
public class ReservaChaveiroRepository {

	private static List<ReservaChaveiro> RESERVAS;
	
	static {
		RESERVAS = new ArrayList<>();
		
		RESERVAS.add( new ReservaChaveiro("luksrn", "keychain_001", 2 ) ); // A chave 02 está reservada no chaveiro Keychain.
	}
	
	public void save(ReservaChaveiro reserva) {
		RESERVAS.add(reserva);
	}
	
	public List<ReservaChaveiro> findAll(){
		return RESERVAS;
	}
}
