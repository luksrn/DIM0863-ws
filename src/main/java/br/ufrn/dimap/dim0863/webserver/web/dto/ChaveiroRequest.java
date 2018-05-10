package br.ufrn.dimap.dim0863.webserver.web.dto;

/**
 * Requisição para o chaveiro liberar/entregar uma chave. 
 */
public class ChaveiroRequest {

	/**
	 * Usuário que está solicitando uma chave ao chaveiro.
	 */
	private String login;
	
	/**
	 * Identificador da "Thing" responsável por controlar o chaveiro.
	 */
	private String chaveiro;
	

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getChaveiro() {
		return chaveiro;
	}

	public void setChaveiro(String chaveiro) {
		this.chaveiro = chaveiro;
	}
	
	
}
