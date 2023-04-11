/**
 * Implements the A* search algorithm to find the shortest path
 * in a graph between a start node and a goal node.
 * It returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.*;


public class AStar {
    private static String timeOrDistance = "distance";    // way of calculating cost: "time" or "distance"

    // find the shortest path between two stops
    public static List<Edge> findShortestPath(Stop start, Stop goal, String timeOrDistance) {
        if (start == null || goal == null) {return null;}
        timeOrDistance= (timeOrDistance.equals("time"))?"time":"distance";

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
                    System.out.println("COUNT OF FRINGE: " + count);
                    return reconstructPath(start, goal, backPointers);
                }

                for(Edge nextNode: currentStop.getForwardEdges()) {
                    Stop neighbourNode = nextNode.toStop();

                    if(!visited.contains(neighbourNode)) {
                        double lengthToNeighbour = edgeCost(nextNode);
                        double heuristicValue = heuristic(neighbourNode, goal);

                        double costSoFar = lengthToNeighbour + currentItem.costSoFar();
                        double totalCost = costSoFar + heuristicValue;

                        PathItem newItem = new PathItem(neighbourNode, nextNode, costSoFar, totalCost);
                        fringe.add(newItem);
                    }
                }
            }
        }
        //TODO: THOW A EXCEPTION HERE INSTEAD?
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

    public static void testAStarAlgorithm() {

        //Create the stops based off 2018 exam question
        Stop s = new Stop(2.24, 2.24, "S", null);
        Stop b = new Stop(0, 3, "B", null);
        Stop c = new Stop(1.4, 1.4, "C", null);
        Stop d = new Stop(1, 0, "D", null);
        Stop e = new Stop(0, 1, "E", null);
        Stop g = new Stop(0, 0, "G", null);

        Edge s_to_c = new Edge(s, c, "bus", null, 2, 4);
        Edge s_to_b = new Edge(s, b, "bus", null, 1.5, 3);

        Edge b_to_s = new Edge(b, s, "bus", null, 1.5, 3);
        Edge b_to_d = new Edge(b, d, "bus", null, 3, 6);

        Edge c_to_s = new Edge(c, s, "bus", null, 2, 4);
        Edge c_to_e = new Edge(c, e, "bus", null, 1, 2);
        Edge c_to_d = new Edge(c, d, "bus", null, 1.5, 3);

        Edge d_to_b = new Edge(d, b, "bus", null, 3, 6);
        Edge d_to_c = new Edge(d, c, "bus", null, 1.5, 3);
        Edge d_to_g = new Edge(d, g, "bus", null, 1, 2);

        Edge e_to_c = new Edge(e, c, "bus", null, 1, 2);
        Edge e_to_g = new Edge(b, d, "bus", null, 2, 4);

        s.addForwardEdge(s_to_c);
        s.addForwardEdge(s_to_b);

        b.addForwardEdge(b_to_s);
        b.addForwardEdge(b_to_d);

        c.addForwardEdge(c_to_s);
        c.addForwardEdge(c_to_e);
        c.addForwardEdge(c_to_d);

        d.addForwardEdge(d_to_b);
        d.addForwardEdge(d_to_c);
        d.addForwardEdge(d_to_g);

        e.addForwardEdge(e_to_c);
        e.addForwardEdge(e_to_g);

        List<Edge> shortestPath = findShortestPath(s, g, "distance");
        System.out.println("WE HERE!");
    }



}
