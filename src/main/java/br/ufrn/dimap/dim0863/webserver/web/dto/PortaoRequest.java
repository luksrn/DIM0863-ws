package br.ufrn.dimap.dim0863.webserver.web.dto;

/**
 * Requisição para liberar a chancela 
 */
public class PortaoRequest {

	/**
	 * Usuário que está com o carro
	 */
	private String login;
	
	/**
	 * Identifica a chave/carro
	 */
	private Integer chave;

	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}

	public Integer getChave() {
		return chave;
	}
	
	public void setChave(Integer chave) {
		this.chave = chave;
	}
}
