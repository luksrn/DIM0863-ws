package br.ufrn.dimap.dim0863.webserver.ssm.actions;

public class NotificarFechamentoChaveiroFiwareAction extends NotificarChaveiroFiwareAction {
	
	@Override
	public String getCommand() {
		return "CLOSED";
	}
}
