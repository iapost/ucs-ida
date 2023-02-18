/** @file Vertex.java */

/*
 * Helper class holding information about a Vertex
 */
import java.util.Vector;

public class Vertex implements Comparable<Vertex> {
	public String name; /**< the name of the vertex */
	public Vector<Road> roads; /**< a vector holding all roads that begin from this vertex */
	public double minCostToDest; /**< minimum cost to destination, heuristic calculated for IDA* algorithm */
	
	public Vertex(String name) {
		this.name=name;
		this.roads=new Vector<Road>();
		this.minCostToDest=-1;
	}
	
	public int compareTo(Vertex v) {
		//The Vertex with less cost is given priority.
		//Multiply with 10 to make sure that a small difference between the costs will not be ignored due to the casting to int
		return (int)((this.minCostToDest - v.minCostToDest) * 10);
	}
}
