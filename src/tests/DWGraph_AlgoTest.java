package tests;

import api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

   @Test
   void init() {
   }

   @Test
   void getGraph() {
   }

   @Test
   void copy() {
   }

   @Test
   void isConnected() {
   }

   @Test
   void shortestPathDist() {
   }

   @Test
   void shortestPath() {
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