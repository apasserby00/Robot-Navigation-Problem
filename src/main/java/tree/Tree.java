package main.java.tree;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import main.java.data.updatelisteners.RobotUpdate;
import main.java.grid.Cell;
import main.java.grid.Grid;
import main.java.gui.GridUpdate;
import main.java.robot.Robot;
import main.java.robot.TreeListener;

public class Tree implements GridUpdate, RobotUpdate, TreeListener {

	private Robot robot;

	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;

	private int r = 10; // Vertex size

	public Tree() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}

	public void draw(Graphics g) {

		FontMetrics f = g.getFontMetrics();

		g.setColor(Color.DARK_GRAY);
		for (Vertex v : vertices) {
			g.fillOval((scale(v.x()) - r / 2), (scale(v.y()) - r / 2), r, r);
			g.drawString(v.toString(), scale(v.x()) - f.stringWidth(v.toString()) / 2,
					(scale(v.y()) + f.getHeight() / 2) - 20);
		}

		g.setColor(Color.WHITE);
		for (Edge e : edges) {
			g.drawLine(scale(e.x().x()), scale(e.x().y()), scale(e.y().x()), scale(e.y().y()));
		}

		g.setColor(Color.WHITE);
		for (Cell c : robot.getProcessed()) {
			g.fillOval((scale(c.x()) - r / 2), (scale(c.y()) - r / 2), r, r);
			g.drawString(c.toString(), scale(c.x()) - f.stringWidth(c.toString()) / 2,
					(scale(c.y()) + f.getHeight() / 2) - 20);
		}

		g.setColor(Color.RED);
		g.fillOval(scale(robot.getCurrentCell().x()) - r / 2, scale(robot.getCurrentCell().y()) - r / 2, r, r);
	}

	private int scale(int x) {
		return (x * Cell.WIDTH) + Cell.WIDTH / 2;
	}

	public void reset() {
		edges = new ArrayList<>();
	}

	@Override
	public void updateGrid(Grid g) {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();

		for (Cell c : g.getTraversableCells()) {
			vertices.add(new Vertex(c.x(), c.y()));
		}
	}

	@Override
	public void updateRobot(Robot r) {
		robot = r;
	}

	@Override
	public void addEdge(Cell x, Cell y) {
		edges.add(new Edge(new Vertex(x.x(), x.y()), new Vertex(y.x(), y.y())));
	}
}
