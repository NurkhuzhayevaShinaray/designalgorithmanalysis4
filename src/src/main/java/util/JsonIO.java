package util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import graph.Graph;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

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
