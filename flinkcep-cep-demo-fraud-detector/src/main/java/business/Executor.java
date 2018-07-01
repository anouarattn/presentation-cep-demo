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
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(); // creation d'un envirenement d'execution suivant le context 
		
		DataStream<Transaction> stream = env
			.addSource(I.getConsumer());
		
		Pattern<Transaction, ?>  pattern = Pattern.<Transaction>begin("first transaction").where( 
				new SimpleCondition<Transaction>() {
		            @Override
		            public boolean filter(Transaction tr) {
		                return !tr.isProxy();
		            }
				}
			);
		
		PatternStream<Transaction>  patternStream = CEP.pattern(stream, pattern);

		DataStream<String> frauds = patternStream.select(new PatternSelectFunction<Transaction, String>() {

			@Override
			public String select(Map<String, List<Transaction>> pattern) throws Exception {
				Transaction firstTransaction = pattern.get("first transaction").get(0);
				//Transaction secondTransaction = pattern.get("second transaction").get(0);
				
				return " fraud detected for "+firstTransaction.getAccountNumber();
			}
		});
		
		frauds.print();
		
		env.execute("fraud detector cep");
	}
	
	


}
