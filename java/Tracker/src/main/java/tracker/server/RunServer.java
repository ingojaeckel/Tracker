package tracker.server;

import tracker.common.Config;

class RunServer {
	public static void main(String argv[]) throws Exception {
		final int port;

		// parse arguments
		if (argv.length == 1) {
			port = Integer.parseInt(argv[0]);
		} else {
			port = Config.SERVER_PORT;
		}

		final Server server = new Server(port);
		server.listen();

		Thread.sleep(Config.SERVER_MAX_RUNTIME_MS);
		server.stop();
	}
}
