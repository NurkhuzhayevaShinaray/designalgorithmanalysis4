package graph.topo;

import graph.Graph;
import util.Metrics;

import java.util.*;

public class KahnTopo {
    private final Graph g;
    private final Metrics metrics;

    public KahnTopo(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<Integer> topoOrder() {
        int n = g.n();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (graph.Graph.Edge e : g.adj(u)) {
                indeg[e.v]++;
            }
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.remove();
            order.add(u);
            metrics.countKahnPop();
            for (graph.Graph.Edge e : g.adj(u)) {
                metrics.countKahnEdge();
                indeg[e.v]--;
                if (indeg[e.v] == 0) q.add(e.v);
            }
        }
        if (order.size() != n) return Collections.emptyList(); // cycle
        return order;
    }
}
