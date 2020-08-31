package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	@JmsListener(destination = "mailbox", containerFactory = "myFactory", concurrency = "1-5")
	public void receiveMessage(Email email, Session session, Message message) throws JMSException {
		LOGGER.info("Received {} on thread {}", email, Thread.currentThread().getName());
		LOGGER.info("Session Ack mode {}", session.getAcknowledgeMode());

		//message.acknowledge();

		//throwError(email);
		//long start = System.currentTimeMillis();
		invokeHttpCall(email).subscribe();
		//long end = System.currentTimeMillis();
		//LOGGER.info("time on subscribe execution: {}", (end - start));
	}


	private void throwError(Email email) {
		if (email.getBody().equals("Hello -1")) {
			throw new RuntimeException("Something went wrong");
		}
	}

	private Mono<String> invokeHttpCall(Email email) {
		return Mono.just(email)
				.flatMap(mail -> {
					LOGGER.info("executing flatMap {} on thread {}", email, Thread.currentThread().getName());
					String body = mail.getBody();
					return Mono.just(body.toUpperCase());
				});
	}

}
