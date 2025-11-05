package util;

public class Metrics {
    private long sccVisits = 0;
    private long sccEdgeVisits = 0;
    private long kahnPop = 0;
    private long kahnEdge = 0;
    private long relaxations = 0;
    private long startNs = 0;
    private long endNs = 0;

    public void startTimer() { startNs = System.nanoTime(); }
    public void stopTimer() { endNs = System.nanoTime(); }
    public double elapsedMs() { return (endNs - startNs)/1_000_000.0; }

    public void countVisit() { sccVisits++; }
    public void countEdgeVisit() { sccEdgeVisits++; }
    public void countKahnPop() { kahnPop++; }
    public void countKahnEdge() { kahnEdge++; }
    public void countRelax() { relaxations++; }

    public long getSccVisits() { return sccVisits; }
    public long getSccEdgeVisits() { return sccEdgeVisits; }
    public long getKahnPop() { return kahnPop; }
    public long getKahnEdge() { return kahnEdge; }
    public long getRelaxations() { return relaxations; }
}
