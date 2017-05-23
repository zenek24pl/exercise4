package wdsr.exercise4.receiver;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.PriceAlert;
import wdsr.exercise4.VolumeAlert;
import wdsr.exercise4.sender.JmsSender;

/**
 * TODO Complete this class so that it consumes messages from the given queue
 * and invokes the registered callback when an alert is received.
 * 
 * Assume the ActiveMQ broker is running on tcp://localhost:62616
 */
public class JmsQueueReceiver implements MessageListener {
	private static final Logger log = LoggerFactory.getLogger(JmsQueueReceiver.class);
	private final String queueName;
	private Connection connectionToBroker;
	private Session session;
	private MessageConsumer consumer;
	private AlertService alertService;
	private ActiveMQConnectionFactory connectionFactory;
	private Destination destination;

	/**
	 * Creates this object
	 * 
	 * @param queueName
	 *            Name of the queue to consume messages from.
	 * @throws JMSException
	 */
	public JmsQueueReceiver(final String queueName) {
		this.queueName = queueName;

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:62616");
		connectionFactory.setTrustAllPackages(true);

		try {
			connectionToBroker = connectionFactory.createConnection();
			connectionToBroker.start();
			session = connectionToBroker.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Registers the provided callback. The callback will be invoked when a
	 * price or volume alert is consumed from the queue.
	 * 
	 * @param alertService
	 *            Callback to be registered.
	 * @throws JMSException
	 */
	public void registerCallback(AlertService alertService) throws JMSException {
		this.alertService = alertService;
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(this);
	}

	/**
	 * Deregisters all consumers and closes the connection to JMS broker.
	 * 
	 * @throws JMSException
	 */
	public void shutdown() throws JMSException {
		connectionToBroker.close();
		 session.close();
		 consumer.close();

	}

	@Override
	public void onMessage(Message message) {
		PriceAlert priceAlert;
		VolumeAlert volumeAlert;

		if (message instanceof TextMessage) {
			TextMessage text = (TextMessage) message;
			try {
				if (message.getJMSType().equals("PriceAlert")) {
					priceAlert = new PriceAlert(text.getText());
					alertService.processPriceAlert(priceAlert);
				} else if (message.getJMSType().equals("VolumeAlert")) {
					volumeAlert = new VolumeAlert(text.getText());
					alertService.processVolumeAlert(volumeAlert);
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (message instanceof ObjectMessage) {
			ObjectMessage object = (ObjectMessage) message;
			try {
				if (message.getJMSType().equals("PriceAlert")) {
					priceAlert = (PriceAlert) object.getObject();
					alertService.processPriceAlert(priceAlert);
				} else if (message.getJMSType().equals("VolumeAlert")) {
					volumeAlert = (VolumeAlert) object.getObject();
					alertService.processVolumeAlert(volumeAlert);
				}

			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	// TODO
	// This object should start consuming messages when registerCallback method
	// is invoked.

	// This object should consume two types of messages:
	// 1. Price alert - identified by header JMSType=PriceAlert - should invoke
	// AlertService::processPriceAlert
	// 2. Volume alert - identified by header JMSType=VolumeAlert - should
	// invoke AlertService::processVolumeAlert
	// Use different message listeners for and a JMS selector

	// Each alert can come as either an ObjectMessage (with payload being an
	// instance of PriceAlert or VolumeAlert class)
	// or as a TextMessage.
	// Text for PriceAlert looks as follows:
	// Timestamp=<long value>
	// Stock=<String value>
	// Price=<long value>
	// Text for VolumeAlert looks as follows:
	// Timestamp=<long value>
	// Stock=<String value>
	// Volume=<long value>

	// When shutdown() method is invoked on this object it should remove the
	// listeners and close open connection to the broker.
}
