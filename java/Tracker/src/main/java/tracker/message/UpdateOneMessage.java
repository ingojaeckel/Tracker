package tracker.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import tracker.common.Debugger;
import tracker.common.Parser;

public class UpdateOneMessage implements Message {
	private final String id;
	private final String state;
	private final long version;
	private static final Parser parser = new Parser();

	public UpdateOneMessage(long version, String id, String state) {
		this.id = id;
		this.state = state;
		this.version = version;
	}

	@Override
	public byte[] serialize() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(MAGIC);
			dos.write(1);
			dos.write(MessageType.UpdateOne.get());

			// 4 bytes version
			// 4 bytes id length
			// 4 bytes state length
			// variable length: id, state
			dos.writeLong(3 * 8 + id.trim().length() + state.toString().trim().length()); // payload
																							// length
			dos.writeLong(version);

			dos.writeLong(id.trim().length());
			dos.writeBytes(id.trim());

			dos.writeLong(state.trim().toString().length());
			dos.writeBytes(state.trim().toString());

			// System.out.println("sent " + id.trim().length() +
			// " as the ID length");

			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static UpdateOneMessage parse(InputStreamReader in) {
		final long payloadLen = parser.readLong(in);

		if (payloadLen > 1000) {
			System.err.println("something is up, payload shouldn't be longer than 1000 bytes");
		}

		final byte[] bytes = parser.readBytes(in, (int) payloadLen);
		Debugger.print(bytes);

		final long version = parser.readLong(bytes, 0);
		final long lenId = parser.readLong(bytes, 0 + 8);
		if (lenId > 10) {
			System.err.println("something is up, the ID should not be longer than 10");
		}
		final String id = parser.readString(bytes, 0 + 8 + 8, lenId);
		final long lenState = parser.readLong(bytes, 0 + 8 + 8 + lenId);
		if (lenState > 50) {
			System.err.println("something is up. state string should not be longer than 50 ");
		}
		final String state = parser.readString(bytes, 0 + 8 + 8 + lenId + 8, lenState);

		return new UpdateOneMessage(version, id, state);
	}

	public static UpdateOneMessage parse(byte[] bytes) {
		final long payloadLen = parser.readLong(bytes, 3);
		final long version = parser.readLong(bytes, 3 + 1 * 8);
		final long lenId = parser.readLong(bytes, 3 + 2 * 8);
		final String id = parser.readString(bytes, 3 + 3 * 8, lenId);
		final long lenState = parser.readLong(bytes, 3 + 3 * 8 + lenId);
		final String state = parser.readString(bytes, 3 + 4 * 8 + lenId, lenState);

		return new UpdateOneMessage(version, id, state);
	}

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public long getVersion() {
		return version;
	}
}
