package br.ufrn.dimap.dim0863.webserver.ssm.actions;

public class NotificarAberturaChaveiroFiwareAction extends NotificarChaveiroFiwareAction {
	
	@Override
	public String getCommand() {
		return "OPEN";
	}
}
