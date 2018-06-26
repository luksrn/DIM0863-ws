package br.ufrn.dimap.dim0863.webserver.ssm.actions;

import br.ufrn.dimap.dim0863.webserver.ssm.AppNotification;

public class IniciarMonitoramentoAction extends AlterarEstadoMonitoramentoAction {

	@Override
	public AppNotification getCommand() {
		return AppNotification.START_COLLECT;
	}

}
