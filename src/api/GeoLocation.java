package api;

public class GeoLocation implements geo_location {

   private final double _x;
   private final double _y;
   private final double _z;

   GeoLocation(double x, double y, double z) {
      _x = x;
      _y = y;
      _z = z;
   }

   GeoLocation(geo_location p) {
//      copy constructor
      _x = p.x();
      _y = p.y();
      _z = p.z();
   }

   @Override
   public double x() {
      return _x;
   }

   @Override
   public double y() {
      return _y;
   }

   @Override
   public double z() {
      return _z;
   }

   @Override
   public double distance(geo_location g) {
      return Math.sqrt(Math.pow((_x - g.x()), 2) + Math.pow((_y - g.y()), 2) + Math.pow((_z - g.z()), 2));
   }

   @Override
   public String toString() {
      return _x + "," + _y + "," + _z;
   }
}
