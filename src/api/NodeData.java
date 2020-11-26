package api;

public class NodeData implements node_data {

   private final int _id;
   private int _tag;
   private String _info;
   private double _weight;
   private geo_location _location;

   NodeData(int id) {
      _id = id;
   }

   NodeData(int id, GeoLocation p) {
      this(id);
      _location = p;
   }

   NodeData(node_data n) {
//      copy constructor
      _id = n.getKey();
      _tag = n.getTag();
      _info = n.getInfo();
      _weight = n.getWeight();
      _location = new GeoLocation(n.getLocation());
   }

   @Override
   public int getKey() {
      return _id;
   }

   @Override
   public geo_location getLocation() {
      return _location;
   }

   @Override
   public void setLocation(geo_location p) {
      _location = p;
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
      return "NodeData{" +
              "id=" + _id +
              ", tag=" + _tag +
              ", info='" + _info +
              ", weight=" + _weight +
              ", location=" + _location +
              '}';
   }
}
