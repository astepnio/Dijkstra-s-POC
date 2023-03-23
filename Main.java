import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		Graph cities = new Graph();
		cities.print();
	
		
		System.out.print("Select your root node [numerical index 0-9] : ");
		int rNode;
		Scanner scanner = new Scanner(System.in);
		rNode = scanner.nextInt();
		
		System.out.println("\n\n" + "you have selected: [" + cities.getNode(rNode).getName() + "] \n");
		scanner.close();
		
		cities.runDijkstras(cities.getNode(rNode));
	}

}
