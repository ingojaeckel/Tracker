package tracker.common;

public class Debugger {
	public static boolean ENABLED = false;

	public static String bytesToString(final byte[] bytes) {
		final StringBuilder buf = new StringBuilder();
		buf.append("{\n");

		for (int i = 0; i < bytes.length; i++) {
			buf.append(String.format("%4d", bytes[i]));
			if (i < bytes.length - 1) {
				buf.append(",");
			}
			if ((i + 1) % 16 == 0) {
				buf.append("\n");
			} else if ((i + 1) % 8 == 0) {
				buf.append("  ");
			}
		}

		buf.append("}; ");
		buf.append(bytes.length);
		return buf.toString().trim();
	}

	public static void print(final String message) {
		if (ENABLED) {
			System.out.println(message);
		}
	}

	public static void print(final byte[] bytes) {
		print(bytesToString(bytes));
	}
}
