package java.graph.scc;

import graph.Graph;
import graph.scc.TarjanSCC;
import util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    public void testSimpleSCC() {
        Graph g = new Graph(5);
        g.addEdge(0,1);
        g.addEdge(1,2);
        g.addEdge(2,0);
        g.addEdge(3,4);
        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g, m);
        List<List<Integer>> comps = t.getComponents();
        assertTrue(comps.size() >= 2);
        boolean foundCycle = comps.stream().anyMatch(c -> c.size() == 3);
        assertTrue(foundCycle);
    }
}
