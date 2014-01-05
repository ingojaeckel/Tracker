package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CloseMessage implements Message {
	@Override
	public byte[] serialize() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(MAGIC);
			dos.write(1);
			dos.write(MessageType.Close.get());

			return baos.toByteArray();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static CloseMessage parse(BufferedReader in) {
		return new CloseMessage();
	}
}
