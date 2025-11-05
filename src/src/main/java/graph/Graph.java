package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final List<Edge>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int n) {
        this.n = n;
        adj = (List<Edge>[]) new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
    }

    public int n() { return n; }

    public void addEdge(int u, int v, double w) {
        adj[u].add(new Edge(u, v, w));
    }

    public void addEdge(int u, int v) { addEdge(u, v, 1.0); }

    public List<Edge> adj(int u) { return adj[u]; }

    public Iterable<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.addAll(adj[i]);
        return list;
    }

    public static class Edge {
        public final int u, v;
        public final double w;
        public Edge(int u, int v, double w) { this.u = u; this.v = v; this.w = w; }
        @Override public String toString() { return String.format("%d->%d(%.2f)", u, v, w); }
    }
}

