package br.ufrn.dimap.dim0863.webserver.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.ssm.Estados;
import br.ufrn.dimap.dim0863.webserver.ssm.Eventos;
import br.ufrn.dimap.dim0863.webserver.ssm.PersistStateMachineHandler;
import br.ufrn.dimap.dim0863.webserver.ssm.PersistStateMachineHandler.PersistStateChangeListener;

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
	

	private final PersistStateChangeListener listener = new LocalPersistStateChangeListener();

	PersistStateMachineHandler persistStateMachineHandler;
	
	public ReservaChaveiroRepository(PersistStateMachineHandler persistStateMachineHandler) {
		super();
		this.persistStateMachineHandler = persistStateMachineHandler;
		this.persistStateMachineHandler.addPersistStateChangeListener(listener);
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
			.filter( test )
			.findAny();
	}
	
	public boolean change(ReservaChaveiro r, Eventos e) {
		return persistStateMachineHandler.handleEventWithState(
				MessageBuilder.withPayload( e ).setHeader("reserva", r).build(), 
				Estados.valueOf(r.getStatus()) );
	}
	
	private class LocalPersistStateChangeListener implements PersistStateChangeListener {

		@Override
		public void onPersist(State<Estados,Eventos> state, Message<Eventos> message,
				Transition<Estados,Eventos> transition, StateMachine<Estados,Eventos> stateMachine) {
			if (message != null && message.getHeaders().containsKey("reserva")) {
				ReservaChaveiro r = message.getHeaders().get("reserva", ReservaChaveiro.class);
				r.setStatus(state.getId().toString());
				//jdbcTemplate.update("update orders set state = ? where id = ?", state.getId(), order);
			}
		}
}
}
