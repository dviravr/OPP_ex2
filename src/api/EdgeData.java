package api;

import com.google.gson.*;

import java.lang.reflect.Type;

//@JsonAdapter(EdgeDataAdapter.class)
public class EdgeData implements edge_data {

   private final int _src;
   private final int _dest;
   private int _tag;
   private String _info;
   private final double _weight;

   EdgeData(int src, int dest, double weight) {
      _src = src;
      _dest = dest;
      _weight = weight;
   }

   public EdgeData(edge_data e) {
//      copy constructor
      _src = e.getSrc();
      _dest = e.getDest();
      _tag = e.getTag();
      _info = e.getInfo();
      _weight = e.getWeight();
   }

   @Override
   public int getSrc() {
      return _src;
   }

   @Override
   public int getDest() {
      return _dest;
   }

   @Override
   public double getWeight() {
      return _weight;
   }

   @Override
   public String getInfo() {
      return _info;
   }

   @Override
   public void setInfo(String s) {
      _info = s;
   }

   @Override
   public int getTag() {
      return _tag;
   }

   @Override
   public void setTag(int t) {
      _tag = t;
   }

   @Override
   public String toString() {
      return "EdgeData{" +
              "src=" + _src +
              ", dest=" + _dest +
              ", tag=" + _tag +
              ", info='" + _info +
              ", weight=" + _weight +
              '}';
   }
}

class EdgeDataAdapter implements JsonSerializer<edge_data>, JsonDeserializer<edge_data> {

   @Override
   public JsonElement serialize(edge_data edge, Type type, JsonSerializationContext jsonSerializationContext) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("src", edge.getSrc());
      jsonObject.addProperty("w", edge.getWeight());
      jsonObject.addProperty("dest", edge.getDest());
      return jsonObject;
   }

   @Override
   public edge_data deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return null;
   }
}
