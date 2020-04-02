package main.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceNightShadeLookAndFeel;

import main.java.data.ProblemReader;
import main.java.grid.Grid;
import main.java.gui.ControlPanel;
import main.java.gui.DrawPanel;
import main.java.gui.FileSettingsDialog;
import main.java.gui.Tiles;
import main.java.robot.Robot;
import main.java.tree.Tree;

public class Application extends JFrame {

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(new SubstanceNightShadeLookAndFeel());
			} catch (Exception e) {
				System.out.println("Substance Graphite failed to initialize");
			}
			Application app = new Application();
			app.setVisible(true);
		});

	}

	public Application() {
		super("The Robot Navigation Problem");
		setIcon(this);

		ControlPanel controlPanel = new ControlPanel();
		FileSettingsDialog fileSettingsDialog = new FileSettingsDialog(this);
		controlPanel.setSearchListener(fileSettingsDialog);

		ProblemReader problemReader = new ProblemReader();

		//
		problemReader.read(
				"E:\\University\\2020\\Semester 1\\AI\\assignment-one\\The Robot Navigation Problem\\src\\main\\resources\\problems/problem5.txt");
		//

		fileSettingsDialog.setProblemReader(problemReader);

		Grid grid = new Grid();

		Tiles tiles = new Tiles();

		Robot robot = new Robot();
		controlPanel.setGuiListener(robot);

		Tree tree = new Tree();
		robot.setTreeListener(tree);

		DrawPanel drawPanel = new DrawPanel();
		controlPanel.setTreeModeListener(drawPanel);
		robot.setMoveListener(drawPanel);

		//
		grid.updateData(problemReader.getData());

		tiles.updateGrid(grid);

		tree.updateGrid(grid);
		tree.updateRobot(robot);

		robot.updateGrid(grid);
		robot.setGuiUpdater(controlPanel);

		drawPanel.updateElements(tiles, robot, tree);

		controlPanel.getToggleViewB().setEnabled(true);
		controlPanel.enablePanelControls();
		//

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.EAST);
		this.add(drawPanel, BorderLayout.WEST);
		this.setResizable(false);
		this.pack();

		Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(sd.width / 2 - this.getWidth() / 2, sd.height / 2 - this.getHeight() / 2);

		controlPanel.getFileSettingsB().addActionListener(e -> {
			fileSettingsDialog.setVisible(true);
			fileSettingsDialog.saveLabelState();
		});

		fileSettingsDialog.getOkB().addActionListener(e -> {
			if (problemReader.getData() != null) {
				fileSettingsDialog.saveLabelState();

				grid.updateData(problemReader.getData());

				tiles.updateGrid(grid);

				tree.updateGrid(grid);
				tree.updateRobot(robot);

				robot.updateGrid(grid);
				robot.setGuiUpdater(controlPanel);

				drawPanel.updateElements(tiles, robot, tree);

				controlPanel.getToggleViewB().setEnabled(true);
				controlPanel.enablePanelControls();

				this.add(drawPanel, BorderLayout.WEST);
				this.pack();
			}
			fileSettingsDialog.setVisible(false);
		});

		fileSettingsDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				fileSettingsDialog.revertLabels();
				super.windowClosed(e);
			}
		});
	}

	private void setIcon(JFrame f) {
		ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "\\classes\\main\\resources\\images/icon.png");
		f.setIconImage(icon.getImage());
	}

}
