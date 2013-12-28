package tracker.common;

public class Versioned<T> {
	private final long version;
	private final T value;

	public Versioned(long version, T value) {
		this.version = version;
		this.value = value;
	}

	public long getVersion() {
		return version;
	}

	public T getValue() {
		return value;
	}
}
