package main.java.tree;

import main.java.grid.Cell;

public class Vertex {

	private int x;
	private int y;

	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}
}
