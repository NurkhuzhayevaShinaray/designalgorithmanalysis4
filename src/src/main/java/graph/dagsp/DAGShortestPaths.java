package graph.dagsp;

import graph.Graph;
import util.Metrics;
import java.util.*;

public class DAGShortestPaths {
    private final Graph g;
    private final Metrics metrics;
    private int countRelax=0;
    public DAGShortestPaths(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public double[] shortestFrom(int src, List<Integer> topoOrder) {
        int n = g.n();
        double INF = Double.POSITIVE_INFINITY;
        double[] dist = new double[n];
        Arrays.fill(dist, INF);
        dist[src] = 0.0;

        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (Graph.Edge e : g.adj(u)) {
                metrics.countRelax();
                if (dist[e.v] > dist[u] + e.w) {
                    dist[e.v] = dist[u] + e.w;
                }
            }
        }
        return dist;
    }

    public int[] reconstructPath(int src, int target, List<Integer> topoOrder) {
        int n = g.n();
        double INF = Double.POSITIVE_INFINITY;
        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[src] = 0.0;
        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (Graph.Edge e : g.adj(u)) {
                metrics.countRelax();
                if (dist[e.v] > dist[u] + e.w) {
                    dist[e.v] = dist[u] + e.w;
                    parent[e.v] = u;
                }
            }
        }

        if (dist[target] == INF) return new int[0];
        List<Integer> path = new ArrayList<>();
        for (int cur = target; cur != -1; cur = parent[cur]) path.add(cur);
        Collections.reverse(path);
        return path.stream().mapToInt(i->i).toArray();
    }
}
