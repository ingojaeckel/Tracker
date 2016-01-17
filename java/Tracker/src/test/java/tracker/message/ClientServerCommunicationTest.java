package tracker.message;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import tracker.client.Client;
import tracker.common.Debugger;
import tracker.server.Server;

public class ClientServerCommunicationTest {
	private Server server;
	private Client client;

	@BeforeMethod(enabled = false)
	public void setup() throws Exception {
		Debugger.ENABLED = true;
		this.server = new Server(6789);
		this.client = new Client("localhost", 6789);
	}

	@Test(enabled = false)
	public void sendOneMessage() throws Exception {
		server.listen();
		client.send(new UpdateMessage("[382,382]"), true);
		Thread.sleep(1 * 1000);
		server.stop();
	}
}
