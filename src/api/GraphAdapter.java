package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GraphAdapter implements JsonSerializer<directed_weighted_graph>, JsonDeserializer<directed_weighted_graph> {
    NodeDataAdapter nodeDataAdapter = new NodeDataAdapter();
    EdgeDataAdapter edgeDataAdapter = new EdgeDataAdapter();

    @Override
    public JsonElement serialize(directed_weighted_graph graph, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray edgesArray = new JsonArray();
        JsonArray nodesArray = new JsonArray();
        for (node_data node : graph.getV()) {
            nodesArray.add(nodeDataAdapter.serialize(node, type, jsonSerializationContext));
            for (edge_data edge : graph.getE(node.getKey())) {
                edgesArray.add(edgeDataAdapter.serialize(edge, type, jsonSerializationContext));
            }
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Edges", edgesArray);
        jsonObject.add("Nodes", nodesArray);
        return jsonObject;
    }

    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        directed_weighted_graph graph = new DWGraph_DS();

        JsonArray nodesArray = jsonElement.getAsJsonObject().getAsJsonArray("Nodes");
        JsonArray edgesArray = jsonElement.getAsJsonObject().getAsJsonArray("Edges");
        for (JsonElement na : nodesArray) {
            graph.addNode(nodeDataAdapter.deserialize(na, type, jsonDeserializationContext));
        }
        for (JsonElement ea : edgesArray) {
            int src = ea.getAsJsonObject().get("src").getAsInt();
            int dest = ea.getAsJsonObject().get("dest").getAsInt();
            double w = ea.getAsJsonObject().get("w").getAsDouble();
            graph.connect(src, dest, w);
        }
        return graph;
    }
}
