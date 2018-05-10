package br.ufrn.dimap.dim0863.webserver.web.dto;

/**
 * Resposta de sucesso de requisição à uma liberação de chave no chaveiro.
 *
 */
public class ChaveiroResponse {

	
	/**
	 * Identificador da "Porta" que contém a chave liberada
	 * no chaveiro.
	 */
	private Integer numeroChave;
	
	/**
	 * Identifica o status do chaveiro/carro.
	 */
	private String status;
	
	/**
	 * Se há responsável com o chaveiro/carro.
	 */
	private String login;
	
	private String chaveiro;
	
	public ChaveiroResponse(String login, String chaveiro, Integer numeroChave, String status) {
		super();
		this.chaveiro = chaveiro;
		this.numeroChave = numeroChave;
		this.login = login;
		this.status = status;
	}

	public int getNumeroChave() {
		return numeroChave;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getChaveiro() {
		return chaveiro;
	}
}
