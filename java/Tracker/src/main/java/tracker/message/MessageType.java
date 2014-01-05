package tracker.message;

public enum MessageType {
	Open((byte) 0), Close((byte) 1), Update((byte) 2), Get((byte) 3);

	private final byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public static MessageType from(byte tag) {
		return from((char) tag);
	}

	public static MessageType from(char tag) {
		switch (tag) {
		case 0:
			return Open;
		case 1:
			return Close;
		case 2:
			return Update;
		case 3:
			return Get;
		default:
			throw new IllegalArgumentException("Unknown MessageType " + tag);
		}
	}

	public final byte get() {
		return value;
	}
}
