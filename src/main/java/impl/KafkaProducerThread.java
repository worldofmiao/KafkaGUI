package impl;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

public class KafkaProducerThread implements Runnable {
	/**
	 * Logger Instance
	 */
	private static final Logger LOGGER = Logger.getLogger(KafkaProducerThread.class);
	private String topic;
	private KafkaProducer<String, String> producer;
	private String key;
	// publishing states
	private int partition;
	private boolean state = true;

	public KafkaProducerThread(int partition, String topicName, String kafkaBrokerURL) {
		LOGGER.debug("launching a Kafka producer for topic: " + topicName);
		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaBrokerURL);
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		this.producer = new KafkaProducer<String, String>(props);
		this.topic = topicName;
		this.key = null;
		this.partition = partition;
	}

	@Override
	public void run() {
		boolean checkState = true;
		while (checkState != false) {
			checkState = getStatus();
			if (Thread.interrupted()) {
				LOGGER.debug("Publisher closing..");
				return;
			}
		}
	}

	public void publishMessage(int partition, String key, String message) {
		producer.send(new ProducerRecord<String, String>(this.topic, this.partition, this.key, message));
	}

	public void closePublisher() {
		this.state = false;
	}

	public boolean getStatus() {
		return state;
	}

}
