import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
   private static Random _rnd = null;
   private directed_weighted_graph wg;

   public static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
      directed_weighted_graph wg = new DWGraph_DS();
      _rnd = new Random(seed);
      for (int i = 0; i < v_size; i++) {
         wg.addNode(new NodeData(i));
      }
      // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
      int[] nodes = nodes(wg);
      while (wg.edgeSize() < e_size) {
         int a = nextRnd(0, v_size);
         int b = nextRnd(0, v_size);
         int i = nodes[a];
         int j = nodes[b];
         wg.connect(i, j, _rnd.nextDouble());
      }
      return wg;
   }

   @BeforeEach
   void generateGraph() {
      wg = graph_creator(10, 0,1);
   }

   @Test
   void getNode() {
      assertEquals(wg.getNode(1).getKey(), 1);
   }

   @Test
   void getEdge() {
      wg.connect(0, 1, 1);
      wg.connect(2, 1, 1);
      wg.connect(0, 2, 1);
      wg.connect(1, 0, 3);
      assertEquals(wg.getEdge(0, 1).getWeight(), 1);
      assertEquals(wg.getEdge(2, 1).getWeight(), 1);
      assertEquals(wg.getEdge(0, 2).getWeight(), 1);
      assertNotNull(wg.getEdge(2, 1));
      assertEquals(wg.getEdge(1, 0).getWeight(), 3);
   }

   @Test
   void addNode() {
      assertEquals(wg.nodeSize(), 10);
      wg = graph_creator(100, 50, 1);
      assertEquals(wg.nodeSize(), 100);
      assertEquals(wg.edgeSize(), 50);
   }

   @Test
   void connect() {
      assertEquals(wg.edgeSize(), 0);
      wg.connect(0, 0, 3.5);
      assertEquals(wg.edgeSize(), 0);
      wg.connect(0, 1, 2.5);
      assertEquals(wg.edgeSize(), 1);
      wg.connect(0, 2, 0.5);
      assertEquals(wg.edgeSize(), 2);
      wg.connect(0, 2, 1);
      assertEquals(wg.edgeSize(), 2);
      wg.connect(2, 0, 3.5);
      assertEquals(wg.edgeSize(), 3);
   }

   @Test
   void getV() {
      assertEquals(wg.getV().size(), 10);
   }

   @Test
   void getE() {
      wg.connect(0,1,2.5);
      wg.connect(0,2,2.5);
      wg.connect(0,3,2.5);
      wg.connect(0,6,2.5);
      wg.connect(3,1,2.5);
      assertEquals(wg.getE(0).size(), 4);
      assertEquals(wg.getE(1).size(), 0);
      wg.removeEdge(6, 0);
      assertEquals(wg.getE(0).size(), 4);
      wg.removeEdge(0, 6);
      assertEquals(wg.getE(0).size(), 3);
      wg.removeNode(3);
      assertEquals(wg.getE(0).size(), 2);
      wg.removeNode(0);
      assertEquals(wg.getE(0).size(), 0);
   }

   @Test
   void removeNode() {
      wg.removeNode(9);
      assertNull(wg.getNode(9));
      assertNull(wg.getNode(10));
      wg.connect(0, 1, 1);
      wg.connect(0, 2, 1);
      wg.connect(0, 3, 1);
      assertEquals(wg.edgeSize(), 3);
      wg.removeNode(0);
      assertNull(wg.getEdge(0, 1));
      assertNull(wg.getEdge(0, 2));
      assertEquals(wg.edgeSize(), 0);
   }

   @Test
   void removeEdge() {
      wg.connect(0, 0, 3.5);
      wg.connect(0, 1, 2.5);
      wg.connect(0, 2, 0.5);
      wg.connect(2, 0, 3.5);
      assertEquals(wg.edgeSize(), 3);
      wg.removeEdge(0, 0);
      assertEquals(wg.edgeSize(), 3);
      wg.removeEdge(0, 1);
      assertEquals(wg.edgeSize(), 2);
      wg.removeEdge(2, 0);
      assertEquals(wg.edgeSize(), 1);
      wg.removeEdge(2, 0);
      assertEquals(wg.edgeSize(), 1);
      wg.connect(0, 1, 2.5);
      wg.removeEdge(1, 0);
      assertEquals(wg.edgeSize(), 2);
   }

   @Test
   void nodeSize() {
      assertEquals(wg.nodeSize(), 10);
   }

   private static int[] nodes(directed_weighted_graph g) {
      int size = g.nodeSize();
      Collection<node_data> V = g.getV();
      node_data[] nodes = new node_data[size];
      V.toArray(nodes); // O(n) operation
      int[] ans = new int[size];
      for (int i = 0; i < size; i++) {
         ans[i] = nodes[i].getKey();
      }
      Arrays.sort(ans);
      return ans;
   }

   private static int nextRnd(int min, int max) {
      double v = nextRnd(0.0 + min, (double) max);
      int ans = (int) v;
      return ans;
   }

   private static double nextRnd(double min, double max) {
      double d = _rnd.nextDouble();
      double dx = max - min;
      double ans = d * dx + min;
      return ans;
   }
}