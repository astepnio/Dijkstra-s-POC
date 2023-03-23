import java.lang.Double;

public class Edge {

	Node dst;
	Double weight;
	
	public Edge(Node dst, double weight) {
		this.dst = dst;
		this.weight = weight;
	}
	
	public Node getDest() { return dst; };
	public Double getWeight() {return weight;};
}
