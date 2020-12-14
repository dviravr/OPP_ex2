package tests;

import api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

   @Test
   void init() {
      directed_weighted_graph g = new DWGraph_DS();
      dw_graph_algorithms ga = new DWGraph_Algo();
      ga.init(g);
      assertEquals(g, new DWGraph_DS());
      g.addNode(new NodeData(0));
      assertNotEquals(g, new DWGraph_DS());
      assertEquals(g, ga.getGraph());
   }

   @Test
   void copy() {
      directed_weighted_graph g = new DWGraph_DS();
      dw_graph_algorithms ga = new DWGraph_Algo();
      for (int i = 0; i < 10; i++) {
         g.addNode(new NodeData(i, new GeoLocation(i, i, i)));
      }
      for (int i = 0; i < 9; i++) {
         g.connect(i, i + 1, i / 2.0);
      }
      ga.init(g);
      assertEquals(ga.getGraph(), ga.copy());
   }

   @Test
   void isConnected() {
      directed_weighted_graph g = new DWGraph_DS();
      dw_graph_algorithms ga = new DWGraph_Algo();
      ga.init(g);
      assertTrue(ga.isConnected()); // EMPTY graph shuold be connect
      g.addNode(new NodeData(0));
      assertTrue(ga.isConnected()); // one node graph need to be connect
      g.addNode(new NodeData(1));
      assertFalse(ga.isConnected()); // 2 node 0 edge need to be not connect.
      g.addNode(new NodeData(2));

      g.connect(0,1,0.2);
      g.connect(0,2,0.2);
      g.connect(2,0,0.2);
      assertFalse(ga.isConnected());
      g.connect(1,2,0.2);
      assertTrue(ga.isConnected());
   }

   @Test
   void shortestPathDist() {
      directed_weighted_graph graph = new DWGraph_DS();
      for (int i = 0; i < 10; i++) {
         graph.addNode(new NodeData(i, new GeoLocation(i, i, i)));
      }
      graph.connect(0,1,1);
      graph.connect(0,7,2);
      graph.connect(1,3,2);
      graph.connect(3,1,1);
      graph.connect(0,3,3);
      graph.connect(7,5,0.1);
      graph.connect(3,4,3);
      graph.connect(4,2,1);
      graph.connect(4,5,1);

      dw_graph_algorithms ga = new DWGraph_Algo();
      ga.init(graph);
      assertEquals(2.1,ga.shortestPathDist(0,5));
   }

   @Test
   void shortestPath() {
      directed_weighted_graph g = new DWGraph_DS();
      dw_graph_algorithms ga = new DWGraph_Algo();
      ga.init(g);

      assertEquals(-1, ga.shortestPathDist(0, 0)); //empty graph return -1
      for (int i = 0; i < 10; i++) {
         g.addNode(new NodeData(i, new GeoLocation(i, i, i)));
      }
      ga.init(g);

      assertEquals(0, ga.shortestPathDist(0, 0));
      g.connect(0, 1, 1);
      g.connect(1, 2, 1);
      g.connect(2, 3, 1);
      g.connect(3, 4, 1);

      ga.init(g);

      assertEquals(4, ga.shortestPathDist(0, 4));
      StringBuilder st = new StringBuilder();
      for (node_data i : ga.shortestPath(0, 4)) {
         String temp = String.valueOf(i.getKey());
         st.append(temp);
      }
      assertEquals("01234", st.toString()); // the only way in the graph is 0 >1>2>3>4
   }



   @Test
   void saveAndLoad() {
      directed_weighted_graph graph = new DWGraph_DS();
      for (int i = 0; i < 10; i++) {
         graph.addNode(new NodeData(i, new GeoLocation(i, i, i)));
      }
      graph.connect(0, 1, 2.5);
      graph.connect(0, 2, 2.1);
      graph.connect(0, 4, 2);
      graph.connect(1, 5, 4.5);
      graph.connect(1, 0, 4.5);
      graph.connect(1, 2, 3.5);
      graph.connect(1, 6, 6.5);
      graph.connect(1, 8, 9.5);
      graph.connect(1, 9, 4.5);

      dw_graph_algorithms ga = new DWGraph_Algo(graph);

      ga.save("./graph");
      ga.init(new DWGraph_DS());
      ga.load("./graph");

      assertEquals(ga.getGraph(), graph);
   }
}