package br.ufrn.dimap.dim0863.webserver.ssm.actions;

public class NotificarAberturaPortaoFiwareAction extends NotificarPortaoFiwareAction {
	
	@Override
	public String getCommand() {
		return "OPEN";
	}
}
