package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send() {

        // Send a message with a POJO - the template reuse the message converter
        System.out.println("Sending an email message.");
        for(int i = 0; i < 2; i++) {
            jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello -" + i));
        }
    }

}
