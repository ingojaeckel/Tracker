package tracker.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import tracker.common.Debugger;
import tracker.common.Parser;

public class UpdateMessage implements Message {
	private final String state;
	private static final Parser parser = new Parser();

	public UpdateMessage(String state) {
		this.state = state.trim();
	}

	@Override
	public byte[] serialize() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(MAGIC);
			dos.write(1);
			dos.write(MessageType.Update.get());

			dos.writeLong(state.length());
			dos.writeBytes(state);

			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static UpdateMessage parse(final BufferedReader in) {
		final long lenState = parser.readLong(in);
		if (lenState > 50) {
			throw new IllegalArgumentException("something is up. state string should not be longer than 50 ");
		}
		final String state = parser.readString(in, lenState);

		Debugger.print(String.format("lenState=%d, state=%s%n", lenState, state));

		return new UpdateMessage(state);
	}

	public static UpdateMessage parse(byte[] bytes) {
		final long lenState = parser.readLong(bytes, 3 + 0 * 8);
		final String state = parser.readString(bytes, 3 + 1 * 8, lenState);

		return new UpdateMessage(state);
	}

	public String getState() {
		return state;
	}
}
