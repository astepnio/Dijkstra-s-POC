import java.util.*;
import java.lang.Double;

public class Graph {
	
	private List<Node> nodes = new ArrayList<>();
	private double[] minDistances;
	private PriorityQueue<Double[]> priorityQueue;
	private double[][] edgeToEdge;
	private Comparator<Double[]> comparator;
	
	public Graph() {
		//create all our airport nodes and add to the list
		nodes.add( new Node("DEN") ); //0
		nodes.add( new Node("SEA") ); //1
		nodes.add( new Node("PHX") ); //2
		nodes.add( new Node("SAN") ); //3
		nodes.add( new Node("DFS") ); //4
		nodes.add( new Node("CHI") ); //5
		nodes.add( new Node("JFK") ); //6
		nodes.add( new Node("BWI") ); //7
		nodes.add( new Node("ATL") ); //8
		nodes.add( new Node("MIA") ); //9
		
		//create edges between our nodes
		nodes.get(6).addEdge(new Edge(nodes.get(5), 136.89));//JFK -> CHI  136.89
		nodes.get(7).addEdge(new Edge(nodes.get(6), 99.09)); //BWI -> JFK  99.09
		nodes.get(8).addEdge(new Edge(nodes.get(7), 209.87));//ATL -> BWI  209.87
		nodes.get(8).addEdge(new Edge(nodes.get(9), 187.67));//ATL -> MIA  187.67
		nodes.get(8).addEdge(new Edge(nodes.get(4), 111.49));//ATL -> DFS  111.49
		nodes.get(8).addEdge(new Edge(nodes.get(5), 109.35));//ATL -> CHI  109.35
		nodes.get(4).addEdge(new Edge(nodes.get(9), 127.63));//DFS -> MIA  127.63
		nodes.get(4).addEdge(new Edge(nodes.get(5), 139.97));//DFS -> CHI  139.97
		nodes.get(4).addEdge(new Edge(nodes.get(0), 186.36));//DFS -> DEN  186.36
		nodes.get(4).addEdge(new Edge(nodes.get(3), 176.18));//DFS -> SAN  176.18
		nodes.get(5).addEdge(new Edge(nodes.get(0), 104.84));//CHI -> DEN  104.84
		nodes.get(5).addEdge(new Edge(nodes.get(1), 159.54));//CHI -> SEA  159.54
		nodes.get(0).addEdge(new Edge(nodes.get(2), 211.16));//DEN -> PHX  211.16
		nodes.get(0).addEdge(new Edge(nodes.get(1), 166.90));//DEN -> SEA  166.90
		nodes.get(2).addEdge(new Edge(nodes.get(3), 142.82));//PHX -> SAN  142.82
		
		
		comparator = new Comparator<Double[]>(){ //used to compare the second value (weight) so we can organize our priorityQueue
			@Override
			public int compare(Double[] o1, Double[] o2) {
				return (int) (o1[1] - o2[1]);
			}
		};
		
		minDistances = new double[nodes.size()];
		edgeToEdge = new double[nodes.size()][3]; //size of (# nodes) x 3
		priorityQueue = new PriorityQueue<>(nodes.size(), comparator );
    
    //initalize all distances to -1 (unknown)
		for(int i = 0; i < minDistances.length; ++i) {
			minDistances[i] = -1.0;
		}
		
	}
	
	public void runDijkstras(Node root) {
		
		Node currentNode = root;
				
		priorityQueue.add(new Double[] { (double)getNodeIndex(currentNode) , 0.0}); //dest, cost
		minDistances[getNodeIndex(root)] = 0;
		edgeToEdge[getNodeIndex(root)] = new double[] {getNodeIndex(root), getNodeIndex(root), 0  }; //src, dest, cost
		
		for(int o = 0; o < nodes.size(); ++o ) { // runs nodes.size() times (until queue is empty & all nodes visited)
			currentNode = nodes.get(priorityQueue.poll()[0].intValue());   //retrieve node with lowest weight from priority queue 
			currentNode.setVisited(true); //mark the node as visited
			
			//update all adjacent vertices
			for(int n = 0; n < nodes.size(); ++n) { //for each node
				for(int e = 0; e < nodes.get(n).getNumEdges(); ++e) {  //each edge in specified node
					if(nodes.get(n) == currentNode){   //from currentNode -> (n)
						if(!compareToQueue(currentNode.getEdge(e).getDest()) && !currentNode.getEdge(e).getDest().isVisited() ) { //if node is previously unknown
							
							currentNode.getEdge(e).getDest().setOrigin(currentNode);
							
							priorityQueue.add(new Double[]{ (double)getNodeIndex(currentNode.getEdge(e).getDest()) 
									, minDistances[getNodeIndex(currentNode)] + currentNode.getEdge(e).getWeight() }); //add new node to queue
							
							minDistances[getNodeIndex( currentNode.getEdge(e).getDest() )] 
									= minDistances[getNodeIndex(currentNode)] +  currentNode.getEdge(e).getWeight(); //update distance
							
						} 
						else if(minDistances[getNodeIndex(currentNode.getEdge(e).getDest())]
								> minDistances[getNodeIndex(currentNode)] + currentNode.getEdge(e).getWeight() ) { //if node is already in queue, check for shorter distance
							
							minDistances[getNodeIndex(currentNode.getEdge(e).getDest())] = minDistances[getNodeIndex(currentNode)] + currentNode.getEdge(e).getWeight();
							
						}
					}
					if(nodes.get(n).getEdge(e).getDest() == currentNode) {  //from (n) -> currentNode
						if(!compareToQueue(nodes.get(n)) && !nodes.get(n).isVisited() ) {
							
							nodes.get(n).setOrigin(currentNode);
							
							priorityQueue.add(new Double[] { (double)getNodeIndex(nodes.get(n))
									, minDistances[getNodeIndex(currentNode)] + nodes.get(n).getEdge(e).getWeight() });
					
							
							minDistances[getNodeIndex( nodes.get(n))] 
									= minDistances[getNodeIndex(currentNode)] + nodes.get(n).getEdge(e).getWeight(); //update distance
							
						}
						else if(minDistances[getNodeIndex(nodes.get(n))] 
								> minDistances[getNodeIndex(currentNode)] + nodes.get(n).getEdge(e).getWeight() ) {
							
							minDistances[getNodeIndex(nodes.get(n))] = minDistances[getNodeIndex(currentNode)] + nodes.get(n).getEdge(e).getWeight();
							
						}
					} 	
				} 
			} //
			if(currentNode != root) edgeToEdge[getNodeIndex(currentNode)] //update the edgeToEdge array so we can reconstruct our path later
					= new double[] {getNodeIndex(currentNode.getOrigin()), getNodeIndex(currentNode), minDistances[getNodeIndex(currentNode)] };
			
		}
	
		
		for(int i = 0; i < minDistances.length; ++i) {
			System.out.print("\n Lowest cost to [ " + nodes.get(i).getName() + " ]  " + minDistances[i] );
		}
		
		System.out.println("\n\n\nThe shortest path to each node from '" + root.getName() + "' is as follows: ");
		
		int prev = -1; 
		for(int i = 0; i < nodes.size(); ++i) { //reconstruct shortest path to nodes using 'edgeToEdge' array
			System.out.print(  "{" + nodes.get(i).getName() + "}" );
			prev = (int) edgeToEdge[i][0];
			while(nodes.get(prev) != root) {
				if( prev != getNodeIndex(root) ) {
					System.out.print( " -> " + nodes.get(prev).getName() );
					prev  = (int) edgeToEdge[prev][0];
				} 
			}
			System.out.print( " -> " + root.getName() + "\n");
		}
		
		
	}
	
	public void print() {
		System.out.print("Nodes: ");
		for(int i = 0; i < nodes.size(); ++i) {
			System.out.print("[" + i + "]" + nodes.get(i).getName() + ", ");
		}
		System.out.print("\n");
	}
	
	public Node getNode(int index) {
		return nodes.get(index);
	}
	
	private int getNodeIndex(Node n) {
		int index = 0;
		for(int i = 0; i < nodes.size(); ++i) { //find index of root node in "nodes" array
			if(nodes.get(i) == n)  {
				index = i;
			}
		}
		return index;
	}
	
	private boolean compareToQueue(Node comparee) { //returns true if node already exists in queue
		for(Double[] d : priorityQueue) {
			if( d[0] == getNodeIndex(comparee) ) return true;
		}
		return false;
	}
	
	
}
