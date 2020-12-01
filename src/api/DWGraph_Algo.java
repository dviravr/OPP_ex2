package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

   private directed_weighted_graph _graph;
   private final HashMap<Integer, Integer> _path;
   private HashMap<Integer,Algo_Node> _nodes;
   private PriorityQueue<Algo_Node> _queue;
   private Comparator<Algo_Node> compereNode = new CompereNode();


   /**
    * inner class thar represent node that will used in the algorithm.
    * the Algo_Node is Similar to the main vertex of the program. With slight modifications of addition and omissions.
    */
   private class Algo_Node{
      private int _AlgoKey;
      private  double _AlgoDist= -1;
      private boolean _isVisited = false;

      Algo_Node(int node_id){
         _AlgoKey = node_id;
      }

      Algo_Node(node_data node){
         _AlgoKey=node.getKey();
      }

      public int get_AlgoKey() {
         return _AlgoKey;
      }

      public boolean isVisited() {
         return _isVisited;
      }

      public void setVisited(boolean visited) {
         _isVisited = visited;
      }

      public double get_AlgoDist() {
         return _AlgoDist;
      }

      public void set_AlgoDist(double _AlgoDist) {
         this._AlgoDist = _AlgoDist;
      }
   }

   /**
    * inner class of comparator it used to compre between 2 Algo_Node when you add them to the priority queue.
    * The comparison is made according to the values of the _AlgoDist in each of the node.
    */
   private static class CompereNode implements Comparator<Algo_Node> {

      public CompereNode(){}
      @Override
      public int compare(Algo_Node o1, Algo_Node o2) {
         int ans = 0;
         double tag1 = o1.get_AlgoDist();
         double tag2 = o2.get_AlgoDist();
         if(tag1<0 || tag2<0)
            throw new RuntimeException("ERR: the node can not add to the queue without change is default tag");
         if(tag1<tag2) {ans = -1;}
         if(tag1>tag2) {ans = 1;}
         return ans;
      }
   }


   public DWGraph_Algo() {
      _graph = new DWGraph_DS();
      _path = new HashMap<>();
      _nodes= new HashMap<>();
   }

   public DWGraph_Algo(directed_weighted_graph g) {
      _graph = g;
      _path = new HashMap<>();
      _nodes = new HashMap<>();
      for (node_data i: g.getV()) {
         _nodes.put(i.getKey(),new Algo_Node(i));
      }
   }

   @Override
   public void init(directed_weighted_graph g) {
      _graph = g;
      _queue = new PriorityQueue<Algo_Node>(compereNode);
      _nodes = new HashMap<>();
    //  _nodes.clear();
      for (node_data i: g.getV()) {
         _nodes.put(i.getKey(),new Algo_Node(i));
      }
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
      if((_graph.getV().isEmpty() ) || (_graph.getV().size() == 1))
         return true;

      int sumConnect , temp = _graph.getV().iterator().next().getKey();
      /*if ((alreadyRun) && (saveStatus_MC == _graph.getMC()))
         sumConnect = saveStatus_connectCount;
      else*/
      //   sumConnect = dijkstra(temp);
      return (0 == _graph.nodeSize());
   }


   @Override
   public double shortestPathDist(int src, int dest) {
      if(((!_graph.getV().contains(_graph.getNode(src))) ||
              (!_graph.getV().contains(_graph.getNode(dest)))))
         return -1;
      if (src==dest) {return 0;}
         scan(src,dest);
      return _nodes.get(dest)._AlgoDist;
   }

   @Override
   public List<node_data> shortestPath(int src, int dest) {
      if(shortestPathDist(src,dest)== -1)
         return null;
      Stack<node_data> path = new Stack<>();
      path.push(_graph.getNode(dest));
      int i = _path.get(dest);

      while((_path.get(i) != null))
      {
         path.push(_graph.getNode(i));
         i = _path.get(i);
      }
      path.push(_graph.getNode(src));
      // change the stack to link list.
      LinkedList<node_data> shortPath = new LinkedList<>();
      int temp = path.size();
      for (int j = 0; j < temp ; j++) {
         shortPath.add(path.pop());
      }
      return shortPath;
   }

   private void scan(int start,int dest) {
      Algo_Node node = _nodes.get(start);
      _nodes.get(start).set_AlgoDist(0);
      _queue.add(node);
      _path.put(start, null);

      while (!_queue.isEmpty()) {
         node = _queue.poll();
         if(node.get_AlgoKey()==dest)
            break;
         for (edge_data i : _graph.getE(node.get_AlgoKey())) {
            double temp = node.get_AlgoDist() + i.getWeight();
            double currectDist = _nodes.get(i.getDest()).get_AlgoDist();
            if ((currectDist == -1.0) || (temp < currectDist)) {
               _nodes.get(i.getDest()).set_AlgoDist(temp);
               _queue.add(_nodes.get(i.getDest()));
               _path.put(i.getDest(), i.getSrc());
            }
         }
      }
   }



   @Override
   public boolean save(String file) {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphAdapter());
      Gson gson = gsonBuilder.create();
      try (FileWriter writer = new FileWriter("./graph.json")) {
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
      try (FileReader reader = new FileReader("./graph.json")) {
         _graph = gson.fromJson(reader, DWGraph_DS.class);
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }
}
