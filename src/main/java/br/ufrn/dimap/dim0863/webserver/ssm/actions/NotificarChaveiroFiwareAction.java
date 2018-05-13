package br.ufrn.dimap.dim0863.webserver.ssm.actions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import br.ufrn.dimap.dim0863.webserver.dominio.ReservaChaveiro;
import br.ufrn.dimap.dim0863.webserver.fiware.FIWAREController;
import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;

public abstract class NotificarChaveiroFiwareAction implements Action<Situacao, Evento> , ApplicationContextAware {

	private ApplicationContext ctx;
	
	public abstract String getCommand();
	
	@Override
	public void execute(StateContext<Situacao, Evento> context) {
		
		if( !ctx.getEnvironment().acceptsProfiles("fiware")) {
			System.err.println("FIWARE Desabilitado");
			return;
		}
		
		try {
			
			ReservaChaveiro reserva = (ReservaChaveiro) context.getMessageHeader(ReservaChaveiro.class.getName());
			
			String params[] = new String[]{ getCommand() , Integer.toString(reserva.getChave())};;
			List<String> paramsList = (List<String>)Arrays.asList(params);
			ctx.getBean(FIWAREController.class).sendCommand(reserva.getChaveiro(), reserva.getChaveiro(), "change_state", paramsList);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to send command to FIWARE");
		}
		
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ctx = arg0;
	}
}
