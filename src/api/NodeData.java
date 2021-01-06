package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

public class NodeData implements node_data {

   private final int _id;
   private int _tag;
   private String _info;
   private double _weight;
   private geo_location _pos;

   public NodeData(int id) {
      _id = id;
      _tag = -1;
      _info = "not visited";
   }

   public NodeData(int id, GeoLocation p) {
      this(id);
      _pos = p;
   }

   public NodeData(node_data n) {
//      copy constructor
      _id = n.getKey();
      _tag = n.getTag();
      _info = n.getInfo();
      _weight = n.getWeight();
      if (n.getLocation() != null) {
         _pos = new GeoLocation(n.getLocation());
      }
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

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      NodeData nodeData = (NodeData) o;
      return _id == nodeData._id &&
              _tag == nodeData._tag &&
              Double.compare(nodeData._weight, _weight) == 0 &&
              Objects.equals(_info, nodeData._info) &&
              Objects.equals(_pos, nodeData._pos);
   }

   @Override
   public int hashCode() {
      return Objects.hash(_id, _tag, _info, _weight, _pos);
   }
}

class NodeDataAdapter implements JsonSerializer<node_data>, JsonDeserializer<node_data> {
   @Override
   public JsonElement serialize(node_data node, Type type, JsonSerializationContext jsonSerializationContext) {
//      write node to a json
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("pos", node.getLocation().toString());
      jsonObject.addProperty("id", node.getKey());
      return jsonObject;
   }

   @Override
   public node_data deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//      read node from a json
      int id = jsonElement.getAsJsonObject().get("id").getAsInt();
      String pos = jsonElement.getAsJsonObject().get("pos").getAsString();
      String[] location = pos.split(",");
      GeoLocation p = new GeoLocation(Double.parseDouble(location[0]),
              Double.parseDouble(location[1]), Double.parseDouble(location[2]));
      return new NodeData(id, p);
   }
}
