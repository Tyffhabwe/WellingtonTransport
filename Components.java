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
        //System.out.println("Sub graph id starting from 0: " + componentNum);
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
            System.out.println("Setting " + stop.getName() + " to " + componentNum + ";\n");

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

    /**
     * Helper method to test find components actually works
     * Based off the lecture slides
     * */
    public static void testFindComponents() {
        Stop a = new Stop(0, 0, "A", null);
        Stop b = new Stop(0, 0, "B", null);
        Stop c = new Stop(0, 0, "C", null);
        Stop d = new Stop(0, 0, "D", null);
        Stop e = new Stop(0, 0, "E", null);

        Stop p = new Stop(0, 0, "P", null);
        Stop q = new Stop(0, 0, "Q", null);
        Stop r = new Stop(0, 0, "R", null);
        Stop s = new Stop(0, 0, "S", null);
        Stop t = new Stop(0, 0, "T", null);
        Stop u = new Stop(0, 0, "U", null);

        Edge a_to_b = new Edge(a, b, "bus", null, 0, 0);
        Edge a_to_c = new Edge(a, c, "bus", null, 0, 0);

        Edge b_to_c = new Edge(b, c, "bus", null, 0, 0);

        Edge c_to_d = new Edge(c, d, "bus", null, 0, 0);
        Edge c_to_p = new Edge(c, p, "bus", null, 0, 0);

        Edge d_to_a = new Edge(d, a, "bus", null, 0, 0);
        Edge d_to_e = new Edge(d, e, "bus", null, 0, 0);

        //SECOND HALF
        Edge p_to_s = new Edge(p, s, "bus", null, 0, 0);

        Edge q_to_p = new Edge(q, p, "bus", null, 0, 0);
        Edge q_to_t = new Edge(q, t, "bus", null, 0, 0);

        Edge r_to_q = new Edge(r, q, "bus", null, 0, 0);

        Edge s_to_r = new Edge(s, r, "bus", null, 0, 0);

        Edge t_to_u = new Edge(t, u, "bus", null, 0, 0);

        //Now add the pointers and stuff
        a.addForwardEdge(a_to_b);
        a.addForwardEdge(a_to_c);
        a.addBackwardEdge(d_to_a);

        b.addForwardEdge(b_to_c);
        b.addBackwardEdge(a_to_b);

        c.addForwardEdge(c_to_d);
        c.addForwardEdge(c_to_p);
        c.addBackwardEdge(a_to_c);
        c.addBackwardEdge(b_to_c);

        d.addForwardEdge(d_to_a);
        d.addForwardEdge(d_to_e);
        d.addBackwardEdge(c_to_d);

        e.addBackwardEdge(d_to_e);

        //NEXT HALF
        p.addForwardEdge(p_to_s);
        p.addBackwardEdge(c_to_p);
        p.addBackwardEdge(q_to_p);

        q.addForwardEdge(q_to_p);
        q.addForwardEdge(q_to_t);
        q.addBackwardEdge(r_to_q);

        r.addForwardEdge(r_to_q);
        r.addBackwardEdge(s_to_r);

        s.addForwardEdge(s_to_r);
        s.addBackwardEdge(p_to_s);

        t.addForwardEdge(t_to_u);
        t.addBackwardEdge(q_to_t);

        u.addBackwardEdge(t_to_u);

        List<Stop> testStops = List.of(a,b,c,d,e,p,q,r,s,t,u);
        List<Line> emptyLineList = new ArrayList<>();

        Graph testGraph = new Graph(testStops, emptyLineList);

        findComponents(testGraph);
        System.out.println("Made it here");
    }

}
