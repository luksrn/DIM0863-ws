package br.ufrn.dimap.dim0863.webserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import br.ufrn.dimap.dim0863.webserver.ssm.Estados;
import br.ufrn.dimap.dim0863.webserver.ssm.Eventos;
import br.ufrn.dimap.dim0863.webserver.ssm.PersistStateMachineHandler;

@SpringBootApplication
public class WebserverApplication {
	
	@Configuration
	@EnableStateMachine
	static class StateMachineConfig
			extends EnumStateMachineConfigurerAdapter<Estados, Eventos> {

		@Override
		public void configure(StateMachineStateConfigurer<Estados, Eventos> states)
				throws Exception {
			states
				.withStates()
					.initial(Estados.DISPONIVEL)
					.state(Estados.EM_TRANSITO_INTERNO)
					.state(Estados.EM_TRANSITO_EXTERNO);
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<Estados, Eventos> transitions)
				throws Exception {
			transitions
				.withExternal()
					.source(Estados.DISPONIVEL).target(Estados.EM_TRANSITO_INTERNO)
					.event(Eventos.INTERAGIR_CHAVEIRO)
					.and()
				.withExternal()
					.source(Estados.EM_TRANSITO_INTERNO).target(Estados.EM_TRANSITO_EXTERNO)
					.event(Eventos.INTERAGIR_PORTAO)
					.and()
				.withExternal()
					.source(Estados.EM_TRANSITO_EXTERNO).target(Estados.EM_TRANSITO_INTERNO)
					.event(Eventos.INTERAGIR_PORTAO)
					.and()
				.withExternal()
					.source(Estados.EM_TRANSITO_INTERNO).target(Estados.DISPONIVEL)
					.event(Eventos.INTERAGIR_CHAVEIRO);
		}

	}
	
	@Configuration
	static class PersistHandlerConfig {

		@Autowired
		private StateMachine<Estados, Eventos> stateMachine;
		@Bean
		public PersistStateMachineHandler persistStateMachineHandler() {
			return new PersistStateMachineHandler(stateMachine);
		}

	}

	
	public static void main(String[] args) {
		SpringApplication.run(WebserverApplication.class, args);
		
	}
}
