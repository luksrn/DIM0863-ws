package br.ufrn.dimap.dim0863.webserver.ssm.actions;

public class NotificarFechamentoPortaoFiwareAction extends NotificarPortaoFiwareAction {
	
	@Override
	public String getCommand() {
		return "CLOSED";
	}
}
