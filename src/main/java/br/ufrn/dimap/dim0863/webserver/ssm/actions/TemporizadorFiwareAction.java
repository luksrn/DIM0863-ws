package br.ufrn.dimap.dim0863.webserver.ssm.actions;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.persist.StateMachinePersister;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;

public class TemporizadorFiwareAction implements Action<Situacao, Evento> , ApplicationContextAware {

	private ApplicationContext ctx;
	private StateMachinePersister<Situacao, Evento, String> stateMachinePersister;

	public TemporizadorFiwareAction(StateMachinePersister<Situacao, Evento, String> stateMachinePersister) {
		this.stateMachinePersister = stateMachinePersister;
	}

	@Override
	public void execute(StateContext<Situacao, Evento> context) {

		if( !ctx.getEnvironment().acceptsProfiles("fiware")) {
			System.err.println("FIWARE Desabilitado");
			return;
		}

		try {
			ReservaChaveiro reserva = (ReservaChaveiro) context.getMessageHeader(ReservaChaveiro.class.getName());

			StateMachine<Situacao,Evento> stateMachine = context.getStateMachine();
			stateMachinePersister.restore(stateMachine, "dim0863:luksrn");

			Message<Evento> event = MessageBuilder
					.withPayload(Evento.INTERAGIR_SENSOR_PORTAO)
			        .setHeader(ReservaChaveiro.class.getName(), reserva).build();

//			boolean eventSent =
			stateMachine.sendEvent(event);
			stateMachinePersister.persist(stateMachine, "dim0863:luksrn"); //TODO Change
			//return eventSent;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to send command to FIWARE");
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ctx = arg0;
	}
}
