package main.java.grid;

public class Cell {

	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;

	private int x;
	private int y;

	public enum status {
		EMPTY, WALL, PETROL
	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		return x >> 16 & y;
	}

	public boolean equals(Object o) {
		if (o instanceof Cell) {
			Cell c = (Cell) o;
			return c.x == x && c.y == y;
		}
		return false;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
