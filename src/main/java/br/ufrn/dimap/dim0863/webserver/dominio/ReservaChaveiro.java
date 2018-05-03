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
	private int chave;
	
	public ReservaChaveiro(String login, String chaveiro, int chave) {
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
	
	public int getChave() {
		return chave;
	}
	
	public void setChave(int chave) {
		this.chave = chave;
	}
}
