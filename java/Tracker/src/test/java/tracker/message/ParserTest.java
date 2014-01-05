package tracker.message;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import tracker.common.Parser;
import tracker.common.State;

public class ParserTest {
	private Parser parser;

	@BeforeMethod
	public void setup() {
		this.parser = new Parser();
	}

	@DataProvider(name = "sampleLongValues")
	public Object[][] sampleLongValues() {
		return new Object[][] {
				// without offset
				new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, 0, 0l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 }, 0, 1l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 2 }, 0, 2l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 3 }, 0, 3l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 4 }, 0, 4l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 5 }, 0, 5l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 6 }, 0, 6l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 7 }, 0, 7l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 8 }, 0, 8l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 9 }, 0, 9l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 10 }, 0, 10l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 1, 0 }, 0, 256l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 1, 1 }, 0, 257l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 1, 2 }, 0, 258l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 1, 0, 0 }, 0, 65536l },

				// With offset
				new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 1, 0l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 2, 1l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 }, 3, 2l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3 }, 4, 3l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 }, 5, 4l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 }, 6, 5l },

				// With offset + suffix
				new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3 }, 1, 0l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3 }, 2, 1l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 3 }, 3, 2l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 3 }, 4, 3l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 2, 3 }, 5, 4l }, new Object[] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 2, 3 }, 6, 5l }, };
	}

	@Test(dataProvider = "sampleLongValues")
	public void parseSampleValues(byte[] bytes, long indexStart, long expected) {
		Assert.assertEquals(parser.readLong(bytes, indexStart), expected);
	}

	@Test
	public void serializeIndividualParts() {
		// M V T +--- Payload length --+ +-- Version ----------+ +-- ID Length
		// --------+ +-- ID ---+ +--- state len --------+ +-- state
		// --------------------------------------------------------+
		// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25
		// 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48
		// 49 50 51 52 53 54
		// byte[] b = { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0,
		// 1, 0, 0, 0, 0, 0, 0, 0, 3, 97, 98, 99, 0, 0, 0, 0, 0, 0, 0, 16, 83,
		// 116, 97, 116, 101, 32, 91, 120, 61, 48, 44, 32, 121, 61, 48, 93 };

		Assert.assertEquals(parser.readLong(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31 }, 3), 31l);
		Assert.assertEquals(parser.readLong(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1 }, 3 + 8), 1l);
		Assert.assertEquals(parser.readLong(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3 }, 3 + 8 + 8), 3l);
		Assert.assertEquals(parser.readString(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 97, 98, 99 }, 3 + 8 + 8 + 8, 3), "abc");
		Assert.assertEquals(parser.readLong(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 97, 98, 99, 0, 0, 0, 0, 0, 0, 0, 16 }, 3 + 8 + 8 + 8 + 3), 16l);
		Assert.assertEquals(parser.readString(new byte[] { 42, 1, 2, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 97, 98, 99, 0, 0, 0, 0, 0, 0, 0, 16, 83, 116, 97, 116, 101, 32, 91, 120, 61, 48, 44, 32, 121, 61, 48, 93 }, 3 + 8 + 8 + 8 + 3 + 8, 16), "State [x=0, y=0]");
	}

	@Test
	public void serializeDeserialize() {
		for (int i = 0; i < 5 * 1000; i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			final String state = new State(i, i).toString();
			final UpdateMessage message = new UpdateMessage(state);
			final byte[] serialized = message.serialize();

			UpdateMessage deserialized = parser.read(serialized, UpdateMessage.class);
			Assert.assertEquals(deserialized.getState(), state, "Failed in iteration " + i);
		}
	}
}
