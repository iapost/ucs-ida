/** @file Main.java */

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

public class Main {
	
	/** Global variable holding the information of the graph formed by the roads */
	static HashMap<String, Vertex> graph=new HashMap<String, Vertex>();
	
	/**
	 * Executes Uniform Cost Search algorithm and prints the best path found.
	 * 
	 * @param source the name of the starting vertex
	 * @param destination the name of the destination
	 * @param predictions a HashMap containing the predictions for each road's traffic
	 * @return true if a path was found and false otherwise
	 *  
	 */
	public static boolean UCS(String source, String destination, HashMap<String, Double> predictions) {
		
		//A set to hold the roads that were already visited
		HashSet<String> visitedRoads = new HashSet<String>();
				
		//A queue holding the nodes to be examined
		PriorityQueue<UCSNode> fringe = new PriorityQueue<UCSNode>();
		
		//Initialize fringe
		fringe.add(new UCSNode(source,0));
		
		//Loop while fringe is not empty
		while (!fringe.isEmpty()) {
			
			//Pop the node with the least cost
			UCSNode node = fringe.poll();
			
			//Check if current node is the destination
			if(node.nodeName.equals(destination)) {
				
				//Print result and return
				System.out.println("Uniform Cost Search:");
				System.out.println("\tPath: " + node.path); 
				System.out.println("\tCost: " + node.currentCost); 
				return true;
				
			} else {//If current node is not the destination
				
				//Retrieve vector with all the roads beginning from current node
				Vector<Road> roads = graph.get(node.nodeName).roads;
				
				//Execute for all roads beginning from current node
				for (int k = 0; k < roads.size(); k++) {
					
					//Retrieve current road
					Road road = roads.get(k);
					
					//Check if current road was already visited by the algorithm
					//If it was already visited continue to the next road, otherwise add the current road to visited roads
					if(!visitedRoads.add(road.name)) {
						continue;
					}
					
					//Find the predicted road cost rounded to 2 decimal digits
					double roadCost = Math.round(road.cost * predictions.get(road.name) * 100) / 100.0;
					
					//Create new node for the vertex that the current road leads to
					//The new node's cost is the current node's cost plus the cost of the current road
					UCSNode child = new UCSNode(road.leadsTo, node.currentCost + roadCost); 
					
					//Set the path that will be printed as the result
					child.path = road.name + "(" + roadCost + ")";
					
					//If this is not the first road in the path, append it to the end of the current node's path
					if(!node.path.equals("")) {
						child.path = node.path + " -> " + child.path;
					}
					
					//Add new node to the fringe to be examined later
					fringe.add(child);
				}
			}
		}
		
		//Reaching this point means there are no more nodes to be examined and the destination was never found
		return false;
	}
	
	
	/**
	 * Executes Iterative Deepening A* algorithm and prints the best path found
	 * 
	 * @param source the name of the vertex to begin
	 * @param destination the name of the destination vertex
	 * @param predictions a HashMap containing the predictions for each road's traffic
	 * @return true if a path was found and false otherwise
	 * 
	 */
	public static boolean IDA(String source, String destination, HashMap<String, Double> predictions) {

		//Initialize cost limit to the cost provided by the heuristic function
		double costLimit = graph.get(source).minCostToDest;
		
		//Loop until a path is found or until the search is exhausted
		while(true){
			
			//Search with the given cost limit
			IDAResult res = IDA_recursive(source, destination, null, 0, predictions, costLimit);
			
			//If a path was found, print the result and return
			if(res.finished) {
				System.out.println("IDA*:");
				System.out.println("\tPath: " + res.path);
				System.out.println("\tCost: " + res.totalPathCost);
				return true;
			}
			
			//If the new cost limit was not increased, the graph was searched in its whole and no path was found 
			if(costLimit >= res.newCostLimit) {
				return false;
			}
			
			//Increase cost limit for the next search
			costLimit = res.newCostLimit;
		}
	}
	
	
	/**
	 * Recursive helper function for IDA* algorithm that searches for a path between two vertices with a given cost limit
	 * 
	 * @param source the name of the vertex to begin
	 * @param destination the name of the destination vertex
	 * @param parent the name of the source vertex's parent
	 * @param currentCost the cost to reach the source vertex from the vertex that the algorithm began
	 * @param predictions a HashMap containing the predictions for each road's traffic
	 * @return an IDAResult object with information about the result of the search
	 * 
	 */
	public static IDAResult IDA_recursive(String source, String destination, String parent, double currentCost, HashMap<String, Double> predictions, double costLimit) {
		
		//Initialize an IDAResult object 
		IDAResult res = new IDAResult();
		
		//Initialize a priority queue to hold the vertices to be checked 
		PriorityQueue<IDANode> minHeap = new PriorityQueue<IDANode>();
		
		//Check if we reached the destination
		if(source.equals(destination)) {
			
			//Update the IDAResult object to signify a finished search
			res.finished = true;
			
			//Update the IDAResult object to hold the total cost of the path
			res.totalPathCost = currentCost;
			
			//Return the IDAResult object
			return res;
		}
		
		//Retrieve the Vertex object holding the information about the source vertex
		Vertex sourceVertex = graph.get(source);
		
		//Check if the total cost exceeds the cost limit
		if(currentCost + sourceVertex.minCostToDest > costLimit) {
			
			//Update the IDAResult object to hold the new cost limit and return it
			res.newCostLimit = currentCost + sourceVertex.minCostToDest;
			return res;
		}
		
		//Loop for each road beginning from the source vertex
		for(Road r : sourceVertex.roads) {
			
			//Ignore the road that led to source vertex from its parent
			if(parent != null && parent.equals(r.leadsTo)) {
				continue;
			}
			
			//Find the predicted cost to traverse the particular road
			double roadCost = predictions.get(r.name) * r.cost;
			
			//Find the minimum cost of the child vertex that this road leads to
			double minCost = graph.get(r.leadsTo).minCostToDest;
			
			//Create a new IDANode object for the child vertex and add it to the queue to be examined later
			minHeap.add(new IDANode(r.leadsTo, currentCost, roadCost, minCost, r.name));
		}
		
		//Loop for all objects in the queue
		while(!minHeap.isEmpty()) {
			
			//Retrieve the IDANode with the minimum total cost to the destination
			IDANode minChild = minHeap.remove();
			
			//Search for the destination recursively beginning from that vertex
			IDAResult cres = IDA_recursive(minChild.name, destination, source, minChild.currentCost, predictions, costLimit);
			
			//Check if the search was successful
			if(cres.finished) {
				
				//Update path in the IDAResult object and return it
				if(!cres.path.equals("")) {
					cres.path = " -> " + cres.path;
				}
				cres.path = minChild.path + cres.path;
				return cres;
			}
			
			//Make sure the cost limit that will be used in the next iteration of the algorithm is the lowest limit that will allow the search to expand to new vertices
			if(res.newCostLimit < 0 || cres.newCostLimit < res.newCostLimit) {
				res.newCostLimit = cres.newCostLimit;
			}
		}		
		
		//Return the IDAResult object
		return res;
	}
	
	
	/**
	 * Loads the predicted traffic for each road into a HashMap
	 * 
	 * @param input a Scanner from which to read the information
	 * @return a HashMap with the names of the roads as keys and factors based on the prediction as values (0.9 for low traffic, 1 for normal and 1.25 for heavy)
	 * 
	 */
	public static HashMap<String, Double> loadPredictions(Scanner input){
		
		//Read opening tag for predictions
		if(!input.nextLine().equals("<Predictions>")) {
			System.out.println("Predictions opening tag not found");
			return null;
		}
		
		//Initialize the HashMap that will be returned
		HashMap<String, Double> predictions = new HashMap<String, Double>();
		
		//Read the first line
		String n = input.nextLine();
		
		//Loop until the closing tag is found
		while(!n.equals("</Predictions>")) {
			
			//Split the line on the semicolon
			String[] v = n.split(";");
			
			//The split should produce exactly two elements: the road name and the prediction
			if(v.length != 2) {
				System.out.println("Error while reading predictions");
				return null;
			}
			
			//Set the value of the factor based on the prediction which is the second element of the array
			double factor;
			if(v[1].strip().equals("low")) {
				factor = 0.9; //low traffic means 10% reduction of the road cost
			}else if(v[1].strip().equals("normal")) {
				factor = 1; //normal traffic means no change to the road cost
			}else {
				factor = 1.25; //heavy traffic means 25% increase to the road cost
			}
			
			//Add the key-value pair of the road name and the factor to the HashMap
			predictions.put(v[0].strip(), factor);
			
			//Read the next line
			n = input.nextLine();
		}
			
		//Return the HashMap
		return predictions;
	}
	
	
	/**
	 * Loads the information about roads and vertices into the HashMap graph
	 * 
	 * @param input a Scanner from which to read the information
	 * @return false on error, true otherwise
	 * 
	 */
	public static boolean loadRoadsToGraph(Scanner input) {
		
		//Read opening tag for roads
		if(!input.nextLine().equals("<Roads>")) {
			System.out.println("Roads opening tag not found");
			return false;
		}
		
		//Read first line
		String tmp = input.nextLine();
		
		//Loop until the closing tag is found
		while(!tmp.equals("</Roads>")){
			
			//Split the line on the semicolons
			String[] values = tmp.split(";");
			
			//The split must produce exactly four elements: road name, vertex1 name, vertex2 name, road cost
			if(values.length != 4) {
				System.out.println("Error while reading roads");
				return false;
			}
			
			//Strip any spaces and convert to other data types as needed
			String name = values[0].strip();
			String vertex1 = values[1].strip();
			String vertex2 = values[2].strip();
			int cost = Integer.parseInt(values[3].strip());
			
			//Add the new road information to the graph element for the first vertex
			Vertex vrt1 = graph.get(vertex1);
			if(vrt1 == null) {
				vrt1 = new Vertex(vertex1); //Create new Vertex if required
			}
			vrt1.roads.add(new Road(name, cost, vertex2));
			graph.put(vertex1, vrt1);
			
			//Add the new road information to the graph element for the second vertex
			Vertex vrt2 = graph.get(vertex2);
			if(vrt2 == null) {
				vrt2 = new Vertex(vertex2);
			}
			vrt2.roads.add(new Road(name, cost, vertex1));
			graph.put(vertex2, vrt2);

			//Read next line
			tmp = input.nextLine(); 
		}
		
		//Reading information was successful
		return true;
	}
	
	
	/**
	 * Uses Dijkstra's algorithm and the "low traffic" cost of each road to calculate the minimum cost to travel from each vertex to the destination. 
	 * Stores calculated cost to minCostToDest variable of each vertex. This heuristic is used for IDA* algorithm.
	 * 
	 * @param destination the name of the destination vertex
	 * 
	 */
	public static void calcMinCosts(String destination) {
		
		//A set to hold the roads already visited by the algorithm
		HashSet<String> roadsChecked = new HashSet<String>();
		
		//A queue to hold vertices whose cost was updated
		PriorityQueue<Vertex> vertToCheck = new PriorityQueue<Vertex>();
		
		//Initialize queue with the destination vertex
		vertToCheck.add(graph.get(destination));
		
		//Initialize cost to reach the destination from itself to 0
		graph.get(destination).minCostToDest = 0;
		
		//Loop while the queue is not empty
		while(!vertToCheck.isEmpty()) {
			
			//Retrieve vertex with the least cost
			Vertex v = vertToCheck.poll();
			
			//Loop for each road beginning from current vertex
			for (Road r : v.roads) {
				
				//Check that the road has not been visited yet and mark it as visited
				if(roadsChecked.add(r.name)) {
					
					//Retrieve the vertex the current road leads to
					Vertex w = graph.get(r.leadsTo);
					
					//Check if necessary to update cost of retrieved vertex
					if(w.minCostToDest == -1 || w.minCostToDest > v.minCostToDest + r.cost * 0.9) {
						
						//Update vertex to the cost of current vertex plus the cost of the current road with low traffic
						w.minCostToDest = v.minCostToDest + r.cost * 0.9;
						
						//Add vertex to the queue
						vertToCheck.add(w);
					}
				} 
			}
		}		
	}
	
	
	/**
	 * The main function. It reads the information from standard input and executes UCS and IDA* algorithms.
	 */
	public static void main(String[] args) {
		
		//Initialize a Scanner to read from standard input
		Scanner input=new Scanner(System.in);
		
		//Read the name of the source and the destination vertex
		String tmp=input.nextLine();
		String source=tmp.substring(8,tmp.length()-9);
		tmp=input.nextLine();
		String destination=tmp.substring(13,tmp.length()-14);
		
		//Load the information about roads and vertices
		if(!loadRoadsToGraph(input)) {
			return;
		}
		
		//Load the predictions for the traffic of each road
		HashMap<String,Double> predictions=loadPredictions(input);
		if(predictions==null) {
			return;
		}
		
		//Finished reading from input
		input.close();
				
		//Calculate heuristic function for IDA* algorithm
		calcMinCosts(destination);
	
		//Execute UCS and IDA
		if(!UCS(source, destination, predictions) || !IDA(source, destination, predictions)) {
			System.out.println("Cannot find path from " + source + " to " + destination);
		}
	}		

}