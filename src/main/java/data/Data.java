package main.java.data;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private String fileName;
	private List<Integer> gridDim;
	private List<Integer> initialState;
	private List<List<Integer>> goalStates;
	private List<List<Integer>> walls;

	public Data() {
		gridDim = new ArrayList<>();
		initialState = new ArrayList<>();
		goalStates = new ArrayList<>();
		walls = new ArrayList<>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Integer> getGridDim() {
		return gridDim;
	}
	
	public Integer getGridDimX() {
		return gridDim.get(1);
	}
	
	public Integer getGridDimY() {
		return gridDim.get(0);
	}
	
	public List<Integer> getInitialState() {
		return initialState;
	}

	public Integer getInitialStateX() {
		return initialState.get(0);
	}
	
	public Integer getInitialStateY() {
		return initialState.get(1);
	}

	public List<List<Integer>> getGoalStates() {
		return goalStates;
	}

	public List<List<Integer>> getWalls() {
		return walls;
	}

}
