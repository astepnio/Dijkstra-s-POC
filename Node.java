import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private String name;
	private List<Edge> edges = new ArrayList<Edge>();
	private boolean visited = false;
	private Node origin;
	
	public Node(String name) {
		this.name = name;
		this.origin = null;
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	public Edge getEdge(int index) {
		return edges.get(index);
	}
	
	public boolean isVisited() { return visited; };
	public void setVisited(boolean visited) { this.visited = visited; };
	public String getName() { return name; };
	public int getNumEdges() {return edges.size();};
	public void setOrigin(Node origin) {this.origin = origin;};
	public Node getOrigin() { return origin; };
}	
