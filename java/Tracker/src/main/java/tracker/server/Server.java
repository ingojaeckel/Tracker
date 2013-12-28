package tracker.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import tracker.common.Config;
import tracker.common.Versioned;

public class Server {
	private final AtomicBoolean running;
	private final ExecutorService executor;
	private final ServerSocket serverSocket;
	private final ConcurrentHashMap<String, Versioned<String>> map;

	public Server(final int port) throws Exception {
		System.out.print("opening socket.. ");
		this.serverSocket = new ServerSocket(port);
		System.out.format("done. [%s]%n", serverSocket);

		this.executor = Executors.newFixedThreadPool(Config.SERVER_THREADS);
		this.map = new ConcurrentHashMap<String, Versioned<String>>();
		this.running = new AtomicBoolean(false);
	}

	public void listen() {
		executor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				running.set(true);

				for (int i = 0; running.get() && i < Config.SERVER_MAX_CONNECTIONS; i++) {
					System.out.print("waiting on connection.. ");
					final Socket socket = serverSocket.accept();
					executor.submit(new Worker(socket, map));
					System.out.format("done. [%s]%n", socket);
				}

				return null;
			}
		});
	}

	public void stop() throws Exception {
		System.out.println("stopping.. ");
		// Tell the listener to stop waiting on new connections.
		running.set(false);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		serverSocket.close();
		executor.shutdownNow();
		System.out.println("done.");
	}

	public Map<String, Versioned<String>> getData() {
		// Return immutable map to avoid outside modifications.
		return Collections.unmodifiableMap(map);
	}
}
