package main.java.tree;

public class Edge {

	private Vertex x;
	private Vertex y;

	public Edge(Vertex x, Vertex y) {
		this.x = x;
		this.y = y;
	}
	
	public Vertex x() {
		return x;
	}
	
	public Vertex y() {
		return y;
	}
}
