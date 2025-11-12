package graph.dagsp;

import graph.Graph;
import util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {
    @Test
    public void testShortestAndLongest() {
        Graph g = new Graph(5);
        g.addEdge(0,1,2);
        g.addEdge(0,2,3);
        g.addEdge(1,3,4);
        g.addEdge(2,3,1);
        g.addEdge(3,4,5);
        Metrics m = new Metrics();

        List<Integer> topo = List.of(0,1,2,3,4);
        DAGShortestPaths dsp = new DAGShortestPaths(g, m);
        double[] d = dsp.shortestFrom(0, topo);
        assertEquals(0.0, d[0]);
        assertEquals(2.0, d[1]);
        assertEquals(3.0, d[2]);
        assertEquals(4.0, d[3], 1e-6);

        DAGLongestPath lg = new DAGLongestPath(g, m);
        double[] l = lg.longestFrom(0, topo);
        assertEquals(0.0, l[0]);

        assertEquals(6.0, l[3], 1e-6);
    }
}
