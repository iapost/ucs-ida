/*
 * Helper class holding information about a Vertex
 */
import java.util.Vector;

public class Vertex {
	public String name; //the name of the vertex
	public Vector<Road> roads; //a vector holding all roads that begin from this vertex
	public double minCostToDest; //minimum cost to destination, heuristic calculated for IDA* algorithm 
	
	public Vertex(String name) {
		this.name=name;
		this.roads=new Vector<Road>();
		this.minCostToDest=-1;
	}
}
