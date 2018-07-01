package transactionMessagesProducer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class I {
	public static String KAFKA_BROKERS = "kafka1:9092";
	public static Integer MESSAGE_COUNT = 1;
	public static String CLIENT_ID = "client1";
	public static String TOPIC_NAME = "T.CustomerTransaction";
	public static String GROUP_ID_CONFIG = "be_group";
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
	public static String OFFSET_RESET_LATEST = "latest";
	public static String OFFSET_RESET_EARLIER = "earliest";
	public static Integer MAX_POLL_RECORDS = 1;
	
	public static final String OPERATIONTYPE = "OperationType";
	public static final String ACCOUNTNUMBER = "AccountNumber";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static final String VALUE = "Value";
	public static final String ISPROXY = "IsProxy";

	public static byte[]  getMap(String operationType, int accountNumber,double latitude,double  longitude, double value ) throws IOException
	{
		Map<String,Object> map = new HashMap<>();
		map.put(OPERATIONTYPE, operationType);
		map.put(ACCOUNTNUMBER, accountNumber);
		map.put(LATITUDE, latitude);
		map.put(LONGITUDE, longitude);
		map.put(VALUE, value);
		map.put(ISPROXY, false);
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(map);
		return fos.toByteArray();
	}
	

	public static Producer<String, byte[]> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, I.KAFKA_BROKERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, I.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());
		return new KafkaProducer<>(props);
	}
	

}
