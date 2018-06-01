package br.ufrn.dimap.dim0863.webserver.web.dto;

import java.util.List;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;


public class LocalizacaoListResponse {

	private String login;
	private List<Localizacao> localizacaoList;
	
	public LocalizacaoListResponse(String login, List<Localizacao> localizacaoList) {
		super();
		this.login = login;
		this.localizacaoList = localizacaoList;
	}
	
	public String getLogin() {
		return login;
	}
	
	public List<Localizacao> getLocalizacaoList() {
		return localizacaoList;
	}

}
