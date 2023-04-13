import java.util.*;

//=============================================================================
//   TODO   Finding Components
//   Finds all the strongly connected subgraphs in the graph
//   Labels each stop with the number of the subgraph it is in
//   sets the subGraphCount of the graph to the number of subgraphs.
//   Uses Kosaraju's_algorithm   (see lecture slides, based on
//   https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm)
//=============================================================================

public class Components{

    // Use a visited set to record which stops have been visited
    // If using Kosaraju's, us a Map<Stop,Stop> to record the root node of each stop.

    
    public static void findComponents(Graph graph) {
        System.out.println("calling findComponents");
        graph.resetSubGraphIds();

        int componentNum = 0;
        List<Stop> nodeList = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();

        for(Stop stop: graph.getStops()) {
            if(!visited.contains(stop)) {
                forwardVisit(stop, nodeList, visited);
            }
        }

        Collections.reverse(nodeList);

        for(Stop stop: nodeList) {
            if(stop.getSubGraphId() == -1) {
                backwardVisit(stop, componentNum);
                componentNum++;
            }
        }
        graph.setSubGraphCount(componentNum);
    }

    /** Helper method to forward visit the nodes in algorithm */
    public static void forwardVisit(Stop stop, List<Stop> nodeList, Set<Stop> visited) {
        if(!visited.contains(stop)) {
            visited.add(stop);

            Collection<Edge> outedges = stop.getForwardEdges();
            List<Stop> outwardStops = new ArrayList<>();

            for(Edge edge: outedges) {
                outwardStops.add(edge.toStop());
            }

            for(Stop neighbour: outwardStops) {
                forwardVisit(neighbour, nodeList, visited);
            }
            nodeList.add(stop);
        }
    }

    /** Helper method to backward visit the nodes in algorithm */
    public static void backwardVisit(Stop stop, int componentNum) {
        if(stop.getSubGraphId() == -1) {
            stop.setSubGraphId(componentNum);

            Collection<Edge> backEdges = stop.getBackwardEdges();
            List<Stop> backwardStops = new ArrayList<>();

            for(Edge edge: backEdges) {
                backwardStops.add(edge.fromStop());
            }

            for(Stop neighbour: backwardStops) {
                backwardVisit(neighbour, componentNum);
            }
        }
    }



}
