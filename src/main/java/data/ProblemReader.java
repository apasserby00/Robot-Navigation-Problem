package main.java.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.java.data.updatelisteners.FileListener;

public class ProblemReader implements FileListener {

	private File file;
	private Scanner scanner;
	private Data data;

	public void read(String filePath) {

		data = new Data();

		file = new File(filePath);

		try {
			scanner = new Scanner(file);
			scanFile(scanner);
			data.setFileName(extractFileName(filePath));
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Something went wrong reading the file...");
			e.printStackTrace();
		}
	}

	private void scanFile(Scanner s) {

		String line;
		int index = 0;

		while (s.hasNextLine()) {

			line = s.nextLine();

			switch (index) {
			// First Line -> Grid Dimensions
			case 0:
				data.getGridDim().add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[0]));
				data.getGridDim().add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[1]));
				break;
			// Second Line -> Initial State
			case 1:
				data.getInitialState().add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[0]));
				data.getInitialState().add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[1]));
				break;
			// Third Line -> Goal States
			case 2:
				String[] rawGoals = line.split("\\|");
				for (int i = 0; i < rawGoals.length; i++) {
					List<Integer> goalState = new ArrayList<>();
					goalState.add(0,
							Integer.parseInt(rawGoals[i].substring(1, rawGoals[i].length() - 1).split(",")[0]));
					goalState.add(1,
							Integer.parseInt(rawGoals[i].substring(1, rawGoals[i].length() - 1).split(",")[1]));
					data.getGoalStates().add(goalState);
				}
				break;
			// Every Other Line -> Wall
			default:
				List<Integer> wall = new ArrayList<>();
				wall.add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[0]));
				wall.add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[1]));
				wall.add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[2]));
				wall.add(Integer.parseInt(line.substring(1, line.length() - 1).split(",")[3]));
				data.getWalls().add(wall);
				break;
			}
			index++;
		}
	}

	private String extractFileName(String s) {
		return s.substring(s.lastIndexOf('\\') + 1, s.length());
	}

	public Data getData() {
		return data;
	}

	@Override
	public void newFile(String path) {
		read(path);
	}

}
