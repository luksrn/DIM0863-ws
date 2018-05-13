package br.ufrn.dimap.dim0863.webserver.fiware;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fiware")
public class FiwareProperties {

	private String token;
	
	private Idas idas;

	private String service;
	
	private String servicePath;
	
	public static class Idas {
		String host;
		
		Integer port = 4041;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}
		
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Idas getIdas() {
		return idas;
	}

	public void setIdas(Idas idas) {
		this.idas = idas;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	
}
