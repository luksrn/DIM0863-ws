package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.List;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.Localizacao;
import br.ufrn.dimap.dim0863.webserver.repositorio.LocalizacaoUsuarioRepository;
import br.ufrn.dimap.dim0863.webserver.web.dto.LocalizacaoRequest;


@Component
public class LocalizacaoUsuarioService {
	
	LocalizacaoUsuarioRepository repositorio;
	
	public LocalizacaoUsuarioService(LocalizacaoUsuarioRepository repositorio) {
		this.repositorio = repositorio;
	}

	public Localizacao enviarLocalizacao(LocalizacaoRequest request)  throws Exception  {
		repositorio.add(request.getLogin(), request.getLocalizacao());
		return request.getLocalizacao();
	}
	
	public List<Localizacao> findLocalizacao(String login)  throws Exception  {
		return repositorio.findAll(login);
	}
	
}
