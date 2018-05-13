package br.ufrn.dimap.dim0863.webserver;

import java.util.EnumSet;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;

import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;

@Configuration
public class StateMachineConfig {

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ProxyFactoryBean stateMachine() {
		ProxyFactoryBean pfb = new ProxyFactoryBean();
		pfb.setTargetSource(poolTargetSource());
		return pfb;
	}

	@Bean
	public CommonsPool2TargetSource poolTargetSource() {
		CommonsPool2TargetSource pool = new CommonsPool2TargetSource();
		pool.setMaxSize(3);
		pool.setTargetBeanName("stateMachineTarget");
		return pool;
	}

	@Bean(name = "stateMachineTarget")
	@Scope(scopeName="prototype")
	public StateMachine<Situacao, Evento> stateMachineTarget() throws Exception {
		Builder<Situacao, Evento> builder = StateMachineBuilder.<Situacao, Evento>builder();

		builder.configureConfiguration()
			.withConfiguration()
				.autoStartup(true);

		builder.configureStates()
			.withStates()
			.initial(Situacao.DISPONIVEL)
			.states(EnumSet.allOf(Situacao.class));

		builder.configureTransitions()
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
		
		return builder.build();
	}

/*	@Bean
	public Action<Situacao, Evento> pageviewAction() {
		return new Action<Situacao, Evento>() {

			@Override
			public void execute(StateContext<Situacao, Evento> context) {
				String variable = context.getTarget().getId().toString();
				Integer count = context.getExtendedState().get(variable, Integer.class);
				if (count == null) {
					context.getExtendedState().getVariables().put(variable, 1);
				} else {
					context.getExtendedState().getVariables().put(variable, (count + 1));
				}
			}
		};
	}
*/
/*	@Bean
	public Action<Situacao, Evento> addAction() {
		return new Action<Situacao, Evento>() {

			@Override
			public void execute(StateContext<Situacao, Evento> context) {
				Integer count = context.getExtendedState().get("COUNT", Integer.class);
				if (count == null) {
					context.getExtendedState().getVariables().put("COUNT", 1);
				} else {
					context.getExtendedState().getVariables().put("COUNT", (count + 1));
				}
			}
		};
	}

	@Bean
	public Action<Situacao, Evento> delAction() {
		return new Action<Situacao, Evento>() {

			@Override
			public void execute(StateContext<Situacao, Evento> context) {
				Integer count = context.getExtendedState().get("COUNT", Integer.class);
				if (count != null && count > 0) {
					context.getExtendedState().getVariables().put("COUNT", (count - 1));
				}
			}
		};
	}

	@Bean
	public Action<Situacao, Evento> payAction() {
		return new Action<Situacao, Evento>() {

			@Override
			public void execute(StateContext<Situacao, Evento> context) {
				context.getExtendedState().getVariables().put("PAYED", true);
			}
		};
	}

	@Bean
	public Action<Situacao, Evento> resetAction() {
		return new Action<Situacao, Evento>() {

			@Override
			public void execute(StateContext<Situacao, Evento> context) {
				context.getExtendedState().getVariables().clear();
			}
		};
	}

*/	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	public StateMachinePersist<Situacao, Evento, String> stateMachinePersist(RedisConnectionFactory connectionFactory) {
		RedisStateMachineContextRepository<Situacao, Evento> repository =
				new RedisStateMachineContextRepository<Situacao, Evento>(connectionFactory);
		return new RepositoryStateMachinePersist<Situacao, Evento>(repository);
	}

	@Bean
	public RedisStateMachinePersister<Situacao, Evento> redisStateMachinePersister(
			StateMachinePersist<Situacao, Evento, String> stateMachinePersist) {
		return new RedisStateMachinePersister<Situacao, Evento>(stateMachinePersist);
	}
}