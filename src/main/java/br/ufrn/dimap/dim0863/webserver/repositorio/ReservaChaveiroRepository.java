package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;

@Component
public class ReservaChaveiroRepository {

	private static List<ReservaChaveiro> RESERVAS;
	
	static {
		RESERVAS = new ArrayList<>();
		RESERVAS.add( new ReservaChaveiro("rafael", "KEYCHAIN_001", 1 , "1") );
		RESERVAS.add( new ReservaChaveiro("luksrn", "KEYCHAIN_001", 2 , "000000000000000000007546") ); // A chave 02 está reservada no chaveiro Keychain.
		RESERVAS.add( new ReservaChaveiro("lucas" , "KEYCHAIN_002", 1 , "3") );
		RESERVAS.add( new ReservaChaveiro( null   , "KEYCHAIN_002", 2 , "4") );
	}
		
	public void save(ReservaChaveiro reserva) {
		RESERVAS.add(reserva);
	}
	
	public List<ReservaChaveiro> findAll(){
		return RESERVAS;
	}
	
	public Optional<ReservaChaveiro> findBy( Predicate<ReservaChaveiro> test ){
		
		return RESERVAS
			.stream()
			.filter( r -> r.getLogin() != null  ) // Apenas reservas
			.filter( test )
			.findAny();
	}
}
