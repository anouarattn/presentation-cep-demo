package business;

import java.util.List;
import java.util.Map;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import Helpers.DistanceCalculator;
import pojo.Transaction;


public class Executor {

	public static void main(String[] args) throws Exception {
		
		
		// creation d'un envirenement d'execution suivant le context 
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(); 
		
		
		
		// DataStream API, recuperation de la source du stream, dans notre cas topic Kafka
		DataStream<Transaction> stream = env
			.addSource(I.getConsumer());
		
		// Pattern API, définition du pattern a detecter
		Pattern<Transaction, ?>  pattern = Pattern.<Transaction>begin("first transaction").where( 
				new SimpleCondition<Transaction>() {
		            @Override
		            public boolean filter(Transaction tr) {
		                return !tr.isProxy();
		            }
				}
			).followedBy("second transaction").where( 
				new SimpleCondition<Transaction>() {
			            @Override
			        public boolean filter(Transaction tr) {
			            return !tr.isProxy();
			        }
				}
			).where(
				new IterativeCondition<Transaction>() {
					
					@Override
					public boolean filter(Transaction value, Context<Transaction> ctx) throws Exception {
						Transaction firsTransaction = ctx.getEventsForPattern("first transaction").iterator().next();
						return 
							value.getAccountNumber().equals(firsTransaction.getAccountNumber())
							&& 
							value.getOperationType().equals(firsTransaction.getOperationType())
							&& 
							(value.getOperationType().equals("PAYEMENT") || value.getOperationType().equals("WITHDRAWAL"))
							&& DistanceCalculator.calculate(firsTransaction.getLatitude(), 
															firsTransaction.getLongitude(), 
															value.getLatitude(), 
															value.getLongitude())>100;
							
					}
				}
			).within(Time.minutes(30));
		
		
		
		// PatternStream CEP Librairy, application du/des pattern(s) sur le streams
		PatternStream<Transaction>  patternStream = CEP.pattern(stream, pattern);

		
		// DataStream API, définition des actions après detection du pattern
		DataStream<String> frauds = patternStream.select(new PatternSelectFunction<Transaction, String>() {

			@Override
			public String select(Map<String, List<Transaction>> pattern) throws Exception {
				Transaction firstTransaction = pattern.get("first transaction").get(0);
				//Transaction secondTransaction = pattern.get("second transaction").get(0);
				
				return "\u001B[31m****** Fraude au "+pattern.get("first transaction").get(0).getOperationType() + " pr le client " + pattern.get("first transaction").get(0).getAccountNumber()+" en moins de 30 min deux operations a "+
				DistanceCalculator.calculate(pattern.get("first transaction").get(0).getLatitude(),
						pattern.get("first transaction").get(0).getLongitude(),
						pattern.get("second transaction").get(0).getLatitude(),
						pattern.get("second transaction").get(0).getLongitude()) + "km de distance \u001B[0m";
			}
		});
		
		frauds.print();
		
		
		// Execution du programme
		env.execute("fraud detector cep");
	}
	
	


}
