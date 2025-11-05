package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.Random;

public class DatasetGenerator {
    private final Random rnd = new Random(42);

    public void generate(String path, int n, double density, boolean weighted, boolean forceCycle) throws Exception {
        JSONObject root = new JSONObject();
        root.put("n", n);
        JSONArray edges = new JSONArray();
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (u == v) continue;
                if (rnd.nextDouble() < density) {
                    JSONObject e = new JSONObject();
                    e.put("u", u);
                    e.put("v", v);
                    if (weighted) e.put("w", 1 + rnd.nextInt(10));
                    edges.add(e);
                }
            }
        }
        // optionally force a cycle by connecting chain
        if (forceCycle && n >= 3) {
            JSONObject e = new JSONObject();
            e.put("u", 0);
            e.put("v", n-1);
            if (weighted) e.put("w", 1);
            edges.add(e);
        }
        root.put("edges", edges);
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(root.toJSONString());
        }
    }
}
