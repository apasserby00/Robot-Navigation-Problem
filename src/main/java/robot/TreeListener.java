package main.java.robot;

import main.java.grid.Cell;

public interface TreeListener {
	void addEdge(Cell x, Cell y);
	void reset();
}
