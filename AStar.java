/**
 * Implements the A* search algorithm to find the shortest path
 * in a graph between a start node and a goal node.
 * It returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

/** public static List<Edge> findShortestPath(Stop start, Stop goal, String timeOrDistance) {
 if (start == null || goal == null) {return null;}
 timeOrDistance= (timeOrDistance.equals("time"))?"time":"distance";
 */

import java.util.*;


public class AStar {
    private static String timeOrDistance = "distance";    // way of calculating cost: "time" or "distance"

    // find the shortest path between two stops
    public static List<Edge> findShortestPath(Stop start, Stop goal, String tOrD) {
       if (start == null || goal == null) {return null;}
       timeOrDistance= (tOrD.equals("time"))?"time":"distance";

        Map<Stop, Edge> backPointers = new HashMap<Stop, Edge>();
        Queue<PathItem> fringe = new PriorityQueue<PathItem>();
        Set<Stop> visited = new HashSet<Stop>();

        Stop nowhere = new Stop(0, 0, "NOWHERE", null);
        Edge startingEdge = new Edge(nowhere, start, "START", null, 0, 0);
        double startingTotalCost = heuristic(start, goal);
        PathItem startItem = new PathItem(start, startingEdge, 0, startingTotalCost);
        fringe.add(startItem);

        int count = 0;
        while (!fringe.isEmpty()) {
            PathItem currentItem = fringe.poll();
            count++;
            Stop currentStop = currentItem.stop();

            if(!visited.contains(currentStop)) {
                visited.add(currentStop);
                backPointers.put(currentStop, currentItem.backPointer());

                if(currentStop.equals(goal)) {
                    return reconstructPath(start, goal, backPointers);
                }

                for(Edge nextNode: currentStop.getForwardEdges()) {
                    Stop neighbourNode = nextNode.toStop();

                    if(!visited.contains(neighbourNode)) {
                        double lengthToNeighbour = edgeCost(nextNode);

                        //I wanna add a cost to paths that are on a different edge

                        double heuristicValue = heuristic(neighbourNode, goal);

                        double costSoFar = lengthToNeighbour + currentItem.costSoFar();
                        double totalCost = costSoFar + heuristicValue;

                        PathItem newItem = new PathItem(neighbourNode, nextNode, costSoFar, totalCost);
                        fringe.add(newItem);
                    }
                }
            }
        }
        //TODO: THROW A EXCEPTION HERE INSTEAD?
        return null;
    }

    /** Return the heuristic estimate of the cost to get from a stop to the goal */
    public static double heuristic(Stop current, Stop goal) {
        if (timeOrDistance=="distance"){ return current.distanceTo(goal);}
        else if (timeOrDistance=="time"){return current.distanceTo(goal) / Transport.TRAIN_SPEED_MPS;}
        else {return 0;}
    }

    /** Return the cost of traversing an edge in the graph */
    public static double edgeCost(Edge edge){
        if (timeOrDistance=="distance"){ return edge.distance();}
        else if (timeOrDistance=="time"){return edge.time();}
        else {return 1;}
    }

    /** Return a list of edges that is the path reconstructed */
    public static List<Edge> reconstructPath(Stop start, Stop end, Map<Stop, Edge> backPointers) {
        ArrayList<Edge> ans = new ArrayList<Edge>();
        Edge endEdge = backPointers.get(end);
        Stop nextStopToLookUpInBackPointers = backPointers.get(end).fromStop();
        Edge nextEdgeToLookUp = backPointers.get(nextStopToLookUpInBackPointers);

        ans.add(endEdge);

        //while(!nextEdgeToLookUp.transpType().equals("START")) {
        while(nextEdgeToLookUp != null) {

            if(!nextEdgeToLookUp.transpType().equals("START")) {
                ans.add(nextEdgeToLookUp);
            }
            Stop nextStop = nextEdgeToLookUp.fromStop();
            nextEdgeToLookUp = backPointers.get(nextStop);


        }

        Collections.reverse(ans);
        return ans;
    }
}
