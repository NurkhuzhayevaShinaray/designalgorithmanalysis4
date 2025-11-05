package graph.scc;

import graph.Graph;

import java.util.*;

public class Condensation {
    private final Graph original;
    private final List<List<Integer>> comps;
    private final Graph condGraph;
    private final int[] compId;

    public Condensation(Graph original, List<List<Integer>> components) {
        this.original = original;
        this.comps = components;
        int k = components.size();
        compId = new int[original.n()];
        for (int i = 0; i < k; i++) {
            for (int v : components.get(i)) compId[v] = i;
        }
        condGraph = new Graph(k);
        // add edges between components (unique)
        Set<Long> seen = new HashSet<>();
        for (int u = 0; u < original.n(); u++) {
            int cu = compId[u];
            for (Graph.Edge e : original.adj(u)) {
                int cv = compId[e.v];
                if (cu != cv) {
                    long key = ((long)cu << 32) | (cv & 0xffffffffL);
                    if (!seen.contains(key)) {
                        condGraph.addEdge(cu, cv);
                        seen.add(key);
                    }
                }
            }
        }
    }

    public Graph condensation() { return condGraph; }
    public int[] componentId() { return compId; }
}
