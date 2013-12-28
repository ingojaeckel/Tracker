package tracker.message;

import org.testng.annotations.BeforeMethod;

import tracker.server.Server;

public class BandwidthTest {
	private Server server;

	@BeforeMethod
	public void setup() throws Exception {
		this.server = new Server(6789);
	}
}
