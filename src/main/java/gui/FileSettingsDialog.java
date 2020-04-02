package main.java.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.data.Data;
import main.java.data.ProblemReader;

public class FileSettingsDialog extends JDialog implements SearchListener {

	private ProblemReader problemReader;
	private Data data;

	private JFileChooser fileChooser;
	private JButton newFileB;

	private JLabel fileNameL;
	private JLabel rowL;
	private JLabel columnL;
	private JLabel initCellL;
	private JLabel petrolCellsL;

	private String fileNameLSaved;
	private String rowLSaved;
	private String columnLSaved;
	private String initCellLSaved;
	private String petrolCellsLSaved;

	private JButton okB;
	private JButton closeB;

	public FileSettingsDialog(JFrame parent) {
		super(parent, "File Settings");
		setSize(300, 200);
		setLocationRelativeTo(parent);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Robot Navigation Problem Files Only", "txt"));
		String problemDirectory = System.getProperty("user.dir") + "\\classes\\main\\resources\\problems";
		fileChooser.setCurrentDirectory(new File(problemDirectory));
		
		newFileB = new JButton("Select New File");
		newFileB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newFileB.addActionListener(e -> {
			if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getPath();
				problemReader.read(filePath);
				data = problemReader.getData();
				okB.setVisible(true);
				updateLabels();
			}
		});

		okB = new JButton("Ok");
		okB.setVisible(false);
		okB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeB = new JButton("Close");
		closeB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeB.addActionListener(e -> this.dispose());

		fileNameL = new JLabel("File: ");
		rowL = new JLabel("Rows: ");
		columnL = new JLabel("Columns: ");
		initCellL = new JLabel("Starting Cell: ");
		petrolCellsL = new JLabel("Petrol Cells: ");

		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets.right = 150;
		add(fileNameL, gc);
		gc.insets.right = 0;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets.right = 20;
		add(newFileB, gc);
		gc.insets.right = 0;

		gc.gridx = 0;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets.right = 100;
		add(rowL, gc);
		gc.insets.right = 0;

		gc.gridx = 0;
		gc.gridy = 1;
		gc.insets.left = 100;
		add(columnL, gc);
		gc.insets.left = 0;

		gc.gridx = 0;
		gc.gridy = 2;
		add(initCellL, gc);

		gc.gridx = 0;
		gc.gridy = 3;
		add(petrolCellsL, gc);

		gc.gridx = 0;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets.left = 20;
		add(okB, gc);
		gc.insets.left = 20;

		gc.gridx = 0;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets.right = 20;
		add(closeB, gc);
		gc.insets.right = 0;
	}

	public void saveLabelState() {
		fileNameLSaved = fileNameL.getText();
		rowLSaved = rowL.getText();
		columnLSaved = columnL.getText();
		initCellLSaved = initCellL.getText();
		petrolCellsLSaved = petrolCellsL.getText();
	}

	public void revertLabels() {
		fileNameL.setText(fileNameLSaved);
		rowL.setText(rowLSaved);
		columnL.setText(columnLSaved);
		initCellL.setText(initCellLSaved);
		petrolCellsL.setText(petrolCellsLSaved);
	}

	private void updateLabels() {
		fileNameL.setText("File: " + data.getFileName());
		rowL.setText("Rows: " + data.getGridDimY());
		columnL.setText("Columns: " + data.getGridDimX());
		initCellL.setText("Starting Cell: (" + data.getInitialStateX() + ", " + data.getInitialStateY() + ")");
		String petrolCoordinates = "";
		for(List<Integer> c : data.getGoalStates()) {
			if (petrolCoordinates == "") {
				petrolCoordinates = "(" + c.get(0) + ", " + c.get(1) + ")";
			} else {
				petrolCoordinates = petrolCoordinates + ", " + "(" + c.get(0) + ", " + c.get(1) + ")";
			}
		}
		petrolCellsL.setText("Petrol Cells: " + petrolCoordinates);
	}

	public JButton getOkB() {
		return okB;
	}

	public void setProblemReader(ProblemReader problemReader) {
		this.problemReader = problemReader;
	}

	@Override
	public void isSearching(boolean b) {
		newFileB.setEnabled(!b);
		okB.setVisible(!b);
	}
}
