package br.ufrn.dimap.dim0863.webserver.dominio;

public class ReservaChaveiro {

	/**
	 * Nome do usuário que possui a reserva de uma chave.
	 */
	private String login;
	
	/**
	 * Nome do chaveiro que a reserva foi realizada.
	 */
	private String chaveiro;
	
	/**
	 * Número da chave do chaveiro.
	 */
	private Integer chave;
	
	/**
	 * Status da reserva/carro.
	 */
	private String status = "DISPONIVEL";
	
	public ReservaChaveiro(String login, String chaveiro, Integer chave) {
		super();
		this.login = login;
		this.chaveiro = chaveiro;
		this.chave = chave;
	}

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
	
	public Integer getChave() {
		return chave;
	}
	
	public void setChave(Integer chave) {
		this.chave = chave;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return " Status: " + status + " | Chave/Carro = " + chave  + " | Login = " + login ;
	}
}
