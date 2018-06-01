package br.ufrn.dimap.dim0863.webserver.web.dto;

import java.util.List;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;


public class LocalizacaoListResponse {

	private String login;
	private List<Localizacao> localizacoes;
	
	public LocalizacaoListResponse(String login, List<Localizacao> localizacoes) {
		super();
		this.login = login;
		this.localizacoes = localizacoes;
	}
	
	public String getLogin() {
		return login;
	}
	
	public List<Localizacao> getLocalizacoes() {
		return localizacoes;
	}

}
