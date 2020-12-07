package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

   private directed_weighted_graph _graph;
   private final HashMap<Integer, Integer> _path;
   private HashMap<Integer, Algo_Node> _nodes;


   /**
    * inner class thar represent node that will used in the algorithm.
    * the Algo_Node is Similar to the main vertex of the program. With slight modifications of addition and omissions.
    */
   private static class Algo_Node implements Comparable<Algo_Node> {
      private final int _AlgoKey;
      private double _AlgoDist = -1;
      private boolean _isVisited = false;

      Algo_Node(int node_id) {
         _AlgoKey = node_id;
      }

      Algo_Node(node_data node) {
         _AlgoKey = node.getKey();
      }

      public int getAlgoKey() {
         return _AlgoKey;
      }

      public boolean isVisited() {
         return _isVisited;
      }

      public void setVisited(boolean visited) {
         _isVisited = visited;
      }

      public double getAlgoDist() {
         return _AlgoDist;
      }

      public void setAlgoDist(double AlgoDist) {
         _AlgoDist = AlgoDist;
      }

      @Override
      public int compareTo(@NotNull Algo_Node o) {
         return Double.compare(this.getAlgoDist(), o.getAlgoDist());
      }
   }

   public DWGraph_Algo() {
      _graph = new DWGraph_DS();
      _path = new HashMap<>();
   }

   public DWGraph_Algo(directed_weighted_graph g) {
      _graph = g;
      _path = new HashMap<>();
   }

   private void initNodes() {
      _nodes = new HashMap<>();
      for (node_data i : _graph.getV()) {
         _nodes.put(i.getKey(), new Algo_Node(i));
      }
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
      if ((_graph.getV().isEmpty()) || (_graph.getV().size() == 1))
         return true;
      int temp = _graph.getV().iterator().next().getKey();
      int connectCount = scan(getGraph(), temp, -1);

      if (connectCount != getGraph().nodeSize())
         return false;

      directed_weighted_graph reverse = reverseGraph(getGraph());
      int connectCountReverse = scan(reverse, temp, -1);
      return (connectCount == connectCountReverse);
   }

   @Override
   public double shortestPathDist(int src, int dest) {
      if (((!_graph.getV().contains(_graph.getNode(src))) ||
              (!_graph.getV().contains(_graph.getNode(dest)))))
         return -1;
      if (src == dest) {
         return 0;
      }
      scan(getGraph(), src, dest);
      return _nodes.get(dest).getAlgoDist();
   }

   @Override
   public List<node_data> shortestPath(int src, int dest) {
      if (src == dest) {
         return new ArrayList<>();
      }
      if (shortestPathDist(src, dest) == -1) {
         return null;
      }
      Stack<node_data> path = new Stack<>();
      path.push(_graph.getNode(dest));
      int i = _path.get(dest);

      while ((_path.get(i) != null)) {
         path.push(_graph.getNode(i));
         i = _path.get(i);
      }
      path.push(_graph.getNode(src));
      // change the stack to link list.
      LinkedList<node_data> shortPath = new LinkedList<>();
      int temp = path.size();
      for (int j = 0; j < temp; j++) {
         shortPath.add(path.pop());
      }
      return shortPath;
   }

   private directed_weighted_graph reverseGraph(directed_weighted_graph g) {
      directed_weighted_graph ans = new DWGraph_DS();
      for (node_data i : g.getV()) {
         ans.addNode(i);
      }
      for (node_data j : g.getV()) {
         for (edge_data i : g.getE(j.getKey())) {
            ans.connect(i.getDest(), i.getSrc(), i.getWeight());
         }
      }
      return ans;
   }

   private int scan(directed_weighted_graph g, int src, int dest) {
      PriorityQueue<Algo_Node> queue = new PriorityQueue<>();
      _path.clear();
      initNodes();

      int counter = 1;
      Algo_Node node = _nodes.get(src);

      _nodes.get(src).setAlgoDist(0);
      _nodes.get(src).setVisited(true);
      queue.add(node);
      _path.put(src, null);

      while (!queue.isEmpty()) {
         node = queue.poll();
         if (node.getAlgoKey() == dest)
            break;
         for (edge_data i : g.getE(node.getAlgoKey())) {
            double temp = node.getAlgoDist() + i.getWeight();
            double currentDist = _nodes.get(i.getDest()).getAlgoDist();
            if (!_nodes.get(i.getDest()).isVisited()) {
               counter++;
               _nodes.get(i.getDest()).setVisited(true);
            }
            if ((currentDist == -1.0) || (temp < currentDist)) {
               _nodes.get(i.getDest()).setAlgoDist(temp);
               queue.add(_nodes.get(i.getDest()));
               _path.put(i.getDest(), i.getSrc());
            }
         }
      }
      return counter;
   }


   @Override
   public boolean save(String file) {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphAdapter());
      Gson gson = gsonBuilder.create();
      try (FileWriter writer = new FileWriter(file)) {
         writer.write(gson.toJson(_graph));
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   @Override
   public boolean load(String file) {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphAdapter());
      Gson gson = gsonBuilder.create();
      try (FileReader reader = new FileReader(file)) {
         init(gson.fromJson(reader, DWGraph_DS.class));
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }
}
