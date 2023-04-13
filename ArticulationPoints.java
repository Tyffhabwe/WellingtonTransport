import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

//=============================================================================
//   TODO   Finding Articulation Points
//   Finds all the articulation points in the undirected graph, without walking edges
//   Labels each stop with the number of the subgraph it is in
//   sets the subGraphCount of the graph to the number of subgraphs.
//=============================================================================

public class ArticulationPoints{

    // Based on....

    // Returns the collection of nodes that are articulation points 
    // in the UNDIRECTED graph with no walking edges.
    // 
    public static Collection<Stop> findArticulationPoints(Graph graph) {
        System.out.println("calling findArticulationPoints");
        graph.computeNeighbours();   // To ensure that all stops have a set of (undirected) neighbour stops

        Set<Stop> articulationPoints = new HashSet<Stop>();
        Map<Stop, Integer> nodeDepths = new HashMap<Stop, Integer>();
        List<Stop> allStops = new ArrayList<>(graph.getStops());
        List<Stop> allStopsToFindRoots = new ArrayList<>(graph.getStops());

        for(Stop stop: allStops) {
            nodeDepths.put(stop, Integer.MAX_VALUE);
        }

        Set<Stop> rootNodes = getMeAllOfTheRootNodes(allStopsToFindRoots);

        for(Stop stop: rootNodes) {
            int numSubTrees = 0;
            nodeDepths.put(stop, 0);

            for(Stop neighbour: stop.getNeighbours()) {
                int neighbourDepth = nodeDepths.get(neighbour);
                if(neighbourDepth == Integer.MAX_VALUE) {
                    recursiveArtPts(neighbour, 1, stop, articulationPoints, nodeDepths);
                    numSubTrees++;
                }
            }
            if(numSubTrees > 1) { articulationPoints.add(stop); }
        }

        System.out.println("Articulation points size: " + articulationPoints.size());
        return articulationPoints;

    }

    public static int recursiveArtPts(Stop stop, int depth, Stop fromStop, Set<Stop> articulationPoints,
                                      Map<Stop, Integer> nodeDepths) {
        nodeDepths.put(stop, depth);
        int reachBack = depth;

        for(Stop neighbour: stop.getNeighbours()) {
            if (neighbour.equals(fromStop)) continue;
            else if (nodeDepths.get(neighbour) != Integer.MAX_VALUE) {
                reachBack = Math.min(nodeDepths.get(neighbour), reachBack);
            }
            else {
                int childReach = recursiveArtPts(neighbour, depth + 1, stop, articulationPoints, nodeDepths);
                if(childReach >= depth) {
                    articulationPoints.add(stop);
                }
                reachBack = Math.min(childReach, reachBack);
            }
        }
        return reachBack;
    }

    public static Set<Stop> getMeAllOfTheRootNodes(List<Stop> allStopsToFindRoots) {
        Set<Stop> ans = new HashSet<>();
        while(!allStopsToFindRoots.isEmpty()) {
            Stop oneRoot = allStopsToFindRoots.get(0);
            ans.add(oneRoot);
            traverseAndRemoveAllConnectedStops(oneRoot, new HashSet<>(), allStopsToFindRoots);
        }

        return ans;
    }

    public static void traverseAndRemoveAllConnectedStops(Stop node, Set<Stop> visited, List<Stop> allStopsToFindRoots) {
        if(!visited.contains(node)) {
            visited.add(node);
            allStopsToFindRoots.remove(node);

            for(Stop neighbour: node.getNeighbours()) {
                if(!visited.contains(neighbour)) {
                    traverseAndRemoveAllConnectedStops(neighbour, visited, allStopsToFindRoots);
                }
            }

        }
    }








}
