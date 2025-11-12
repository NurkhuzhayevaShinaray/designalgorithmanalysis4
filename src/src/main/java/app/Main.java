package app;

import graph.Graph;
import graph.scc.TarjanSCC;
import graph.scc.Condensation;
import graph.topo.KahnTopo;
import graph.dagsp.DAGShortestPaths;
import graph.dagsp.DAGLongestPath;
import util.JsonIO;
import util.Metrics;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        // список всех 9 датасетов
        String[] datasets = {
                "dataset_small_1.json",
                "dataset_small_2.json",
                "dataset_small_3.json",
                "dataset_medium_1.json",
                "dataset_medium_2.json",
                "dataset_medium_3.json",
                "dataset_large_1.json",
                "dataset_large_2.json",
                "dataset_large_3.json"
        };

        for (String dataset : datasets) {
            System.out.println("====================================================");
            System.out.println("Processing dataset: " + dataset);
            System.out.println("====================================================");

            String dataPath = "data/" + dataset;
            Graph g = JsonIO.loadGraph(dataPath);

            Metrics metrics = new Metrics();

            // === 1. Strongly Connected Components (Tarjan) ===
            metrics.startTimer();
            TarjanSCC tarjan = new TarjanSCC(g, metrics);
            metrics.stopTimer();
            List<List<Integer>> comps = tarjan.getComponents();
            System.out.println("SCC count: " + comps.size());
            System.out.println("SCC sizes: ");
            for (List<Integer> c : comps) System.out.println(c.size() + " -> " + c);
            System.out.printf("SCC time: %.3f ms, visits=%d, edgeVisits=%d%n",
                    metrics.elapsedMs(), metrics.getSccVisits(), metrics.getSccEdgeVisits());

            // === 2. Condensation + Topological Sorting ===
            Condensation cond = new Condensation(g, comps);
            Graph cg = cond.condensation();
            Metrics metrics2 = new Metrics();
            metrics2.startTimer();
            KahnTopo topo = new KahnTopo(cg, metrics2);
            List<Integer> order = topo.topoOrder();
            metrics2.stopTimer();
            System.out.println("Condensation nodes: " + cg.n());
            System.out.println("Topological order (components): " + order);
            System.out.printf("Topo time: %.3f ms, pops=%d, edges=%d%n",
                    metrics2.elapsedMs(), metrics2.getKahnPop(), metrics2.getKahnEdge());

            // === 3. DAG Shortest & Longest Paths ===
            if (!order.isEmpty()) {
                int src = order.get(0);

                // Shortest Path
                Metrics metrics3 = new Metrics();
                DAGShortestPaths dsp = new DAGShortestPaths(cg, metrics3);
                metrics3.startTimer();
                double[] dist = dsp.shortestFrom(src, order);
                metrics3.stopTimer();
                System.out.printf("DAG-SP time: %.3f ms, relax=%d%n",
                        metrics3.elapsedMs(), metrics3.getRelaxations());
                System.out.println("Shortest distances from src component " + src + ":");
                for (int i = 0; i < dist.length; i++)
                    System.out.printf("%d: %s%n", i, (Double.isInfinite(dist[i]) ? "INF" : String.format("%.2f", dist[i])));

                // Longest Path
                Metrics metrics4 = new Metrics();
                DAGLongestPath longp = new DAGLongestPath(cg, metrics4);
                metrics4.startTimer();
                double[] best = longp.longestFrom(src, order);
                metrics4.stopTimer();
                System.out.printf("DAG-Longest time: %.3f ms, relax=%d%n",
                        metrics4.elapsedMs(), metrics4.getRelaxations());
                System.out.println("Longest distances from src component " + src + ":");
                for (int i = 0; i < best.length; i++)
                    System.out.printf("%d: %s%n", i, (Double.isInfinite(best[i]) ? "-INF" : String.format("%.2f", best[i])));
            }

            System.out.println("Finished processing " + dataset);
            System.out.println();
        }

        System.out.println("All datasets processed successfully!");
    }
}
