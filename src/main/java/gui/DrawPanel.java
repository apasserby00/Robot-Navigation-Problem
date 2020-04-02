package main.java.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.java.grid.Cell;
import main.java.robot.Robot;
import main.java.tree.Tree;

public class DrawPanel extends JPanel implements RobotMoveListener, TreeModeListener {

	private Tiles tiles;
	private Robot robot;
	private Tree tree;

	private boolean treeView;

	public DrawPanel() {
		setPreferredSize(new Dimension(500, 300));
		treeView = false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (tiles != null) {
			if (treeView) {
				tree.draw(g);
			} else {
				tiles.draw(g);
				robot.draw(g);
			}
		}

	}

	@Override
	public void treeMode(boolean b) {
		treeView = b;
		repaint();
	}

	public void updateElements(Tiles tiles, Robot robot, Tree tree) {

		this.tiles = tiles;
		this.robot = robot;
		this.tree = tree;

		setPreferredSize(new Dimension(tiles.getColumns() * Cell.WIDTH, tiles.getRows() * Cell.HEIGHT));

		repaint();
	}

	@Override
	public void robotMoved() {
		repaint();
	}
}
