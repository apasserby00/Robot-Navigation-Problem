package main.java.gui;

import main.java.algorithm.Algorithm;

public interface GUIListener {

	void updateLifeTime(int l);
	
	void updateSpeed(int s);
	
	void algorithmUpdate(Algorithm a);

	void beginSearch();
	
	void stopSearch();
}
