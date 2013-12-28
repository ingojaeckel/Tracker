package tracker.client;

import tracker.common.Config;

class RunClient {
	public static void main(String argv[]) throws Exception {
		final String hostname;
		final int port;
		final int maxMessages;

		// parse arguments
		if (argv.length == 3) {
			hostname = argv[0];
			port = Integer.parseInt(argv[1]);
			maxMessages = Integer.parseInt(argv[2]);
		} else {
			hostname = "localhost";
			port = Config.SERVER_PORT;
			maxMessages = 10 * 1000 * 1000;
		}

		new Client(hostname, port).run(maxMessages);
	}
}
