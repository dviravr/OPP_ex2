package api;

import java.util.HashMap;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {

   private directed_weighted_graph _graph;
   private final HashMap<Integer, Integer> _path;

   DWGraph_Algo() {
      _graph = new DWGraph_DS();
      _path = new HashMap<>();
   }

   DWGraph_Algo(directed_weighted_graph g) {
      _graph = g;
      _path = new HashMap<>();
   }

   @Override
   public void init(directed_weighted_graph g) {
      _graph = g;
   }

   @Override
   public directed_weighted_graph getGraph() {
      return _graph;
   }

   @Override
   public directed_weighted_graph copy() {
//      deep copy of a graph
      return new DWGraph_DS(_graph);
   }

   @Override
   public boolean isConnected() {
      return false;
   }

   @Override
   public double shortestPathDist(int src, int dest) {
      return 0;
   }

   @Override
   public List<node_data> shortestPath(int src, int dest) {
      return null;
   }

   @Override
   public boolean save(String file) {
      return false;
   }

   @Override
   public boolean load(String file) {
      return false;
   }
}
