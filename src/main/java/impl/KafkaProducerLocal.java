package impl;

import org.apache.log4j.Logger;

public class KafkaProducerLocal {
	/**
	 * Logger Instance
	 */
	private static final Logger LOGGER = Logger.getLogger(KafkaProducerLocal.class);

	private KafkaProducerThread producer;

	public KafkaProducerLocal(int partition, String topicName, String brokerURL) {
		this.producer = new KafkaProducerThread(partition, topicName, brokerURL);
		LOGGER.debug("creating new publisher thread");
	}

	public void execute() {
		Thread t = new Thread(producer);
		t.start();
	}

	public void closeProducer() {
		producer.closePublisher();
	}

	public void publishMessage(int partition, String key, String message) {
		producer.publishMessage(partition, key, message);
	}

	public boolean getStatus() {
		return producer.getStatus();
	}
}
