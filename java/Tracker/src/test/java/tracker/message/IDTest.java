package tracker.message;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import tracker.common.ID;

public class IDTest {
	@Test
	public void verifyPseudoRandomness() {
		final int iterations = 100 * 1000;
		final Set<ID> ids = new HashSet<ID>(iterations);

		for (int i = 0; i < iterations; i++) {
			final ID id = ID.random();
			Assert.assertFalse(ids.contains(id), "Collision detected after " + (i + 1) + " iterations");
			ids.add(id);
		}
	}

	@Test
	public void verifyEquality() {
		ID id = ID.random();
		Assert.assertEquals(id, id);
	}

	@Test
	public void visualTest() {
		System.out.println(ID.random());
	}
}
