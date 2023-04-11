/**
 * Implements the A* search algorithm to find the shortest path
 * in a graph between a start node and a goal node.
 * It returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class AStar {
    private static String timeOrDistance = "distance";    // way of calculating cost: "time" or "distance"

    // find the shortest path between two stops
    public static List<Edge> findShortestPath(Stop start, Stop goal, String timeOrDistance) {
        if (start == null || goal == null) {return null;}
        timeOrDistance= (timeOrDistance.equals("time"))?"time":"distance";

        return null;   // fix this!!!
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

        while(!nextEdgeToLookUp.transpType().equals("START")) {
            ans.add(nextEdgeToLookUp);

            Stop nextStop = nextEdgeToLookUp.fromStop();
            nextEdgeToLookUp = backPointers.get(nextStop);

        }

        Collections.reverse(ans);
        return ans;
    }

    public static void testReconstructPath() {

        Stop nowhere = new Stop(0, 0, "NOWHERE", null);
        Stop s = new Stop(0, 0, "S", null);
        Stop b = new Stop(0, 0, "B", null);
        Stop c = new Stop(0, 0, "C", null);
        Stop e = new Stop(0, 0, "E", null);
        Stop g = new Stop(0, 0, "G", null);


        Edge startingEdge = new Edge(nowhere, s, "START", null, 0, 0);
        Edge s_to_b = new Edge(s, b, "bus", null, 0, 0);
        Edge s_to_c = new Edge(s, c, "bus", null, 0, 0);
        Edge c_to_e = new Edge(c, e, "bus", null, 0, 0);
        Edge e_to_g = new Edge(e, g, "bus", null, 0, 0);

        Map<Stop, Edge> backPointers = Map.of(
                s, startingEdge,
                b, s_to_b,
                c, s_to_c,
                e, c_to_e,
                g, e_to_g
        );

        List<Edge> reconstructedPath = reconstructPath(s, g, backPointers);
    }



}
