package tracker.message;

public interface Message {
	byte[] serialize();

	int MAGIC = 0x42;
}
