/** @file IDANode.java */

/**
 * Helper class holding information about a vertex to be examined during IDA* algorithm
 */
public class IDANode implements Comparable<IDANode> {
	public String name; /**< the name of the vertex */
	public String path; /**< the name of the road that led to this vertex along with its cost */
	public double totalEstCost; /**< the sum of the cost to reach this vertex from the source and the minimum cost to reach the destination from this vertex */
	public double currentCost; /**< the cost to reach this vertex from the source */
	
	public IDANode(String name,double currentCost, double roadCost, double minCost, String roadName) {
		this.name=name;
		roadCost=Math.round(roadCost*100)/100.0; //round to 2 decimals
		this.totalEstCost=currentCost+roadCost+minCost;
		this.currentCost=currentCost+roadCost;
		this.path=roadName+"("+roadCost+")";
	} 
	
	public int compareTo(IDANode i) {
		//Multiply with 10 to make sure that a small difference between the costs will not be ignored due to the casting to int
		return (int)((this.totalEstCost - i.totalEstCost) * 10);
	}
}
