package br.ufrn.dimap.dim0863.webserver.ssm;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;

@Component
public class TempoEsgotadoPassarEmPortaoActionTrigger implements Action<Situacao, Evento>{

	@Override
	public void execute(StateContext<Situacao, Evento> context) {
		ReservaChaveiro r =  (ReservaChaveiro)context.getExtendedState().get(ReservaChaveiro.class.getName(), ReservaChaveiro.class);
		System.out.println("FECHANDO O PORTAO AUTOMATICAMENTE - Reserva: " + r.getLogin() + " Carro " + r.getChave() );
	}

}
