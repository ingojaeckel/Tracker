package tracker.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import tracker.common.Debugger;
import tracker.common.ID;
import tracker.common.Parser;
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
		Debugger.print(new UpdateOneMessage("abc", new State(0, 0).toString()).serialize());
		Debugger.print(new UpdateOneMessage(ID.random().toString(), new State(0, 0).toString()).serialize());
	}

	@Test
	public void serializeVersion() throws Exception {
		Parser parser = new Parser();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeLong(384);
		byte[] bytes = baos.toByteArray();

		Assert.assertTrue(Arrays.equals(bytes, new byte[] { 0, 0, 0, 0, 0, 0, 1, -128 }));
		Assert.assertEquals(parser.readLong(bytes, 0), 384l);
	}
}
