package main.java.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import main.java.algorithm.Algorithm;

public class ControlPanel extends JPanel implements GUIUpdater {

	JButton fileSettingsB;

	public JButton getFileSettingsB() {
		return fileSettingsB;
	}

	private RobotSettingsPanel robotPanel;
	private SearchStatusPanel searchStatusPanel;

	JToggleButton toggleViewB;

	public JToggleButton getToggleViewB() {
		return toggleViewB;
	}

	private TreeModeListener listeningTree;

	public void setTreeModeListener(TreeModeListener l) {
		listeningTree = l;
	}

	private GUIListener listeningRobot;

	public void setGuiListener(GUIListener l) {
		listeningRobot = l;
	}

	private SearchListener listeningFileDialog;

	public void setSearchListener(SearchListener l) {
		listeningFileDialog = l;
	}

	public ControlPanel() {

		setPreferredSize(new Dimension(300, 500));

		robotPanel = new RobotSettingsPanel();
		searchStatusPanel = new SearchStatusPanel();

		fileSettingsB = new JButton("File Settings");
		fileSettingsB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		toggleViewB = new JToggleButton("Tree Mode");
		toggleViewB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		toggleViewB.setEnabled(false);
		toggleViewB.setAlignmentX(CENTER_ALIGNMENT);
		toggleViewB.addActionListener(e -> listeningTree.treeMode(toggleViewB.isSelected()));

		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 0.25;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets.right = 7;
		add(fileSettingsB, gc);
		gc.insets.right = 0;

		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 1;
		gc.weighty = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(robotPanel, gc);

		gc.gridx = 0;
		gc.gridy = 2;
		gc.weightx = 1;
		gc.weighty = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(searchStatusPanel, gc);

		gc.gridx = 0;
		gc.gridy = 3;
		gc.weightx = 1;
		gc.weighty = 0.25;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets.left = 7;
		add(toggleViewB, gc);
		gc.insets.left = 0;

		robotPanel.algorithmCB.addActionListener(e -> {
			Algorithm a;
			String selection = robotPanel.algorithmCB.getSelectedItem().toString();
			switch (selection) {
			case "Breadth First Search":
				a = Algorithm.BFS;
				break;
			case "Depth First Search":
				a = Algorithm.DFS;
				break;
			case "A Star":
				a = Algorithm.ASTAR;
				break;
			default:
				a = Algorithm.BFS;
				break;
			}
			listeningRobot.algorithmUpdate(a);
		});

		robotPanel.lifeTimeT.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				listeningRobot.updateLifeTime(Integer.parseInt(robotPanel.lifeTimeT.getText()));
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				listeningRobot.updateLifeTime(0);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		robotPanel.speedS.addChangeListener(e -> {
			listeningRobot.updateSpeed(robotPanel.speedS.getValue());
		});

		robotPanel.startB.addActionListener(e -> {

			disablePanelControls();

			robotPanel.stopB.setVisible(true);

			toggleViewB.setEnabled(true);
			robotPanel.speedS.setEnabled(true);

			listeningFileDialog.isSearching(true);

			listeningRobot.updateLifeTime(Integer.parseInt(robotPanel.lifeTimeT.getText()));
			listeningRobot.beginSearch();
		});

		disablePanelControls();
	}

	private class RobotSettingsPanel extends JPanel {

		// Robot speeds
		private static final int SPEED_MIN = 1;
		private static final int SPEED_MAX = 5;
		private static final int SPEED_INIT = 3;

		private JLabel searchAlgorithmL;
		private JComboBox algorithmCB;
		private JLabel lifeTimeL;
		private JTextField lifeTimeT;
		private JLabel speedL;
		private JSlider speedS;
		private JButton startB;
		private JButton stopB;

		public RobotSettingsPanel() {

			searchAlgorithmL = new JLabel("Algorithm: ");
			String[] algorithms = { "Breadth First Search", "Depth First Search", "A Star"};
			algorithmCB = new JComboBox<>(algorithms);

			lifeTimeL = new JLabel("Life Time: ");
			lifeTimeT = new JTextField("75", 55);
			PlainDocument lifeTime = (PlainDocument) lifeTimeT.getDocument();
			lifeTime.setDocumentFilter(new IntegerFilter());

			speedL = new JLabel("Speed: ");
			speedS = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_INIT);
			speedS.setMajorTickSpacing(1);
			speedS.setPaintLabels(true);
			startB = new JButton("START");
			startB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			stopB = new JButton("STOP");
			stopB.setVisible(false);
			stopB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			stopB.addActionListener(e -> {
				listeningRobot.stopSearch();
			});

			Border innerBorder = BorderFactory.createTitledBorder(null, "Agent Settings",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
					new Font("SansSerif", Font.PLAIN, 18));
			Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
			setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

			setLayout(new GridBagLayout());

			GridBagConstraints gc = new GridBagConstraints();

			gc.gridx = 0;
			gc.gridy = 0;
			gc.weightx = 1;
			gc.weighty = 1;
			gc.fill = GridBagConstraints.HORIZONTAL;

			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets.top = 5;
			gc.insets.left = 7;
			add(searchAlgorithmL, gc);
			gc.insets.left = 0;

			gc.gridx = 1;
			gc.gridy = 0;
			gc.insets.right = 7;
			add(algorithmCB, gc);
			gc.insets.right = 0;
			gc.insets.top = 0;

			gc.gridx = 0;
			gc.gridy = 1;
			gc.insets.left = 7;
			add(lifeTimeL, gc);
			gc.insets.left = 0;

			gc.gridx = 1;
			gc.gridy = 1;
			gc.insets.right = 7;
			add(lifeTimeT, gc);
			gc.insets.right = 0;
			
			gc.gridx = 0;
			gc.gridy = 2;
			gc.insets.left = 7;
			add(speedL, gc);
			gc.insets.left = 0;

			gc.gridx = 1;
			gc.gridy = 2;
			gc.insets.right = 7;
			add(speedS, gc);
			
			gc.gridx = 1;
			gc.gridy = 3;
			gc.anchor = GridBagConstraints.LINE_END;
			gc.fill = GridBagConstraints.NONE;
			add(startB, gc);
			add(stopB, gc);
			gc.insets.right = 0;
		}
	}

	private class SearchStatusPanel extends JPanel {

		private JLabel foundPetrolL;
		private JLabel petrolL;
		private JLabel movesRemainingL;
		private JLabel movesL;
		private JLabel currentCellL;
		private JLabel cellL;
		private JLabel cellHistoryL;
		private JTextArea cellHistoryTA;
		private JScrollPane historyScroller;

		public SearchStatusPanel() {

			foundPetrolL = new JLabel("Found Petrol: ");
			petrolL = new JLabel("NO");
			movesRemainingL = new JLabel("Moves Remaining: ");
			movesL = new JLabel("0");
			currentCellL = new JLabel("Current Cell: ");
			cellL = new JLabel("(-1, -1)");
			cellHistoryTA = new JTextArea(10, 10);
			cellHistoryTA.setEditable(false);
			cellHistoryL = new JLabel("Previous Cells");
			historyScroller = new JScrollPane(cellHistoryTA, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			historyScroller.setPreferredSize(new Dimension(50, 50));

			Border innerBorder = BorderFactory.createTitledBorder(null, "Search Information",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
					new Font("SansSerif", Font.PLAIN, 18));
			Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
			setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

			setLayout(new GridBagLayout());

			GridBagConstraints gc = new GridBagConstraints();

			gc.gridx = 0;
			gc.gridy = 0;
			gc.weightx = 1;
			gc.weighty = 1;

			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets.top = 5;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets.left = 7;
			add(foundPetrolL, gc);
			gc.insets.left = 0;

			gc.gridx = 0;
			gc.gridy = 0;
			gc.anchor = GridBagConstraints.CENTER;
			add(petrolL, gc);
			gc.insets.top = 0;

			gc.gridx = 0;
			gc.gridy = 1;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets.left = 7;
			add(movesRemainingL, gc);
			gc.insets.left = 0;

			gc.gridx = 0;
			gc.gridy = 1;
			gc.anchor = GridBagConstraints.CENTER;
			add(movesL, gc);

			gc.gridx = 0;
			gc.gridy = 2;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets.left = 7;
			add(currentCellL, gc);
			gc.insets.left = 0;

			gc.gridx = 0;
			gc.gridy = 2;
			gc.anchor = GridBagConstraints.CENTER;
			add(cellL, gc);

			gc.gridx = 0;
			gc.gridy = 3;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets.left = 7;
			add(cellHistoryL, gc);

			gc.gridx = 0;
			gc.gridy = 4;
			gc.fill = GridBagConstraints.HORIZONTAL;
			add(historyScroller, gc);
			gc.insets.left = 0;
		}

	}

	@Override
	public void foundPetrol(boolean b) {
		if (b) {
			searchStatusPanel.petrolL.setText("Call the president.");
		} else {
			searchStatusPanel.petrolL.setText("NO");
		}
	}

	@Override
	public void remainingMoves(int n) {
		searchStatusPanel.movesL.setText(Integer.toString(n));
	}

	@Override
	public void updateCell(int x, int y) {
		searchStatusPanel.cellL.setText("(" + x + ", " + y + ")");
	}

	@Override
	public void removePreviousCoordinates() {
		searchStatusPanel.cellHistoryTA.setText("");
	}

	@Override
	public void addPastCell(int x, int y) {
		JTextArea history = searchStatusPanel.cellHistoryTA;

		if (history.getText().equals("")) {
			history.setText("(" + x + "," + y + ")");
		} else {
			history.setText(history.getText() + "-> (" + x + "," + y + ")");
		}

		validate();
		searchStatusPanel.historyScroller.getHorizontalScrollBar()
				.setValue(searchStatusPanel.historyScroller.getHorizontalScrollBar().getMaximum());
	}

	private void disablePanelControls() {
		robotPanel.startB.setVisible(false);
		toggleViewB.setEnabled(false);
		robotPanel.algorithmCB.setEnabled(false);
		robotPanel.lifeTimeT.setEnabled(false);
		robotPanel.speedS.setEnabled(false);
	}

	public void enablePanelControls() {
		robotPanel.startB.setVisible(true);
		robotPanel.stopB.setVisible(false);
		toggleViewB.setEnabled(true);
		robotPanel.algorithmCB.setEnabled(true);
		robotPanel.lifeTimeT.setEnabled(true);
		robotPanel.speedS.setEnabled(true);
	}

	@Override
	public void finishedSearch() {
		enablePanelControls();
		listeningFileDialog.isSearching(false);
	}

}
