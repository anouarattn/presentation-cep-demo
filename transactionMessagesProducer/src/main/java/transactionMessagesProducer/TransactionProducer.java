package transactionMessagesProducer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class TransactionProducer {

	static void runProducer()  throws IOException {
		Producer<String, byte[]> producer = I.createProducer();
		for (int index = 0; index < I.MESSAGE_COUNT; index++) {
			
			
			ProducerRecord<String, byte[]> record = new ProducerRecord<>(I.TOPIC_NAME,
					I.getMap("dddd", 23754890, 234.33, 2342.32, 4443));
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception{	
		runProducer();
	}
	
}
