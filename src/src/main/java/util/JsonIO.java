package util;

import graph.Graph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;

public class JsonIO {

    public static Graph loadGraph(String path) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(new FileReader(path));
        long n = (Long) root.get("n");
        Graph g = new Graph((int)n);
        JSONArray edges = (JSONArray) root.get("edges");
        for (Object o : edges) {
            JSONObject e = (JSONObject) o;
            int u = ((Number)e.get("u")).intValue();
            int v = ((Number)e.get("v")).intValue();
            double w = e.containsKey("w") ? ((Number)e.get("w")).doubleValue() : 1.0;
            g.addEdge(u, v, w);
        }
        return g;
    }

    public static void writeReport(String path, String content) throws Exception {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
        }
    }
}
