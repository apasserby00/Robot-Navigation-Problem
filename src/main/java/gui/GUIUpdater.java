package main.java.gui;

public interface GUIUpdater {
	
	void foundPetrol(boolean b);
	
	void remainingMoves(int n);
	
	void updateCell(int x, int y);
	
	void removePreviousCoordinates();
	
	void addPastCell(int x, int y);

	void finishedSearch();
}
