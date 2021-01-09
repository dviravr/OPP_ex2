package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the "regular" Graph Theory algorithms.
 * this class implements dw_graph_algorithms.
 * each DWGraph_Algo has DWgraph that all the algorithm base on it.
 * each DWGraph_Algo has {@link HashMap} that help us save the prev node in the short path.4
 * each DWGraph_Algo has {@link HashMap} which contains within it the opposite sides
 *    for example with there is a edge from vertex 1 to vertex 2
 *    in HashMap the edge will be kept in the opposite direction from vertex 2 to vertex 1.
 *    There is an explanation below.
 * The class supports saving and uploading graphs from Jason files.
 *
 * TO
 * the class has:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 *
 * To be able to do all these actions. The class uses an internal class "Algo_Node" that creates vertices
 *    that are slightly different from the original vertices NodeData .
 */
public class DWGraph_Algo implements dw_graph_algorithms {

   private directed_weighted_graph _graph;
   private final HashMap<Integer, Integer> _path;
   private HashMap<Integer, Algo_Node> _nodes;


   /**
    * inner class thar represent node that will used in the algorithm.
    * the Algo_Node is Similar to the main vertex of the program. With slight modifications of addition and omissions.
    *
    */
   private static class Algo_Node implements Comparable<Algo_Node> {
      private final int _AlgoKey;
      private double _AlgoDist = -1;
      private boolean _isVisited = false;

      Algo_Node(int node_id) {
         _AlgoKey = node_id;
      }

      /**
       * Returns the key (id) associated with this node.
       * @param node
       */
      Algo_Node(node_data node) {
         _AlgoKey = node.getKey();
      }

      /**
       * Returns the key (id) associated with this node.
       * @return
       */
      public int getAlgoKey() {
         return _AlgoKey;
      }

      /**
       * return if the node was visited.
       * used in the algorithem.
       * @return _isVisited value
       */
      public boolean isVisited() {
         return _isVisited;
      }

      /**
       *
       * @param visited
       */
      public void setVisited(boolean visited) {
         _isVisited = visited;
      }

      /**
       *
       * @return
       */
      public double getAlgoDist() {
         return _AlgoDist;
      }

      /**
       *
       * @param AlgoDist
       */
      public void setAlgoDist(double AlgoDist) {
         _AlgoDist = AlgoDist;
      }

      /**
       *
       * @param o
       * @return
       */
      @Override
      public int compareTo(@NotNull Algo_Node o) {
         return Double.compare(this.getAlgoDist(), o.getAlgoDist());
      }
   }

   /**
    * Constructor
    * Creates an empty Graph_Algo.
    */
   public DWGraph_Algo() {
      _graph = new DWGraph_DS();
      _path = new HashMap<>();
   }

   /**
    * Constructor
    * Creates an DWGraph_Algo that the graph Initialized to the graph g .
    * @param g
    */
   public DWGraph_Algo(directed_weighted_graph g) {
      _graph = g;
      _path = new HashMap<>();
   }

   /**
    * private method that initializes the HashMap _nodes.
    * the method create new Algo_Node that copy the key from the original node, and put it in _nodes.
    */
   private void initNodes() {
      _nodes = new HashMap<>();
      for (node_data i : _graph.getV()) {
         _nodes.put(i.getKey(), new Algo_Node(i));
      }
   }
   /**
    * A method that initializes the graph.
    * Always come before starting the algorithm
    * @param g the graph we want to init.
    */
   @Override
   public void init(directed_weighted_graph g) {
      _graph = g;
   }

   /**
    * @return the gragh that all the algorithem base on.
    */
   @Override
   public directed_weighted_graph getGraph() {
      return _graph;
   }
   /**
    * making deep copy of this graph
    * @return the graph we want to copy.
    */
   @Override
   public directed_weighted_graph copy() {
//      deep copy of a graph
      return new DWGraph_DS(_graph);
   }

   /**
    * The method that check if all the graph is 'strong' Connected.
    *
    * 'strong connectivity' it means - that you can reach from any vertex to any vertex.
    * According to the start of Kosaraju algorithm it is enough to do deep scan to the graph and
    *    calculat the number of nodes are connected that obtained from the scan.
    *    if it is equals to the total number of vertices in the graph
    *    its mean that the graph has weak connect.
    *
    * After that the method invert the graph and scan in the exact same way.
    *    if also the reverse graph is weak connect it means that the graph has strong connect.
    *
    * All scans are done using the method scan
    *    which receives the graph on which the scan base on (original or revers),
    *    the start vertex and an end vertex that is initialized to vertex -1 (that does not exist)
    *    so that the scan is performed to the end.
    * the method used the method scan.
    *
    * @return true if connect; false if not.
    */
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

   /**
    *the method get 2 node and return the weight-distance between the 2 node.
    * the method use 'scan' method that scan the graph and set the '_AlgoDist' in each Algo_Node to be the distance from src.
    * In the last stage the '_AlgoDist' of dest will be the distance between the nodes.
    * if the nodes not connect the scan will not arrive to the dest scan and the 'AlgoDist' of dest will stay the Default -1.
    * @param src - start node
    * @param dest - end (target) node
    * @return - the number of node that take us arrive from src to end. return -1 if the path is not exist.
    */
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
   /**
    * the method get 2 nodes and returns the the shortest path between src to dest - as an ordered List of nodes:
    * src -->  n1 -->  n2 --> ...dest
    *
    * We choose  to implementaion with stack We push in the stack the dest node and after that push the prev node.
    * I do it with the help of the HashMap '_path' that give me direct access to the prev node.
    * in the end we push the src node and finish to push all the path.
    *
    * I return linklist so the last level we need to add the node to the linklist
    * it easy because it already arranged as required because in stack it order LIFO (last in first out)

    * @param src - start node
    * @param dest - end (target) node
    * @return list that represent the path.
    */
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

   /**
    * the method get dw_graph and reverse all his edge.
    * if in the original graph from 1 to 2 in the reverse graph their edge from 2 to 1.
    * @param g
    * @return the reverse graph.
    */
   private directed_weighted_graph reverseGraph(directed_weighted_graph g) {
      directed_weighted_graph ans = new DWGraph_DS();
      for (node_data i : g.getV()) {
         ans.addNode(new NodeData(i.getKey()));
      }
      for (node_data j : g.getV()) {
         for (edge_data i : g.getE(j.getKey())) {
            ans.connect(i.getDest(), i.getSrc(), i.getWeight());
         }
      }
      return ans;
   }

   /**
    * this is the  private method that do all the algorithm to find the shortest path and check connectivty.
    *
    * The method starts with declaring the queue, resetting the _path
    *    and re-creating '_nodes' and initializing the vertices in the map.
    *
    * the scan base on BFS algorithem.
    * we start from the node 'src' and scan all the graph untill the first time that we meet the dest node.
    *
    * the method use the parameter _AlgoDist that contain in each Algo_Node and mark the distance from the node to the start node.
    * the method hold {@link PriorityQueue} and use it to make sure that the node with the smallest weight (from the start node)
    *      is the one that will continue the algorithm and this is how we know every time that if a certain vertex is out of the queue
    *      it is necessarily with the shortest path so far.
    * the method save the prev node of each node in HashMap we will use it in the method 'shortestPath'.
    * the method count the number of the node that connect and return that, it will help us to in 'isConnected()'.
    *
    *
    *
    * @param g
    * @param src
    * @param dest
    * @return
    */
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

   /**
    * Saves this weighted directed graph to the given file name.
    * the method save the grafh in json format using {@Gson} class
    * @return true - iff the file was successfully saved
    */
   @Override
   public boolean save(String file) {
//      save grape in json format
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


   /**
    * This method load a graph to this graph algorithm.
    * the method load grafh in json format using {@Gson} class
    * if the file was successfully loaded - the underlying graph
    *    of this class will be changed (to the loaded one), in case the
    *    graph was not loaded the original graph should remain "as is".
    *
    * @param file - file name
    * @return true - iff the graph was successfully loaded.
    */
   @Override
   public boolean load(String file) {
//      load grape from json
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

   public ArrayList<Integer> connectedComponent(int src) {
      resetVisited();
      Set<Integer> path = bfs(getGraph(), src);
      Set<Integer> transposePath = bfs(reverseGraph(getGraph()), src);
      ArrayList<Integer> component = new ArrayList<>();
      for (Integer n : path) {
         if (transposePath.contains(n)) {
            getGraph().getNode(n).setTag(src);
            component.add(n);
         }
      }
      return component;
   }

   public ArrayList<ArrayList<Integer>> connectedComponents() {
      resetValues();
      ArrayList<ArrayList<Integer>> components = new ArrayList<>();
      for (node_data n : getGraph().getV()) {
         if (n.getTag() == -1) {
            components.add(connectedComponent(n.getKey()));
         }
      }
      return components;
   }

   private Set<Integer> dfs(directed_weighted_graph g, int key, Set<Integer> scc) {
      node_data n = g.getNode(key);
      n.setInfo("visited");
      scc.add(key);

      for (edge_data e : g.getE(key)) {
         node_data ni = g.getNode(e.getDest());
         if (ni.getInfo().equals("not visited")) {
            dfs(g, ni.getKey(), scc);
         }
      }
      return scc;
   }

   private Set<Integer> bfs(directed_weighted_graph g, int src) {
      Queue<Integer> q = new LinkedList<>();
      q.add(src);
      Set<Integer> scc = new HashSet<>();
      g.getNode(src).setInfo("visited");
      scc.add(src);

      while (!q.isEmpty()) {
         int node = q.remove();
         for (edge_data e : g.getE(node)) {
            node_data ni = g.getNode(e.getDest());
            if (!ni.getInfo().equals("visited")) {
               q.add(ni.getKey());
               ni.setInfo("visited");
               scc.add(ni.getKey());
            }
         }
      }
      return scc;
   }

   private void resetValues() {
      for (node_data n : _graph.getV()) {
         n.setInfo("not visited");
         n.setTag(-1);
      }
   }

   private void resetVisited() {
      for (node_data n : _graph.getV()) {
         n.setInfo("not visited");
      }
   }
}
