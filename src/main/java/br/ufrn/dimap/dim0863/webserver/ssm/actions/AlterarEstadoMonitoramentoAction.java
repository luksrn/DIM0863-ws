package br.ufrn.dimap.dim0863.webserver.ssm.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import br.ufrn.dimap.dim0863.webserver.negocio.FirebaseService;
import br.ufrn.dimap.dim0863.webserver.negocio.exception.FirebaseTokenException;
import br.ufrn.dimap.dim0863.webserver.ssm.AppNotification;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;


public abstract class AlterarEstadoMonitoramentoAction implements Action<Situacao, Evento> {

	@Autowired
	FirebaseService firebaseService;

	public abstract AppNotification getCommand();

	@Override
	public void execute(StateContext<Situacao, Evento> context) {
		try {
			String login = "luksrn"; //TODO Change to get user login

			firebaseService.notifyUser(login, getCommand());
//			System.out.println(String.format("Change monitoring state to %s for user %s", getCommand().name(), login));
		} catch (FirebaseTokenException e) {
			System.out.println("Failed to send message to Firebase: " + e.getMessage());
		}
	}

}
