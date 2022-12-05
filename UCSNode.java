/*
 * Helper class to hold information about a vertex to be examined during UCS algorithm
 */
public class UCSNode implements Comparable<UCSNode> {
	public String nodeName; //the name of the vertex
	public double currentCost; //the cost to reach this vertex from the source
	public String path; //the path to reach this vertex from the source
	
	public UCSNode(String nodeName, double currentCost) {
		this.nodeName = nodeName;
		this.currentCost = currentCost; 
		this.path = "";
	}

	//Used in PriorityQueue holding UCSNode objects. The UCSNode with less cost is given priority.
	public int compareTo(UCSNode node) {
		//Multiply with 10 to make sure that a small difference between the costs will not be ignored due to the casting to int
        return (int)((this.currentCost - node.currentCost) * 10);
    }
	

}
