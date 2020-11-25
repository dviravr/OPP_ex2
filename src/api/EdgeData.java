package api;

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
}
