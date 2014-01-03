package tracker.client;

import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import tracker.common.Debugger;
import tracker.common.State;
import tracker.message.CloseMessage;
import tracker.message.Message;
import tracker.message.OpenMessage;
import tracker.message.UpdateOneMessage;

public class Client {
	private final Socket socket;
	private final BufferedOutputStream out;
	private final InputStreamReader in;

	public Client(final String hostname, final int port) throws Exception {
		System.out.format("connecting to %s:%d.. ", hostname, port);
		this.socket = new Socket(hostname, port);
		this.out = new BufferedOutputStream(socket.getOutputStream());
		this.in = new InputStreamReader(socket.getInputStream());
		System.out.format("done. [%s]%n", socket);
	}

	public void run(final int maxMessageCount) throws Exception {
		int bytes = 0;

		System.out.println("sending open request..");
		bytes += send(new OpenMessage(new State(0, 0).toString()), true);
		final char[] openMessageResponse = new char[16];
		in.read(openMessageResponse);
		final String id = new String(openMessageResponse);
		System.out.println("Received ID: " + id);

		final long before = System.currentTimeMillis();

		for (int i = 0; i < maxMessageCount; i++) {
			bytes += send(new UpdateOneMessage(id, new State(i, i).toString()), false);
		}

		char[] closeMessageResponse = new char[1];
		bytes += send(new CloseMessage(id), true);
		in.read(closeMessageResponse);
		if (closeMessageResponse[0] != 1) {
			throw new IllegalArgumentException("unexpected response on close " + closeMessageResponse[0]);
		}

		final long diff = System.currentTimeMillis() - before;
		final double timePerMessage = (double) diff / (double) maxMessageCount;
		final double messagesPerSecond = 1000.0 / timePerMessage;
		final int kiloBytes = bytes / 1024;
		final double kiloBytePerSecond = (double) kiloBytes / ((double) diff / 1000.0);
		final double bytesPerMessage = (double) bytes / maxMessageCount;

		System.out.println();
		System.out.format("%d messages in %d ms%n", maxMessageCount, diff);
		System.out.format("%.1f ms per message%n", timePerMessage);
		System.out.format("%.1f messages per second%n", messagesPerSecond);
		System.out.format("%d KB, %.1f KB per second%n", kiloBytes, kiloBytePerSecond);
		System.out.format("%.1f bytes per message%n", bytesPerMessage);

		socket.close();
	}

	public int send(final Message message, final boolean flush) throws Exception {
		final byte[] bytes = message.serialize();
		Debugger.print(bytes);
		out.write(bytes);
		if (flush) {
			// Only flush if the caller deems it necessary.
			out.flush();
		}
		return bytes.length;
	}
}
