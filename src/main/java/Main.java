import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Connection connectionToBroker;
	private static Session session;
	private static Destination destination;
	private static MessageProducer producer;
	private static int firstMessagePackage = 10000;
	private static int seconMessagePackage = 10000;
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int messageCounter = 0;
		try {
			connectionToBroker = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
			connectionToBroker.start();
			session = connectionToBroker.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("zenek24pl.QUEUE");
			producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			long start = System.currentTimeMillis();
			while (messageCounter < firstMessagePackage) {
				message.setText("test_" + messageCounter);
				producer.send(message);
				messageCounter++;
			}
			long elapsedTimeMillisPersistent = System.currentTimeMillis() - start;
			log.info("10000 persistent messages sent in {" + elapsedTimeMillisPersistent + "} milliseconds.\n");
			message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			long secondStart = System.currentTimeMillis();
			messageCounter = 0;
			while (messageCounter < seconMessagePackage) {
				message.setText("test_" + messageCounter);
				producer.send(message);
				messageCounter++;
			}
			long elapsedTimeMillisNonPersistent = System.currentTimeMillis() - secondStart;
			log.info("10000 persistent messages sent in {" + elapsedTimeMillisNonPersistent + "} milliseconds.\n");
			 producer.close();
	         session.close();
	         connectionToBroker.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
