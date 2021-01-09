import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.node_data;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.Extensions;

import java.util.*;

public class TimeTest {

   private static int n1, n2;
   private long start;
   private static dw_graph_algorithms ga;

   @BeforeAll
   static void setUp() {
      String fileName = "./data/G_10_80_0.json";
      fileName = "./data/G_100_800_0.json";
      fileName = "./data/G_1000_8000_0.json";
      fileName = "./data/G_10000_80000_0.json";
      fileName = "./data/G_20000_160000_0.json";
      fileName = "./data/G_30000_240000_0.json";
      ga = new DWGraph_Algo();
      ga.load(fileName);
      n1 = getRandomNode();
      do {
         n2 = getRandomNode();
      } while (n2 == n1);
   }

   private static int getRandomNode() {
      Set<Integer> keys = ga.getGraph().getGraphNodes().keySet();
      Integer[] a = keys.toArray(new Integer[0]);
      Random random = new Random();
      int ranInt = random.nextInt(a.length);
      return a[ranInt];
   }

   @BeforeEach
   void start() {
      start = System.currentTimeMillis();
   }

   @AfterEach
   void End(TestInfo testInfo) {
      System.out.println(testInfo.getDisplayName() + " took " +
              ((System.currentTimeMillis() - start) / 1000.0) + " seconds");
   }

   @Test
   @DisplayName("shortest path")
   void testShortestPathTime() {
      System.out.println(ga.shortestPath(n1, n2));
   }

   @Test
   @DisplayName("connected component")
   void testConnectedComponentTime() {
      System.out.println(ga.connectedComponent(n1));
   }

   @Test
   @DisplayName("connected components")
   void testConnectedComponentsTime() {
      System.out.println(ga.connectedComponents());
   }
}
