package app;

import graph.Graph;
import graph.scc.TarjanSCC;
import graph.scc.Condensation;
import graph.topo.KahnTopo;
import graph.dagsp.DAGShortestPaths;
import graph.dagsp.DAGLongestPath;
import util.JsonIO;
import util.Metrics;

import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String dataPath = "data/dataset_small_1.json"; // change to chosen dataset
        System.out.println("Loading graph: " + dataPath);
        Graph g = JsonIO.loadGraph(dataPath);

        Metrics metrics = new Metrics();

        // 1. SCC
        metrics.startTimer();
        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        metrics.stopTimer();
        List<List<Integer>> comps = tarjan.getComponents();
        System.out.println("SCC count: " + comps.size());
        System.out.println("SCC sizes: ");
        for (List<Integer> c : comps) System.out.println(c.size() + " -> " + c);
        System.out.printf("SCC time: %.3f ms, visits=%d, edgeVisits=%d%n",
                metrics.elapsedMs(), metrics.getSccVisits(), metrics.getSccEdgeVisits());

        // 2. Condensation + Topo
        Condensation cond = new Condensation(g, comps);
        Graph cg = cond.condensation();
        Metrics metrics2 = new Metrics();
        KahnTopo topo = new KahnTopo(cg, metrics2);
        metrics2.startTimer();
        List<Integer> order = topo.topoOrder();
        metrics2.stopTimer();
        System.out.println("Condensation nodes: " + cg.n());
        System.out.println("Topological order (components): " + order);
        System.out.printf("Topo time: %.3f ms, pops=%d, edges=%d%n",
                metrics2.elapsedMs(), metrics2.getKahnPop(), metrics2.getKahnEdge());

        // 3. DAG shortest and longest (use condensation DAG)
        // pick source component 0
        if (!order.isEmpty()) {
            int src = order.get(0);
            Metrics metrics3 = new Metrics();
            DAGShortestPaths dsp = new DAGShortestPaths(cg, metrics3);
            metrics3.startTimer();
            double[] dist = dsp.shortestFrom(src, order);
            metrics3.stopTimer();
            System.out.printf("DAG-SP time: %.3f ms, relax=%d%n", metrics3.elapsedMs(), metrics3.getRelaxations());
            System.out.println("Shortest dists from src component " + src + ":");
            for (int i = 0; i < dist.length; i++) System.out.printf("%d: %s%n", i, (Double.isInfinite(dist[i])?"INF":String.format("%.2f",dist[i])));

            Metrics metrics4 = new Metrics();
            DAGLongestPath longp = new DAGLongestPath(cg, metrics4);
            metrics4.startTimer();
            double[] best = longp.longestFrom(src, order);
            metrics4.stopTimer();
            System.out.printf("DAG-Longest time: %.3f ms, relax=%d%n", metrics4.elapsedMs(), metrics4.getRelaxations());
            System.out.println("Longest dists from src component " + src + ":");
            for (int i = 0; i < best.length; i++) System.out.printf("%d: %s%n", i, (Double.isInfinite(best[i])?"-INF":String.format("%.2f",best[i])));
        }
    }
}
