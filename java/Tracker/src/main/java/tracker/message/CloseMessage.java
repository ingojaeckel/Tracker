package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import tracker.common.Parser;

public class CloseMessage implements Message {
	private final String id;
	private static final Parser parser = new Parser();

	public CloseMessage(String id) {
		this.id = id.trim();
	}

	@Override
	public byte[] serialize() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(MAGIC);
			dos.write(1);
			dos.write(MessageType.Close.get());
			dos.writeLong(id.length());
			dos.writeBytes(id);

			return baos.toByteArray();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static CloseMessage parse(BufferedReader in) {
		final long len = parser.readLong(in);
		final String id = parser.readString(in, len);

		return new CloseMessage(id);
	}

	public String getId() {
		return id;
	}
}
