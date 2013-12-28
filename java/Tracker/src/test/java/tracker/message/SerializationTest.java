package tracker.message;

import org.testng.annotations.Test;

import tracker.common.Debugger;
import tracker.common.ID;
import tracker.common.State;

public class SerializationTest {
	@Test
	public void serializeOpen() {
		Debugger.print(new OpenMessage(new State(0, 0).toString()).serialize());
	}

	@Test
	public void serializeClose() {
		Debugger.print(new CloseMessage("abc").serialize());
	}

	@Test
	public void serializeUpdateOne() {
		Debugger.print(new UpdateOneMessage(1, "abc", new State(0, 0).toString()).serialize());
		Debugger.print(new UpdateOneMessage(1, ID.random().toString(), new State(0, 0).toString()).serialize());
	}
}
