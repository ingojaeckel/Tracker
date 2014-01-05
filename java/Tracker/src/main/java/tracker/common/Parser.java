package tracker.common;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

import tracker.message.Message;
import tracker.message.MessageType;
import tracker.message.UpdateMessage;

public class Parser {
	/**
	 * Read numBytes bytes from in.
	 * 
	 * @return The numBytes bytes that were read from in.
	 */
	public byte[] readBytes(BufferedReader in, int numBytes) {
		try {
			final char[] all = new char[numBytes];
			final int read = in.read(all);

			if (read == -1) {
				throw new RuntimeException("Unexpected number of bytes read: " + read);
			}

			byte[] result = new byte[read];
			for (int i = 0; i < read; i++) {
				result[i] = (byte) all[i];
			}

			return result;
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public String readString(byte[] bytes, long indexStart, long length) {
		if (length > 1000) {
			throw new IllegalArgumentException("something is up. length should not be bigger than 1000");
		}

		final byte[] buf = new byte[(int) length];

		// Put all bytes representing the long value into the ByteBuffer
		for (int i = 0; i < length; i++) {
			try {
				buf[i] = bytes[(int) indexStart + i];
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				throw new RuntimeException("parsing error", e);
			}
		}

		return new String(buf);
	}

	public long readLong(byte[] bytes, long indexStart) {
		final ByteBuffer bb = ByteBuffer.allocate(8);

		// Put all 8 bytes representing the long value into the ByteBuffer
		for (int i = 0; i < 8; i++) {
			try {
				bb.put(bytes[(int) indexStart + i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				throw new RuntimeException("parsing error", e);
			}
		}

		bb.flip();
		return bb.getLong();
	}

	public String readString(BufferedReader in, final long len) {
		try {
			final char[] buf = new char[(int) len];
			int read = -1;
			if ((read = in.read(buf)) != len) {
				throw new IllegalArgumentException(String.format("invalid message format. read %d expected %d", read, len));
			}

			return new String(buf);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public long readLong(BufferedReader in) {
		try {
			final char[] buf = new char[8];

			if (in.read(buf) != buf.length) {
				throw new IllegalArgumentException("invalid message format");
			}

			final ByteBuffer bb = ByteBuffer.allocate(8);
			for (int i = 0; i < 8; i++) {
				bb.put((byte) buf[i]);
			}
			bb.flip();

			return bb.getLong();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public <T> T read(byte[] bytes, Class<T> clazz) {
		if (bytes.length < 3) {
			throw new IllegalArgumentException("Not enough bytes to read header");
		}
		if (bytes[0] != Message.MAGIC) {
			throw new RuntimeException("Invalid magic number " + bytes[0]);
		}
		if (bytes[1] != 1) {
			throw new RuntimeException("Invalid version " + bytes[1]);
		}

		switch (MessageType.from(bytes[2])) {
		case Update:
			return (T) UpdateMessage.parse(bytes);
			/*
			 * case Open: return OpenMessage.parse(payload); case Close: return
			 * CloseMessage.parse(payload);
			 */
		default:
			throw new IllegalArgumentException("Unsupported message type " + bytes[2]);
		}
	}
}
