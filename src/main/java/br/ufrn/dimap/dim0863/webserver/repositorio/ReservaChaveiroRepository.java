package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;

@Component
public class ReservaChaveiroRepository {

	private static List<ReservaChaveiro> RESERVAS;
	
	static {
		RESERVAS = new ArrayList<>();
		RESERVAS.add( new ReservaChaveiro("rafael", "KEYCHAIN_001", 1 ) );
		RESERVAS.add( new ReservaChaveiro("luksrn", "KEYCHAIN_001", 2 ) ); // A chave 02 está reservada no chaveiro Keychain.
		RESERVAS.add( new ReservaChaveiro("lucas" , "KEYCHAIN_002", 1 ) );
		RESERVAS.add( new ReservaChaveiro( null   , "KEYCHAIN_002", 2 ) );
	}
	
	@Autowired
	private StateMachine<Situacao, Evento> stateMachine;

	@Autowired
	private StateMachinePersister<Situacao, Evento, String> stateMachinePersister;
	
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
	
	private StateMachine<Situacao, Evento> resetStateMachineFromStore(String user) throws Exception {
			return stateMachinePersister.restore(stateMachine, "dim0863:" + user);
	}

	private boolean feedMachine(String user, Evento id) throws Exception {
		boolean eventSent = stateMachine.sendEvent(id);
		stateMachinePersister.persist(stateMachine, "dim0863:" + user);
		return eventSent;
	}
	
	public boolean change(ReservaChaveiro r, Evento event) throws Exception {
		resetStateMachineFromStore(r.getLogin());
		return feedMachine(r.getLogin(), event);
	}
}
