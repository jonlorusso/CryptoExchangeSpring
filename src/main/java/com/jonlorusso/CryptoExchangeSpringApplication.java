package com.jonlorusso;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.jonlorusso.contributor.Port;
import com.jonlorusso.entity.Message;

@SpringBootApplication
public class CryptoExchangeSpringApplication {

	@Bean
	public Port port1() {
		return new Port("P1");
	}

	@Bean
	public Port port2() {
		return new Port("P2");
	}

	@Bean
	public Port port3() {
		return new Port("P3");
	}

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeSpringApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
		return args -> {
			Map<String, Port> ports = applicationContext.getBeansOfType(Port.class);

			Port port1 = ports.get("port1");
			Port port2 = ports.get("port2");
			Port port3 = ports.get("port3");

			// MatchingEngine matchingEngine =
			// (MatchingEngine)applicationContext.getBean("ME");

			port1.publishMessage(port1.id, "SEL 8 30");
			port2.publishMessage(port2.id, "SEL 6 60");
			port1.publishMessage(port1.id, "SEL 7 5");
			port2.publishMessage(port2.id, "SEL 5 40");
			port3.publishMessage(port3.id, "SEL 9 66");

			Message buyMessage = port1.publishMessage(port1.id, "BUY 5 10");
			port2.publishMessage(port2.id, "BUY 2 4");
			port3.publishMessage(port3.id, "BUY 3 100");
			port3.publishMessage(port3.id, "BUY 4 22");

			port1.publishMessage(port1.id, "CA " + buyMessage.getSequenceNumber());
		};
	}
}
