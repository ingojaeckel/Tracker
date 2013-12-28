package tracker.message;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DeserializationTest {
	@Test
	public void deserializeUpdateMessage() {
		//            M  V  T  +--- Payload length --+ +-- Version ----------+ +-- ID Length --------+ +-- ID ---+ +--- state len --------+  +-- state --------------------------------------------------------+
		//            0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37  38  39   40  41   42   43  44  45   46  47  48  49  50   51  52  53  54
		byte[] b = { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 97, 98, 99, 0, 0, 0, 0, 0, 0, 0, 16, 83, 116, 97, 116, 101, 32, 91, 120, 61, 48, 44, 32, 121, 61, 48, 93 };

		UpdateOneMessage message = UpdateOneMessage.parse(b);
		Assert.assertEquals(message.getId(), "abc");
		Assert.assertEquals(message.getVersion(), 1l);
		Assert.assertEquals(message.getState(), "State [x=0, y=0]");
	}
}
