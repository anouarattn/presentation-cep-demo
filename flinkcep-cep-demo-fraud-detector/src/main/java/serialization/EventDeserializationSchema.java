package serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

import business.I;
import pojo.Transaction;



/* 
 * Deserializer des bytes messages envoiyes depuis la topic kafka vers le pojo Transaction
 * 
 * */
public class EventDeserializationSchema implements DeserializationSchema<Transaction> {
	private static final long serialVersionUID = 1L;

	@Override
	public TypeInformation<Transaction> getProducedType() {
		return TypeExtractor.getForClass(Transaction.class);
	}

	@Override
	public Transaction deserialize(byte[] message) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Map<String, Object> m;
		Transaction tr = new Transaction();
		try {
			m = (Map<String, Object>) ois.readObject();
			tr.setAccountNumber((String) m.get(I.ACCOUNTNUMBER));
			tr.setOperationType((String) m.get(I.OPERATIONTYPE));
			tr.setLatitude((double) m.get(I.LATITUDE));
			tr.setLongitude((double) m.get(I.LONGITUDE));
			tr.setValue((double) m.get(I.VALUE));
			tr.setProxy((boolean) m.get(I.ISPROXY));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tr;
	}

	@Override
	public boolean isEndOfStream(Transaction nextElement) {
		// TODO Auto-generated method stub
		return false;
	}

}
