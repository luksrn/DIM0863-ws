package br.ufrn.dimap.dim0863.webserver;

import java.util.EnumSet;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;

import br.ufrn.dimap.dim0863.webserver.ssm.Evento;
import br.ufrn.dimap.dim0863.webserver.ssm.Situacao;
import br.ufrn.dimap.dim0863.webserver.ssm.actions.NotificarAberturaChaveiroFiwareAction;
import br.ufrn.dimap.dim0863.webserver.ssm.actions.NotificarAberturaPortaoFiwareAction;
import br.ufrn.dimap.dim0863.webserver.ssm.actions.NotificarFechamentoChaveiroFiwareAction;
import br.ufrn.dimap.dim0863.webserver.ssm.actions.NotificarFechamentoPortaoFiwareAction;

@Configuration
public class StateMachineConfig {
	

	@Value("${spring.redis.host}")
	private String host;
	

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
			.action(notificarAberturaChaveiroFiwareAction())
			.and()
		.withExternal()
			.source(Situacao.EM_TRANSITO_INTERNO).target(Situacao.EM_TRANSITO_EXTERNO)
			.event(Evento.INTERAGIR_PORTAO)	
			.action(notificarAberturaPortaoFiwareAction())
			.and()
//		.withExternal()
//			.source(Situacao.AGUARDANDO_SAIR).target(Situacao.EM_TRANSITO_EXTERNO)
//			.event(Evento.INTERAGIR_SENSOR_PORTAO)
//			.and()
		.withInternal()
			.source(Situacao.EM_TRANSITO_EXTERNO)
			.action(notificarFechamentoPortaoFiwareAction())
			.timerOnce(4_000)
			.and()
		.withExternal()
			.source(Situacao.EM_TRANSITO_EXTERNO).target(Situacao.EM_TRANSITO_INTERNO)
			.event(Evento.INTERAGIR_PORTAO)
			.action(notificarAberturaPortaoFiwareAction())
			.and()
//		.withExternal()
//			.source(Situacao.AGUARDANDO_ENTRAR).target(Situacao.EM_TRANSITO_INTERNO)
//			.event(Evento.INTERAGIR_SENSOR_PORTAO)
//			.and()
		.withInternal()
			.source(Situacao.EM_TRANSITO_INTERNO)
			.action(notificarFechamentoPortaoFiwareAction())
			.timerOnce(4_000)
			.and()
		.withExternal()
			.source(Situacao.EM_TRANSITO_INTERNO).target(Situacao.DISPONIVEL)
			.event(Evento.INTERAGIR_CHAVEIRO)
			.action(notificarFechamentoChaveiroFiwareAction());
		
		return builder.build();
	}

	@Bean
	public Action<Situacao, Evento> notificarAberturaPortaoFiwareAction() {
		return new NotificarAberturaPortaoFiwareAction();
	}
	
	@Bean
	public Action<Situacao, Evento> notificarFechamentoPortaoFiwareAction() {
		return new NotificarFechamentoPortaoFiwareAction();
	}

	@Bean
	public NotificarAberturaChaveiroFiwareAction notificarAberturaChaveiroFiwareAction() {
		return new NotificarAberturaChaveiroFiwareAction();
	}
	
	@Bean
	public NotificarFechamentoChaveiroFiwareAction notificarFechamentoChaveiroFiwareAction() {
		return new NotificarFechamentoChaveiroFiwareAction();
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName(host); // TODO Refatorar?
	    return jedisConFactory;
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