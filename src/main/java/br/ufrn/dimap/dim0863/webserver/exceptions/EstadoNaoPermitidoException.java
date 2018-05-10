package br.ufrn.dimap.dim0863.webserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_MODIFIED)
public class EstadoNaoPermitidoException extends RuntimeException {

	public EstadoNaoPermitidoException() {
		super("Mudança de estado não permitida.");
	}
}
