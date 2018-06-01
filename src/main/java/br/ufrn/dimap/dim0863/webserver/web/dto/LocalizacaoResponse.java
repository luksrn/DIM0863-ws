package br.ufrn.dimap.dim0863.webserver.web.dto;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;


public class LocalizacaoResponse {

	private String login;
	private Localizacao localizacao;
	
	public LocalizacaoResponse(String login, Localizacao localizacao) {
		super();
		this.login = login;
		this.localizacao = localizacao;
	}
	
	public String getLogin() {
		return login;
	}
	
	public Localizacao getLocalizacao() {
		return localizacao;
	}

}
