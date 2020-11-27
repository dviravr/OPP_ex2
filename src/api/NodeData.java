package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class NodeData implements node_data {

   private final int _id;
   private int _tag;
   private String _info;
   private double _weight;
   private geo_location _pos;

   NodeData(int id) {
      _id = id;
   }

   NodeData(int id, GeoLocation p) {
      this(id);
      _pos = p;
   }

   NodeData(node_data n) {
//      copy constructor
      _id = n.getKey();
      _tag = n.getTag();
      _info = n.getInfo();
      _weight = n.getWeight();
      _pos = new GeoLocation(n.getLocation());
   }

   @Override
   public int getKey() {
      return _id;
   }

   @Override
   public geo_location getLocation() {
      return _pos;
   }

   @Override
   public void setLocation(geo_location p) {
      _pos = p;
   }

   @Override
   public double getWeight() {
      return _weight;
   }

   @Override
   public void setWeight(double w) {
      _weight = w;
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
      return "{" +
              "id=" + _id +
              ", pos=" + _pos +
              '}';
   }
}

class NodeDataAdapter implements JsonSerializer<node_data>, JsonDeserializer<node_data> {
   @Override
   public JsonElement serialize(node_data node, Type type, JsonSerializationContext jsonSerializationContext) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("pos", node.getLocation().toString());
      jsonObject.addProperty("id", node.getKey());
      return jsonObject;
   }

   @Override
   public node_data deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return null;
   }
}
