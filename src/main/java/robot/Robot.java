package main.java.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import main.java.algorithm.Algorithm;
import main.java.grid.Cell;
import main.java.grid.Grid;
import main.java.gui.GUIListener;
import main.java.gui.GUIUpdater;
import main.java.gui.GridUpdate;
import main.java.gui.RobotMoveListener;

public class Robot implements GridUpdate, GUIListener {

	private Grid grid; // The robot's grid

	private Cell initCell; // Robot's starting cell
	private Cell cc; // Robot's cell

	private int r = 25; // Robot size
	private int lifeTime; // Time to live
	private int zzz = 1000; // Time to sleep

	private LinkedList<Cell> adj; // A list of surrounding cells

	private SwingWorker<Integer, Integer> worker;

	private RobotMoveListener drawingPanel;

	public void setMoveListener(RobotMoveListener l) {
		drawingPanel = l;
	}

	private TreeListener tree;

	public void setTreeListener(TreeListener l) {
		tree = l;
	}

	private GUIUpdater gui;

	public void setGuiUpdater(GUIUpdater gui) {
		this.gui = gui;
	}

	private Algorithm algorithm;

	private ArrayList<Cell> processed; // Cells which have been fully processed (all branches discovered)
	private ArrayList<Cell> discovered; // Cells newly discovered
	private Map<Cell, Cell> parents;

	private boolean finished; // Determines whether the robot has finished its search

	public Robot() {
		processed = new ArrayList<>();
		discovered = new ArrayList<>();
		parents = new HashMap<>();

		algorithm = Algorithm.BFS;

		finished = false;
	}

	private void reset() {
		adj = new LinkedList<>();
		processed.clear();
		discovered.clear();
		parents.clear();
		finished = false;
	}

	// Draws the robot
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(Cell.WIDTH * cc.x() + r / 2, Cell.HEIGHT * cc.y() + r / 2, r, r);
	}

	/*
	 *
	 * BFS
	 * 
	 */
	private void bfs(Cell s) {

		LinkedList<Cell> queue = new LinkedList<>();
		Iterator<Cell> p;

		discovered.add(s);
		queue.add(s);

		while (!queue.isEmpty() && lifeTime >= 0 && !finished && !worker.isCancelled()) {

			Cell n = queue.poll();
			processVertexEarly(n);
			processed.add(n);
			calcAdj(n);
			p = adj.listIterator();
			while (p.hasNext()) {
				Cell a = p.next();
				if (!processed.contains(a)) {
					processEdge(n, a);
				}
				if (!discovered.contains(a)) {
					queue.add(a);
					discovered.add(a);
					parents.put(a, n);
				}
			}
			processVertexLate(n);
		}
	}

	/*
	 *
	 * DFS
	 * 
	 */
	private void dfs(Cell s) {

		if (finished)
			return;

		Iterator<Cell> p;

		discovered.add(s);

		processVertexEarly(s);

		calcAdj(s);
		p = adj.listIterator();
		while (p.hasNext()) {
			Cell n = p.next();
			if (!discovered.contains(n)) {
				parents.put(n, s);
				processEdge(s, n);
				dfs(n);
			} else if (!processed.contains(n)) {
				processEdge(s, n);
				if (finished)
					return;
			}
		}

		processed.add(s);
	}

	/*
	 *
	 * A*
	 * 
	 */
	private void aStar(Cell s) {
		Comparator<Cell> byPathAndGoalCost = (c1, c2) -> {
			return (pathCost(c1) + goalDistance(c1)) - (pathCost(c2) + goalDistance(c2));
		};

		ArrayList<Cell> q = new ArrayList();
		Iterator<Cell> frontier;

		discovered.add(s);
		q.add(s);

		while (!q.isEmpty() && lifeTime >= 0 && !finished && !worker.isCancelled()) {
			Cell n = q.remove(0);
			processVertexEarly(n);
			processed.add(n);
			calcAdj(n);
			frontier = adj.listIterator();
			while (frontier.hasNext()) {
				Cell a = frontier.next();
				if (!processed.contains(a)) {
					processEdge(n, a);
				}
				if (!discovered.contains(a)) {
					discovered.add(a);
					q.add(a);
					parents.put(a, n);
				}
				q.sort(byPathAndGoalCost);
			}
		}
	}

	private int pathCost(Cell v) {
		return Math.abs(parents.get(v).x() - initCell.x()) + Math.abs(parents.get(v).y() - initCell.y()) + 1;
	}

	private int goalDistance(Cell v) {
		return Math.abs(v.x() - grid.getGoalCells().get(0).x()) + Math.abs(v.y() - grid.getGoalCells().get(0).y());
	}

	private void processVertexEarly(Cell v) {
		move(v);
	}

	private void processVertexLate(Cell v) {
		SwingUtilities.invokeLater(() -> gui.addPastCell(v.x(), v.y()));
		if (lifeTime == 0) {
			System.out.println("No Moves Remaining...");
		}
	}

	// Edge between vertices x & y processed
	private void processEdge(Cell x, Cell y) {
		SwingUtilities.invokeLater(() -> tree.addEdge(x, y));
	}

	private void findPath(Cell start, Cell end) {
		if (start == end) {
			System.out.println(start.x() + "," + start.y());
		} else {
			findPath(start, parents.get(end));
			System.out.println(end.x() + "," + end.y());
		}
	}

	// Moves the robot to an appointed cell
	// The robot get's tired every time he moves and thus rests for a second...
	// He also loses a move
	private void move(Cell nextCell) {

		cc = nextCell;

		SwingUtilities.invokeLater(() -> {
			gui.updateCell(cc.x(), cc.y());
			gui.remainingMoves(lifeTime - 1);
			drawingPanel.robotMoved();
		});

		if (grid.getCellStatus(cc).equals(Cell.status.PETROL)) {
			finished = true;
			SwingUtilities.invokeLater(() -> gui.foundPetrol(finished));
			findPath(initCell, cc);
			return;
		}

		rest();
		lifeTime--;
	}

	// Determines whether a given coordinate is beyond the grid
	private boolean outOfBounds(Cell c) {
		return c.x() >= grid.dimX() || c.x() < 0 || c.y() >= grid.dimY() || c.y() < 0;
	}

	// Returns the robot's cell
	public Cell getCurrentCell() {
		return cc;
	}

	// Calculates adjacent vertices
	private void calcAdj(Cell c) {

		adj = new LinkedList<>();

		Cell above = new Cell(c.x(), c.y() - 1);
		if (!outOfBounds(above)) {
			adj.add(above);
		}

		Cell left = new Cell(c.x() - 1, c.y());
		if (!outOfBounds(left)) {
			adj.add(left);
		}

		Cell below = new Cell(c.x(), c.y() + 1);
		if (!outOfBounds(below)) {
			adj.add(below);
		}

		Cell right = new Cell(c.x() + 1, c.y());
		if (!outOfBounds(right)) {
			adj.add(right);
		}

		adj.removeAll(grid.getWallCells()); // Removes all wall cells (which are non-existent in our graph)
	}

	// Otherwise he's just too fast...
	private void rest() {
		try {
			Thread.sleep(zzz);
		} catch (InterruptedException e) {

		}
	}

	public ArrayList<Cell> getProcessed() {
		return processed;
	}

	@Override
	public void updateGrid(Grid g) {
		reset();
		grid = g;
		initCell = cc = grid.robotsInitialCoordinate();
	}

	@Override
	public void updateLifeTime(int l) {
		lifeTime = l + 1;
	}

	@Override
	public void updateSpeed(int s) {
		switch (s) {
		case 1:
			zzz = 3000;
			break;
		case 2:
			zzz = 1000;
			break;
		case 3:
			zzz = 500;
			break;
		case 4:
			zzz = 150;
			break;
		case 5:
			zzz = 50;
			break;
		}
	}

	@Override
	public void algorithmUpdate(Algorithm a) {
		algorithm = a;
	}

	@Override
	public void beginSearch() {
		reset();

		tree.reset();

		gui.foundPetrol(finished);
		gui.removePreviousCoordinates();

		search();
	}

	private void search() {
		worker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				switch (algorithm) {
				case BFS:
					bfs(initCell);
					break;
				case DFS:
					dfs(initCell);
					break;
				case ASTAR:
					aStar(initCell);
					break;
				}
				gui.finishedSearch();
				return null;
			}
		};
		worker.execute();
	}

	@Override
	public void stopSearch() {
		worker.cancel(true);
		gui.finishedSearch();
	}

}
