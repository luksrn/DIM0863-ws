package br.ufrn.dimap.dim0863.webserver.negocio;

import java.util.List;

import org.springframework.stereotype.Component;

import br.ufrn.dimap.dim0863.webserver.dominio.Location;
import br.ufrn.dimap.dim0863.webserver.repositorio.LocalizacaoUsuarioRepository;


@Component
public class LocalizacaoUsuarioService {

	LocalizacaoUsuarioRepository repositorio;

	public LocalizacaoUsuarioService(LocalizacaoUsuarioRepository repositorio) {
		this.repositorio = repositorio;
	}

	public void enviarLocalizacao(String login, Location location)  throws Exception  {
		repositorio.add(login, location);
	}

	public List<Location> findLocalizacao(String login)  throws Exception  {
		return repositorio.findAll(login);
	}

}
