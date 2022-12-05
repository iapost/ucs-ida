/*
 * Helper class holding information to be returned from a call of the IDA_recursive function in Main class
 */
public class IDAResult {
	public boolean finished; //true if the destination was reached, otherwise false
	public double newCostLimit; //the new cost limit for the next iteration of IDA*
	public String path; //the path found by IDA*
	public double totalPathCost; //the total cost of the path found by IDA*
	
	public IDAResult() {
		this.finished=false;
		this.newCostLimit=-1;
		this.path="";
		this.totalPathCost=0;
	}
}
