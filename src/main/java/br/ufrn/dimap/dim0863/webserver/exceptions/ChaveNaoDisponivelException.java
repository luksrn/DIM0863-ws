package br.ufrn.dimap.dim0863.webserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_MODIFIED)
public class ChaveNaoDisponivelException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChaveNaoDisponivelException() {
		super("Chave não disponível");
	}
}
