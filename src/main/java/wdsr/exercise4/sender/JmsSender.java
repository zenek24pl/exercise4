package wdsr.exercise4.sender;

import java.math.BigDecimal;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.Order;

public class JmsSender {
	private static final Logger log = LoggerFactory.getLogger(JmsSender.class);
	
	private final String queueName;
	private final String topicName;
	private Order order;

	public JmsSender(final String queueName, final String topicName) {
		this.queueName = queueName;
		this.topicName = topicName;
	}

	/**
	 * This method creates an Order message with the given parameters and sends it as an ObjectMessage to the queue.
	 * @param orderId ID of the product
	 * @param product Name of the product
	 * @param price Price of the product
	 */
	public void sendOrderToQueue(final int orderId, final String product, final BigDecimal price)throws JMSException {
		// localhost:61616
				order=new Order(orderId,product,price);
				Connection connectionToBroker = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
				connectionToBroker.start();
				Session session = connectionToBroker.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createQueue("trades");
				MessageProducer producer = session.createProducer(destination);
				ObjectMessage mg=session.createObjectMessage();
			
				mg.setObject(order);
				producer.send(mg);
				session.close();
				connectionToBroker.close();
	
		
		
		// TODO
	}

	/**
	 * This method sends the given String to the queue as a TextMessage.
	 * @param text String to be sent
	 */
	public void sendTextToQueue(String text)throws JMSException {
		// TODO
		ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connectionToBroker = connectionFactory.createConnection();
		connectionToBroker.start();
		Session session = connectionToBroker.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("trades");
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage();
		message.setText(text);
			producer.send(message);
		session.close();
		connectionToBroker.close();
		
	}

	/**
	 * Sends key-value pairs from the given map to the topic as a MapMessage.
	 * @param map Map of key-value pairs to be sent.
	 */
	public void sendMapToTopic(Map<String, String> map)throws JMSException {
		// TODO
	Connection connectionToBroker = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
		connectionToBroker.start();
		Session session = connectionToBroker.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("trades");
		MessageProducer producer = session.createProducer(destination);
		MapMessage message = session.createMapMessage();
		  for (Map.Entry<String, ?> entry : map.entrySet()) message.setObject(entry.getKey(), entry.getValue());
		   
			   producer.send(message);
			   session.close();
			   connectionToBroker.close();
		
	}
}
