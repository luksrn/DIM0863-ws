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
	private int numeroChave;
	
	public ChaveiroResponse(int numeroChave) {
		super();
		this.numeroChave = numeroChave;
	}

	public int getNumeroChave() {
		return numeroChave;
	}

	public void setNumeroChave(int numeroChave) {
		this.numeroChave = numeroChave;
	}
}
