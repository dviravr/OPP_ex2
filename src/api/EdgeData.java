package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

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

   EdgeData(edge_data e) {
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

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      EdgeData edgeData = (EdgeData) o;
      return _src == edgeData._src &&
              _dest == edgeData._dest &&
              _tag == edgeData._tag &&
              Double.compare(edgeData._weight, _weight) == 0 &&
              Objects.equals(_info, edgeData._info);
   }

   @Override
   public int hashCode() {
      return Objects.hash(_src, _dest, _tag, _info, _weight);
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
      int src = jsonElement.getAsJsonObject().get("src").getAsInt();
      int dest = jsonElement.getAsJsonObject().get("dest").getAsInt();
      double w = jsonElement.getAsJsonObject().get("w").getAsDouble();
      return new EdgeData(src, dest, w);
   }
}
