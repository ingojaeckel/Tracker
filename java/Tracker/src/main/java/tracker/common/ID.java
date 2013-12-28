package tracker.common;

import java.util.Random;

public class ID {

	private final String id;
	private static final int LEN = 7;
	private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890!@#$%^&*()-=_+{}[]|:;'\"<>,.?/";
	private static final Random R = new Random(System.currentTimeMillis());

	public ID(final String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

	public static ID random() {
		final StringBuffer buf = new StringBuffer(LEN);

		for (int i = 0; i < LEN; i++) {
			final int randomPosition = R.nextInt(CHARS.length());
			buf.append(CHARS.charAt(randomPosition));
		}

		return new ID(buf.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ID other = (ID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
