/**
 * AStar search (and Dijkstra search) uses a priority queue of partial paths
 * that the search is building.
 * Each partial path needs several pieces of information, to specify
 * the path to that point, its cost so far, and its estimated total cost
 */

public class PathItem implements Comparable<PathItem> {
    private Stop stop;
    private Edge backPointer;
    private double costSoFar;
    private double totalCost;
    public PathItem(Stop stop, Edge backPointer, double costSoFar, double totalCost) {
        this.stop = stop;
        this.backPointer = backPointer;
        this.costSoFar = costSoFar;
        this.totalCost = totalCost;
    }
    public Stop stop() {
        return this.stop;
    }
    public double totalCost() {
        return this.totalCost;
    }
    public double costSoFar() {
        return this.costSoFar;
    }
    public Edge backPointer() {
        return this.backPointer;
    }
    @Override
    public int compareTo(PathItem o) {
        if(this.totalCost() < o.totalCost()) {
            return -1;
        }
        else if(this.totalCost() == o.totalCost()) {
            return 0;
        }
        return 1;
    }

}
