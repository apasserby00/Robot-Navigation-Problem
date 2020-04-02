package main.java.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.data.Data;
import main.java.data.updatelisteners.DataUpdate;

public class Grid implements DataUpdate {

	private Data data;
	private HashMap<Cell, Cell.status> cells;
	private ArrayList<Cell> wallCells;
	private ArrayList<Cell> traversableCells;
	private ArrayList<Cell> goalCells;
	
	public ArrayList<Cell> getGoalCells() {
		return goalCells;
	}

	public Grid() {
		cells = new HashMap<>();
		wallCells = new ArrayList<>();
		traversableCells = new ArrayList<>();
		goalCells = new ArrayList<>();
	}

	private void initCells() {

		// Empty Cells
		for (int y = 0; y < data.getGridDimY(); y++) {
			for (int x = 0; x < data.getGridDimX(); x++) {
				Cell c = new Cell(x, y);
				cells.put(c, Cell.status.EMPTY);
				traversableCells.add(c);
			}
		}

		// Wall Cells
		for (int w = 0; w < data.getWalls().size(); w++) {

			List<Integer> wall = data.getWalls().get(w);
			int wallX = wall.get(0);
			int wallY = wall.get(1);
			int wallW = wall.get(2);
			int wallH = wall.get(3);

			for (int yy = 0; yy < wallH; yy++) {
				for (int xx = 0; xx < wallW; xx++) {
					Cell wallCoordinate = new Cell(xx + (wallX), yy + (wallY));
					wallCells.add(wallCoordinate);
					cells.replace(wallCoordinate, Cell.status.WALL);
					traversableCells.removeAll(wallCells);
				}
			}
		}

		// Petrol Cells
		for (int g = 0; g < data.getGoalStates().size(); g++) {
			cells.replace(new Cell(data.getGoalStates().get(g).get(0), data.getGoalStates().get(g).get(1)),
					Cell.status.PETROL);
			goalCells.add(new Cell(data.getGoalStates().get(g).get(0), data.getGoalStates().get(g).get(1)));
		}
	}

	// Returns the status of the cell (e.g. Empty, Wall, etc.)
	public Cell.status getCellStatus(Cell cell) {
		return cells.get(cell);
	}

	// Returns the number of walls in the grid
	private int numberOfWalls() {
		int numberOfWalls = 0;
		for (Cell.status t : cells.values()) {
			if (t == Cell.status.WALL) {
				numberOfWalls++;
			}
		}
		return numberOfWalls;
	}

	// Returns a list of the traversable vertices
	public List<Cell> getTraversableCells() {
		return traversableCells;
	}

	// Returns a list of wall cells
	public List<Cell> getWallCells() {
		return wallCells;
	}

	// Returns the number of cells/vertices v
	public int v() {
		return cells.size() - numberOfWalls();
	}

	// Returns the robot's initial cell
	public Cell robotsInitialCoordinate() {
		return new Cell(data.getInitialStateX(), data.getInitialStateY());
	}

	// returns the number of columns in the grid
	public int dimX() {
		return data.getGridDimX();
	}

	// returns the number of rows in the grid
	public int dimY() {
		return data.getGridDimY();
	}

	@Override
	public void updateData(Data d) {
		this.data = d;
		cells = new HashMap<>();
		wallCells = new ArrayList<>();
		traversableCells = new ArrayList<>();
		initCells();
	}
}
