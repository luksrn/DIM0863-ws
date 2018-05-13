package br.ufrn.dimap.dim0863.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.PersistStateMachineHandler;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.ssm.TempoEsgotadoPassarEmPortaoActionTrigger;

@SpringBootApplication
public class WebserverApplication {
	
	@Configuration
	@EnableStateMachine
	static class StateMachineConfig
			extends EnumStateMachineConfigurerAdapter<Situacao, Evento> {

		@Override
		public void configure(StateMachineStateConfigurer<Situacao, Evento> states)
				throws Exception {
			states
				.withStates()
					.initial(Situacao.DISPONIVEL)
					.state(Situacao.EM_TRANSITO_INTERNO)
					.state(Situacao.AGUARDANDO_SAIR)
					.state(Situacao.EM_TRANSITO_EXTERNO)
					.state(Situacao.AGUARDANDO_ENTRAR);
		}
		
		@Override
		public void configure(StateMachineTransitionConfigurer<Situacao, Evento> transitions)
				throws Exception {
			transitions
				.withExternal()
					.source(Situacao.DISPONIVEL).target(Situacao.EM_TRANSITO_INTERNO)
					.event(Evento.INTERAGIR_CHAVEIRO)
					.and()
				.withExternal()
					.source(Situacao.EM_TRANSITO_INTERNO).target(Situacao.AGUARDANDO_SAIR)
					.event(Evento.INTERAGIR_PORTAO)					
					.and()
				.withExternal()
					.source(Situacao.AGUARDANDO_SAIR).target(Situacao.EM_TRANSITO_EXTERNO)
					.event(Evento.INTERAGIR_SENSOR_PORTAO)
					.and()
				.withExternal()
					.source(Situacao.EM_TRANSITO_EXTERNO).target(Situacao.AGUARDANDO_ENTRAR)
					.event(Evento.INTERAGIR_PORTAO)
					.and()
				.withExternal()
					.source(Situacao.AGUARDANDO_ENTRAR).target(Situacao.EM_TRANSITO_INTERNO)
					.event(Evento.INTERAGIR_SENSOR_PORTAO)
					.and()
				.withExternal()
					.source(Situacao.EM_TRANSITO_INTERNO).target(Situacao.DISPONIVEL)
					.event(Evento.INTERAGIR_CHAVEIRO);
				/*	.and()
				   .withInternal()
					.source(Situacao.AGUARDANDO_SAIR)
					.action(new TempoEsgotadoPassarEmPortaoActionTrigger())
					.timerOnce(10000);*/
		}

	}
	
	@Configuration
	static class PersistHandlerConfig {

		@Bean
		public PersistStateMachineHandler persistStateMachineHandler(StateMachine<Situacao, Evento> stateMachine) {
			return new PersistStateMachineHandler(stateMachine);
		}

	}

	
	public static void main(String[] args) {
		SpringApplication.run(WebserverApplication.class, args);
		
	}
}
