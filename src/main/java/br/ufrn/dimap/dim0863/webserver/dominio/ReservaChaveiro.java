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
	
	private String rfidTag;
	
	public ReservaChaveiro(String login, String chaveiro, Integer chave, String rfidTag) {
		super();
		this.login = login;
		this.chaveiro = chaveiro;
		this.chave = chave;
		this.rfidTag = rfidTag;
	}

	public String getRfidTag() {
		return rfidTag;
	}

	public void setRfidTag(String rfidTag) {
		this.rfidTag = rfidTag;
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
	
	@Override
	public String toString() {
		return "Chave/Carro = " + chave  + " | Login = " + login ;
	}

}
