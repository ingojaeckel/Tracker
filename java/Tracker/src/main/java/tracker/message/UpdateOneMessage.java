package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import tracker.common.Debugger;
import tracker.common.Parser;

public class UpdateOneMessage implements Message {
	private final String id;
	private final String state;
	private static final Parser parser = new Parser();

	public UpdateOneMessage(String id, String state) {
		this.id = id.trim();
		this.state = state.trim();
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
			// 8 bytes id length
			// + 8 bytes state length
			// + variable length: id, state
			dos.writeLong(2 * 8 + id.length() + state.length());

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

		final long lenId = parser.readLong(bytes, 0);
		if (lenId > 10) {
			throw new IllegalArgumentException(String.format("ID should not be longer than 10 but is %d. payloadLen=%d%n", lenId, payloadLen));
		}
		final String id = parser.readString(bytes, 0 + 8, lenId);
		final long lenState = parser.readLong(bytes, 0 + 8 + lenId);
		if (lenState > 50) {
			throw new IllegalArgumentException("something is up. state string should not be longer than 50 ");
		}
		final String state = parser.readString(bytes, 0 + 8 + lenId + 8, lenState);

		Debugger.print(String.format("lenId=%d, lenState=%d, id=%s, state=%s%n", lenId, lenState, id, state));

		return new UpdateOneMessage(id, state);
	}

	public static UpdateOneMessage parse(byte[] bytes) {
		final long payloadLen = parser.readLong(bytes, 3);
		final long lenId = parser.readLong(bytes, 3 + 1 * 8);
		final String id = parser.readString(bytes, 3 + 2 * 8, lenId);
		final long lenState = parser.readLong(bytes, 3 + 2 * 8 + lenId);
		final String state = parser.readString(bytes, 3 + 3 * 8 + lenId, lenState);

		return new UpdateOneMessage(id, state);
	}

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}
}
