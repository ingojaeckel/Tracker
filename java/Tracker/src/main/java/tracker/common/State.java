package tracker.common;

public class State {
	private int x;
	private int y;

	public State(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}
