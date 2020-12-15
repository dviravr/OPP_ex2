package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 implements Runnable {

   private static Arena _ar;
   private static long _id = 316095660;
   private static game_service _game;
   private static directed_weighted_graph _graph;
   private static dw_graph_algorithms _ga;
   private static int _scenario = 0;
   private static LoginFrame _login;
   private static boolean _loginPage = false;

   public static void main(String[] args) {
      if (args.length > 0) {
//         if there are id and scenario from the args start the game
         _id = Long.parseLong(args[0]);
         _scenario = Integer.parseInt(args[1]);
         Thread client = new Thread(new Ex2());
         client.start();
      } else {
//         if there aren't id and scenario from the args start the game after login
         _login = new LoginFrame();
         _loginPage = true;
      }
   }

   @Override
   public void run() {
      if (_loginPage) {
         _id = _login.getId();
         _scenario = _login.getScenario();
      }
      _game = Game_Server_Ex2.getServer(_scenario); // you have [0,23] games
      String g = _game.getGraph();
      _graph = getGraph(g);
      _ga = new DWGraph_Algo(_graph);
      _game.login(_id);  // please use your ID only as a key. uncomment this will upload your results to the server
      _ar = new Arena();
      _ar.setGraph(_graph);
      _ar.setPokemons(Arena.json2Pokemons(_game.getPokemons(), _ar.getPokemons(), _graph));
      String info = _game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(_game.getPokemons());

      locateAgents();
      GameFrame _win = new GameFrame(_ar, _scenario, _game);

      _game.startGame();
      for (Agent agent : _ar.getAgents()) {
         _game.chooseNextEdge(agent.getID(), agent.getCurrPokemon().getEdge().getDest());
      }
      System.out.println(_ar.getAgents());
      _game.move();
      while (_game.isRunning()) {
         long t = _game.timeToEnd();
         String lg = _game.move();
         _ar.setAgents(Arena.getAgents(lg, _ar.getAgents(), _graph));
         _win.update(_ar);
         System.out.println(lg);
         long tts = getMinTimeToSleep(_ar.getAgents());
         try {
            Thread.sleep(tts);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         for (Agent agent : _ar.getAgents()) {
            int newDest = closestPokemon(agent);
            _game.chooseNextEdge(agent.getID(), newDest);
         }
      }
      System.out.println(_game.toString());
   }


   private static int closestPokemon(Agent agent) {
      _ar.setPokemons(Arena.json2Pokemons(_game.getPokemons(), _ar.getPokemons(), _graph));
      ArrayList<Pokemon> pokemons = _ar.getPokemons();
//      find best pokemon's src
      int pSrc = findBestPokemon(agent, pokemons);
      int dest = -1;
      if (pSrc != -1) {
//         find the shortest path between agent's node and pokemon's src
         List<node_data> path = _ga.shortestPath(agent.getSrcNode(), pSrc);
//         adding the pokemon's dest to the path
         path.add(_graph.getNode(agent.getCurrPokemon().getEdge().getDest()));
         do {
//            taking the first element from the path while he isn't the agent's node and return that node
            dest = path.remove(0).getKey();
         } while (dest == agent.getSrcNode() && !path.isEmpty());
      }
      return dest;

   }

   private static int findBestPokemon(Agent agent, ArrayList<Pokemon> pokemons) {
      int dest;
      if (agent.getCurrPokemon() == null || atePokemon(agent, pokemons)) {
         agent.setCurrPokemon(pokemons.get(0));
      }
      dest = agent.getCurrPokemon().getEdge().getSrc();
      for (Pokemon pokemon : pokemons) {
//         looping on the pokemons to look after the best pokemon to catch
         int pSrc = pokemon.getEdge().getSrc();
         double closestAgent = Double.MAX_VALUE;
         if (pokemon.getClosestAgent() != null) {
            closestAgent = _ga.shortestPathDist(pokemon.getClosestAgent().getSrcNode(), pSrc);
         }
         double newDist = _ga.shortestPathDist(agent.getSrcNode(), pSrc);
         double oldDist = _ga.shortestPathDist(agent.getSrcNode(), agent.getCurrPokemon().getEdge().getSrc());
//         check if the closest agent to the pokemon is closer the the current agent and if we should switch pokemon
         if (closestAgent >= newDist && shouldSwitchPokemon(newDist, oldDist, pokemon.getValue(), agent)) {
//            set old closest agent's pokemon to null
            if (pokemon.getClosestAgent() != null) {
               pokemon.getClosestAgent().setCurrPokemon(null);
            }
            pokemon.setClosestAgent(agent);
            agent.setCurrPokemon(pokemon);
            dest = pSrc;
         }
      }
      return dest;
   }

   private static boolean shouldSwitchPokemon(double newDist, double oldDist, double newValue, Agent agent) {
      long t = _game.timeToEnd();
      double oldValue = agent.getCurrPokemon().getValue();
      if (newDist <= oldDist) {
         if (newValue >= oldValue) {
//               return true if the new pokemon is closer and the value is bigger
            return true;
         }
//            checking the ratio between the value's of the pokemons
         return ((oldValue / oldDist) > (newValue / newDist));
      } else {
         if ((oldValue / oldDist) <= (newValue / newDist)) {
//               checking if there is enough time to catch the new pokemon
            return (agent.getSpeed() / newDist) > t;
         }
      }
      return false;
   }

   private static void locateAgents() {
      ArrayList<Pokemon> pokemons = _ar.getPokemons();
//      Priority queue of pokemon by their values
      PriorityQueue<Pokemon> mostValuesPokemons = new PriorityQueue<>(pokemons);
      int numOfAgents = getNumOfAgent();

      if (_ga.isConnected()) {
         for (int i = 0; i < numOfAgents; i++) {
            int src = -1;
            Pokemon pokemon = mostValuesPokemons.poll();
            if (pokemon != null) {
               src = pokemon.getEdge().getSrc();
            }
//          locate the agent in the most value pokemon's src node
            _game.addAgent(src);
         }
      } else {
         int i = 0;
         ArrayList<Integer> prevSrc = findDifferentComponents(numOfAgents, mostValuesPokemons);
         while (i < numOfAgents && !mostValuesPokemons.isEmpty()) {
            int src = -1;
            Pokemon pokemon = mostValuesPokemons.poll();
            if (pokemon != null) {
               src = pokemon.getEdge().getSrc();
            }
            for (int prev : prevSrc) {
               if (_ga.shortestPathDist(prev, src) == -1) {
                  break;
               }
            }
            prevSrc.add(src);
//            locate the agent in the most value pokemon's src node
            _game.addAgent(src);
            i++;
         }
      }

      ArrayList<Agent> agents = Arena.getAgents(_game.getAgents(), new ArrayList<>(), _graph);
      for (Agent agent : agents) {
         for (Pokemon pokemon : pokemons) {
            if (pokemon.getEdge().getSrc() == agent.getSrcNode()) {
//               set agent's pokemon and pokemon's agent
               agent.setCurrPokemon(pokemon);
               pokemon.setClosestAgent(agent);
            }
         }
      }
      _ar.setAgents(agents);
   }

   private static ArrayList<Integer> findDifferentComponents(int numOfAgents, PriorityQueue<Pokemon> mostValuesPokemons) {
      ArrayList<Integer> ans = new ArrayList<>();
      int i = 0, src = -1;
      boolean diffComponent = true;
      while (i < numOfAgents && !mostValuesPokemons.isEmpty()) {
         Pokemon pokemon = mostValuesPokemons.poll();
         if (pokemon != null) {
            src = pokemon.getEdge().getSrc();
         }
         for (int a : ans) {
            if (_ga.shortestPathDist(src, a) != -1) {
               diffComponent = false;
            }
         }
         if (diffComponent) {
            ans.add(src);
            i++;
         }
      }
      return ans;
   }

   private static boolean atePokemon(Agent agent, ArrayList<Pokemon> pokemons) {
//      check if the agent's pokemon has been eaten.
      return !pokemons.contains(agent.getCurrPokemon());
   }

   private static long getMinTimeToSleep(ArrayList<Agent> agents) {
      long minT = -1;
      long t = 100;
      ArrayList<Pokemon> pokemons = Arena.json2Pokemons(_game.getPokemons(), _ar.getPokemons(), _graph);
//      calculate the time every agent need to sleep and retuning the minimum time
      for (Agent agent : agents) {
         if (agent.getCurrPokemon() != null) {
            if (atePokemon(agent, pokemons)) {
               t = agent.getTimeAfterEating();
            } else {
               t = agent.getTimeToSleep();
            }
         }
         if (minT == -1 || t < minT) {
            minT = Math.max(t, 90);
         }
      }
      return minT;
   }

   private static int getNumOfAgent() {
//      return num of agents from server
      int numOfAgents = 0;
      try {
         JSONObject line;
         line = new JSONObject(_game.toString());
         JSONObject gameServer = line.getJSONObject("GameServer");
         numOfAgents = gameServer.getInt("agents");
      } catch (JSONException e) {
         e.printStackTrace();
      }
      return numOfAgents;
   }

   private static directed_weighted_graph getGraph(String g) {
//      read graph from string
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphAdapter());
      Gson gson = gsonBuilder.create();
      return gson.fromJson(g, DWGraph_DS.class);
   }
}
