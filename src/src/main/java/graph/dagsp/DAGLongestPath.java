package graph.dagsp;

import graph.Graph;
import util.Metrics;

import java.util.*;


public class DAGLongestPath {
    private final Graph g;
    private final Metrics metrics;

    public DAGLongestPath(Graph g, Metrics metrics) {
        this.g = g; this.metrics = metrics;
    }

    public double[] longestFrom(int src, List<Integer> topoOrder) {
        int n = g.n();
        double NEG_INF = Double.NEGATIVE_INFINITY;
        double[] best = new double[n];
        Arrays.fill(best, NEG_INF);
        best[src] = 0.0;
        for (int u : topoOrder) {
            if (best[u] == NEG_INF) continue;
            for (Graph.Edge e : g.adj(u)) {
                metrics.countRelax();
                if (best[e.v] < best[u] + e.w) {
                    best[e.v] = best[u] + e.w;
                }
            }
        }
        return best;
    }

    public int[] reconstructLongest(int src, int target, List<Integer> topoOrder) {
        int n = g.n();
        double NEG_INF = Double.NEGATIVE_INFINITY;
        double[] best = new double[n];
        int[] parent = new int[n];
        Arrays.fill(best, NEG_INF);
        Arrays.fill(parent, -1);
        best[src] = 0.0;
        for (int u : topoOrder) {
            if (best[u] == NEG_INF) continue;
            for (Graph.Edge e : g.adj(u)) {
                metrics.countRelax();
                if (best[e.v] < best[u] + e.w) {
                    best[e.v] = best[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }
        if (best[target] == NEG_INF) return new int[0];
        List<Integer> path = new ArrayList<>();
        for (int cur = target; cur != -1; cur = parent[cur]) path.add(cur);
        Collections.reverse(path);
        return path.stream().mapToInt(i->i).toArray();
    }
}
