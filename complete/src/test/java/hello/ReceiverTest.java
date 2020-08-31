package hello;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ReceiverTest {


    @ClassRule
    public static EmbeddedActiveMQBroker embeddedBroker = new EmbeddedActiveMQBroker();

    @Autowired
    private Producer producer;

    @Autowired
    private Receiver receiver;

    private static final int MAX_TRIES = 5000;

    @Autowired
    JmsTemplate jmsTemplate;

    @Before
    public void setUp() {
        embeddedBroker.start();
    }

    @AfterAll
    public void treaDown() {
        embeddedBroker.stop();
    }

    @Test
    public void sendAndReceiveTest() throws InterruptedException {
        producer.send();
        CountDownLatch latch = new CountDownLatch(1);
        waitForAll("mailbox");
    }

    private boolean getMessageCount(String queueName) {
        Message mailbox = embeddedBroker.peekMessage("mailbox");
        return mailbox != null;

        //Integer integer = jmsTemplate.browseSelected(queueName, "true = true", (s, qb) -> Collections.list(qb.getEnumeration()).size());
        //return integer;
    }

    public void waitForAll(String queueName) {
        int i = 0;
        while (i <= MAX_TRIES) {
            if (getMessageCount(queueName)) {
                return;
            }
            i++;
        }
    }
}
