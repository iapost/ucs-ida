/** @file Road.java */

/*
 * Helper class holding information about a road
 */
public class Road {
	public String name; /**< the name of the road */
	public int cost; /**< the cost to traverse the road with normal traffic */
	public String leadsTo; /**< the vertex this road leads to */
	
	public Road(String name,int cost, String leadsTo) {
		this.name=name;
		this.cost=cost;
		this.leadsTo=leadsTo;
	}
}
