package graph.scc;

import graph.Graph;
import util.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TarjanSCC {
    private final Graph g;
    private final int n;
    private int time = 0;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Stack<Integer> stack;
    private final List<List<Integer>> components = new ArrayList<>();
    private final Metrics metrics;

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = g;
        this.n = g.n();
        this.metrics = metrics;
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        for (int i = 0; i < n; i++) disc[i] = -1;
        for (int v = 0; v < n; v++) if (disc[v] == -1) dfs(v);
    }

    private void dfs(int v) {
        disc[v] = low[v] = time++;
        stack.push(v);
        onStack[v] = true;
        metrics.countVisit();
        for (Graph.Edge e : g.adj(v)) {
            metrics.countEdgeVisit();
            int w = e.v;
            if (disc[w] == -1) {
                dfs(w);
                low[v] = Math.min(low[v], low[w]);
            } else if (onStack[w]) {
                low[v] = Math.min(low[v], disc[w]);
            }
        }
        if (low[v] == disc[v]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                if (w == v) break;
            }
            components.add(comp);
        }
    }

    public List<List<Integer>> getComponents() { return components; }
}
