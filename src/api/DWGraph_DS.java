package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {

   private final HashMap<Integer, node_data> _graphNodes;
   private int modeCount = 0;

   public DWGraph_DS() {
      _graphNodes = new HashMap<>();
   }

   public DWGraph_DS(directed_weighted_graph g) {
//      copy constructor
      this();
   }

   @Override
   public node_data getNode(int key) {
      return _graphNodes.get(key);
   }

   @Override
   public edge_data getEdge(int src, int dest) {
      return null;
   }

   @Override
   public void addNode(node_data n) {
//      if the node exist don't do nothing
      if (_graphNodes.containsKey(n.getKey())) {
         _graphNodes.put(n.getKey(), n);
         modeCount++;
      }
   }

   @Override
   public void connect(int src, int dest, double w) {

   }

   @Override
   public Collection<node_data> getV() {
      return _graphNodes.values();
   }

   @Override
   public Collection<edge_data> getE(int node_id) {
      return null;
   }

   @Override
   public node_data removeNode(int key) {
      return null;
   }

   @Override
   public edge_data removeEdge(int src, int dest) {
      return null;
   }

   @Override
   public int nodeSize() {
      return _graphNodes.size();
   }

   @Override
   public int edgeSize() {
      return 0;
   }

   @Override
   public int getMC() {
      return modeCount;
   }
}
