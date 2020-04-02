package main.java.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.java.grid.Cell;
import main.java.grid.Cell.status;
import main.java.grid.Grid;

public class Tiles implements GridUpdate {

	private Grid grid;

	private BufferedImage tileSheet;

	private int rows;
	private int columns;

	public Tiles() {
		tileSheet = loadTileSheet(
				"E:\\University\\2020\\Semester 1\\AI\\assignment-one\\The Robot Navigation Problem\\src\\main\\resources\\images/tiles.jpg");
	}

	private BufferedImage loadTileSheet(String file) {

		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(file));
		} catch (Exception e) {
			System.out.println("Could not load image file.");
		}

		return img;
	}

	public void draw(Graphics g) {

		for (int y = 0; y < rows; y++) {

			for (int x = 0; x < columns; x++) {

				Cell.status cell = grid.getCellStatus(new Cell(x, y));

				int index;

				if (cell == status.WALL) {
					// Wall Tile
					index = 1;
				} else if (cell == status.PETROL) {
					// Green Tile
					index = 2;
				} else {
					// White Tile
					index = 0;
				}

				g.drawImage(tileSheet, x * Cell.WIDTH, y * Cell.HEIGHT, (x * Cell.WIDTH) + Cell.WIDTH,
						(y * Cell.HEIGHT) + Cell.HEIGHT, index * Cell.WIDTH + 1, 0, (index * Cell.WIDTH) + Cell.WIDTH,
						Cell.HEIGHT, null);

			}
		}

	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	@Override
	public void updateGrid(Grid g) {
		grid = g;
		columns = grid.dimX();
		rows = grid.dimY();
	}
}
