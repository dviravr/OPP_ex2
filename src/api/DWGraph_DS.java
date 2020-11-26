package api;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {

   private final HashMap<Integer, node_data> _graphNodes;
   private final HashMap<Integer, HashMap<Integer, edge_data>> _graphEdges;
   private final HashMap<Integer, HashSet<Integer>> _destNi;
   private int modeCount = 0;
   private int edgeSize = 0;

   public DWGraph_DS() {
      _graphNodes = new HashMap<>();
      _graphEdges = new HashMap<>();
      _destNi = new HashMap<>();
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
      if (hasEdge(src, dest)) {
         return _graphEdges.get(src).get(dest);
      }
      return null;
   }

   @Override
   public void addNode(node_data n) {
//      if the node exist don't do nothing
      if (hasNode(n.getKey())) {
         _graphNodes.put(n.getKey(), n);
         _graphEdges.put(n.getKey(), new HashMap<>());
         _destNi.put(n.getKey(), new HashSet<>());
         modeCount++;
      }
   }

   @Override
   public void connect(int src, int dest, double w) {
      if (hasNode(src) && hasNode(dest) && w >= 0) { // todo: maybe just w > 0
         if (!hasEdge(src, dest)) {
            edgeSize++;
         }
         _destNi.get(dest).add(src);
         _graphEdges.get(src).put(dest, new EdgeData(src, dest, w));
         modeCount++;
      }
   }

   @Override
   public Collection<node_data> getV() {
      return _graphNodes.values();
   }

   @Override
   public Collection<edge_data> getE(int node_id) {
      if (hasNode(node_id)) {
         return _graphEdges.get(node_id).values();
      }
      return new ArrayList<>();
   }

   @Override
   public node_data removeNode(int key) {
      node_data node = getNode(key);
      if (node != null) {
//         if the node exist in the graph and removing all his edges
//         first, removing all of the edges that the node is the dest node
         ArrayList<HashSet<Integer>> destNis = new ArrayList<>(_destNi.values());
//         destNis have just one element - set of the sources
         for (int ni : destNis.get(0)) {
            removeEdge(ni, key);
         }
//         after he isn't the dest of any node removing him from the graph
         edgeSize -= _graphEdges.get(key).size();
         _graphEdges.remove(key);
         _graphNodes.remove(key);
         modeCount++;
      }
      return node;
   }

   @Override
   public edge_data removeEdge(int src, int dest) {
      if (hasEdge(src, dest)) {
         edgeSize--;
         modeCount++;
         _destNi.get(dest).remove(src);
         return _graphEdges.get(src).remove(dest);
      }
      return null;
   }

   @Override
   public int nodeSize() {
      return _graphNodes.size();
   }

   @Override
   public int edgeSize() {
      return edgeSize;
   }

   @Override
   public int getMC() {
      return modeCount;
   }

   private boolean hasNode(int key) {
      return _graphNodes.containsKey(key);
   }

   private boolean hasEdge(int src, int dest) {
      return hasNode(src) && _graphEdges.get(src).containsKey(dest);
   }
}