import api.GeoLocation;
import api.geo_location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoLocationTest {

   @Test
   void distance() {
      geo_location p1 = new GeoLocation(0, 0, 0);
      geo_location p2 = new GeoLocation(2, 2, 1);
      geo_location p3 = new GeoLocation(5, 6, Math.sqrt(3));

      assertEquals(p1.distance(p2), 3);
      assertEquals(p1.distance(p3), 8);
   }
}