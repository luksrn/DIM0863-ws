package br.ufrn.dimap.dim0863.webserver.ssm;

import java.util.LinkedList;
import java.util.List;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

public class StateMachineLogListener extends StateMachineListenerAdapter<Situacao , Evento> {

	private final LinkedList<String> messages = new LinkedList<String>();

	public List<String> getMessages() {
		return messages;
	}

	public void resetMessages() {
		messages.clear();
	}

	@Override
	public void stateContext(StateContext<Situacao , Evento> stateContext) {
		if (stateContext.getStage() == Stage.STATE_ENTRY) {
			messages.addFirst("Enter " + stateContext.getTarget().getId());
		} else if (stateContext.getStage() == Stage.STATE_EXIT) {
			messages.addFirst("Exit " + stateContext.getSource().getId());
		} else if (stateContext.getStage() == Stage.STATEMACHINE_START) {
			messages.addLast("Machine started");
		} else if (stateContext.getStage() == Stage.STATEMACHINE_STOP) {
			messages.addFirst("Machine stopped");
		}
	}
}