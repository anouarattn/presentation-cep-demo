package business;

import java.util.Properties;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import pojo.Transaction;
import serialization.EventDeserializationSchema;

public class I {
	public static String KAFKA_BROKERS = "kafka1:9092";
	public static String CLIENT_ID = "client1";
	public static String GROUP_ID_CONFIG = "be_group";
	public static String TOPIC_NAME = "T.CustomerTransaction";
	
	
	public static final String OPERATIONTYPE = "OperationType";
	public static final String ACCOUNTNUMBER = "AccountNumber";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static final String VALUE = "Value";
	public static final String ISPROXY = "IsProxy";
	
	public static FlinkKafkaConsumer010<Transaction> getConsumer() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, I.KAFKA_BROKERS);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, I.CLIENT_ID);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
		
		return new FlinkKafkaConsumer010<>(TOPIC_NAME,new EventDeserializationSchema(),props);
	}
}
