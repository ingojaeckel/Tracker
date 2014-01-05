package tracker.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import tracker.common.Debugger;
import tracker.common.Parser;
import tracker.common.State;

public class SerializationTest {
	@BeforeMethod
	public void setup() {
		Debugger.ENABLED = true;
	}
	
	@Test
	public void serializeOpen() {
		Debugger.print(new OpenMessage(new State(0, 0).toString()).serialize());
	}

	@Test
	public void serializeClose() {
		Debugger.print(new CloseMessage().serialize());
	}

	@Test
	public void serializeUpdateOne() {
		Debugger.print(new UpdateMessage(new State(0, 0).toString()).serialize());
	}
	
	@Test
	public void serializeMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "b");
		map.put("c", "d");
		map.put("e", "f");
		
		byte[] bytes = map.toString().getBytes();
		System.out.println(map.toString());
		Debugger.print(bytes);
		Assert.assertEquals(bytes, new byte[]{ 123, 101,  61, 102,  44,  32,  99,  61,   100,  44,  32,  97,  61,  98, 125});
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
