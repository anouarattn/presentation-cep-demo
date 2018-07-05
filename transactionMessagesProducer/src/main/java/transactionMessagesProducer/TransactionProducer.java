package transactionMessagesProducer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class TransactionProducer {

	Boolean pauser = true;
	
	static void getDataAndSend() throws Exception{	
		Stream<String> stream = Files.lines(Paths.get("src/main/resources/data.csv"));
		Producer<String, byte[]> producer = I.createProducer();

		stream.forEach(line -> {
			String [] columns = line.split(",");
			try {
				
			ProducerRecord<String, byte[]> record = new ProducerRecord<>(I.TOPIC_NAME,
					I.getMapArray(columns[0], columns[1], Double.parseDouble(columns[2]), 
							Double.parseDouble(columns[3]),Double.parseDouble(columns[4]),  Boolean.parseBoolean(columns[5])));
			
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent  to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
				Thread.sleep(500);

			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (Exception e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		});
		stream.close();
	}
	public static void main(String[] args) throws Exception{	
		getDataAndSend();
	}
	
}
