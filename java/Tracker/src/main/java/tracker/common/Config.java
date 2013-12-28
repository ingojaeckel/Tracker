package tracker.common;

public class Config {
	public static final int SERVER_PORT = 6789;
	/**
	 * Number of connections after which server is automatically shutdown.
	 */
	public static final int SERVER_MAX_CONNECTIONS = 100;
	/**
	 * Time after which the server is automatically shutdown.
	 */
	public static final long SERVER_MAX_RUNTIME_MS = 60 * 60 * 1000;
	/**
	 * <ol>
	 * <li>1 thread to wait on new connections</li>
	 * <li>4 threads to process requests from incoming connections</li>
	 * </ol>
	 */
	public static final int SERVER_THREADS = 1 + 4;
}
