package tracker.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import tracker.common.Debugger;
import tracker.common.ID;
import tracker.message.CloseMessage;
import tracker.message.Message;
import tracker.message.MessageType;
import tracker.message.OpenMessage;
import tracker.message.UpdateOneMessage;

public class Worker implements Runnable {
	private static final byte[] OK = new byte[] { 1 };
	private final BufferedReader in;
	private final BufferedOutputStream out;
	private final ConcurrentHashMap<String, String> map;

	public Worker(final Socket socket, final ConcurrentHashMap<String, String> map) {
		try {
			System.out.println("connection " + socket.toString());
			this.map = map;
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new BufferedOutputStream(socket.getOutputStream());
		} catch (final Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void run() {
		try {
			boolean running = true;
			char[] header = new char[3];
			int read = -1;

			while (running && (read = in.read(header)) != -1) {
				validateHeader(header, read);

				switch (MessageType.from(header[2])) {
				case Open:
					final OpenMessage message1 = OpenMessage.parse(in);
					final String id = ID.random().toString();

					map.put(id, message1.getState());

					if (Debugger.ENABLED) {
						System.out.format("initialized id='%s' with state='%s' (version=%d)%n", id, message1.getState(), 0);
					}
					writeAndFlush(id.getBytes());
					break;
				case Close:
					final CloseMessage message = CloseMessage.parse(in);
					map.remove(message.getId());

					if (Debugger.ENABLED) {
						System.out.format("closed connection to '%s'%n", message.getId());
					}

					writeAndFlush(OK);
					running = false;
					break;
				case UpdateOne:
					final UpdateOneMessage m3 = UpdateOneMessage.parse(in);
					map.put(m3.getId(), m3.getState());

					if (Debugger.ENABLED) {
						System.out.format("updated id='%s' with state='%s'%n", m3.getId(), m3.getState());
					}

					// Don't send a response to the client.
					break;
				case UpdateAll:
					// Not implemented yet.
					System.out.println("UpdateAll");
					writeAndFlush(OK);
					break;
				default:
					throw new IllegalArgumentException("Invalid MessageType " + header[2]);
				}
			}
		} catch (final Exception exception) {
			exception.printStackTrace();
			throw new RuntimeException(exception);
		}
	}

	private void validateHeader(final char[] header, final int read) {
		if (read != header.length) {
			throw new IllegalArgumentException("Unable to parse header");
		}
		if (header[0] != Message.MAGIC) {
			throw new RuntimeException("Invalid magic number " + header[0]);
		}
		if (header[1] != 1) {
			throw new RuntimeException("Invalid version " + header[1]);
		}
	}

	private void writeAndFlush(final byte[] bytes) throws Exception {
		out.write(bytes);
		out.flush();
	}
}
