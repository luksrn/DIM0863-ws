package br.ufrn.dimap.dim0863.webserver.web.dto;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;

public class LocalizacaoRequest {

	private String login;
	private Localizacao localizacao;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}
	
}
