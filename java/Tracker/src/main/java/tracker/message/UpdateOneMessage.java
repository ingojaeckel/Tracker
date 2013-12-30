package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import tracker.common.Debugger;
import tracker.common.Parser;

public class UpdateOneMessage implements Message {
	private final String id;
	private final String state;
	private final long version;
	private static final Parser parser = new Parser();

	public UpdateOneMessage(long version, String id, String state) {
		this.id = id.trim();
		this.state = state.trim();
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

			// payload length =
			// 8 bytes version
			// + 8 bytes id length
			// + 8 bytes state length
			// + variable length: id, state
			dos.writeLong(3 * 8 + id.length() + state.length());
			dos.writeLong(version);

			dos.writeLong(id.length());
			dos.writeBytes(id);

			dos.writeLong(state.length());
			dos.writeBytes(state);

			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static UpdateOneMessage parse(final BufferedReader in) {
		final long payloadLen = parser.readLong(in);
		if (payloadLen > 1000) {
			throw new IllegalArgumentException(String.format("Payload shouldn't be longer than 1000 bytes but is %d%n", payloadLen));
		}

		final byte[] bytes = parser.readBytes(in, (int) payloadLen);
		Debugger.print(bytes);

		final long version = parser.readLong(bytes, 0);
		final long lenId = parser.readLong(bytes, 0 + 8);
		if (lenId > 10) {
			throw new IllegalArgumentException(String.format("ID should not be longer than 10 but is %d. payloadLen=%d, version=%d%n", lenId, payloadLen, version));
		}
		final String id = parser.readString(bytes, 0 + 8 + 8, lenId);
		final long lenState = parser.readLong(bytes, 0 + 8 + 8 + lenId);
		if (lenState > 50) {
			throw new IllegalArgumentException("something is up. state string should not be longer than 50 ");
		}
		final String state = parser.readString(bytes, 0 + 8 + 8 + lenId + 8, lenState);

		Debugger.print(String.format("version=%d, lenId=%d, lenState=%d, id=%s, state=%s%n", version, lenId, lenState, id, state));

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
