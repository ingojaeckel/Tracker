package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import tracker.common.Parser;

public class OpenMessage implements Message {
	private final String state;
	private static final Parser parser = new Parser();

	public OpenMessage(String state) {
		this.state = state;
	}

	@Override
	public byte[] serialize() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(MAGIC);
			dos.write(1);
			dos.write(MessageType.Open.get());
			dos.writeLong(state.length());
			dos.writeBytes(state);

			return baos.toByteArray();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static OpenMessage parse(BufferedReader in) {
		try {
			final long len = parser.readLong(in);
			final byte[] bytes = parser.readBytes(in, (int) len);
			final String state = parser.readString(bytes, 0, len);

			return new OpenMessage(state);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getState() {
		return state;
	}
}
