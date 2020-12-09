package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 implements Runnable {

   private static Arena _ar;
   private static game_service game;
   private static directed_weighted_graph gg;
   private static final int scenario = 2;

   public static void main(String[] args) { // TODO: 06/12/2020 static
//      test1();
      test2();
   }

   //
   private static void test2() {
      Thread client = new Thread(new Ex2());
      client.start();
   }

   @Override
   public void run() {
      game = Game_Server_Ex2.getServer(scenario); // you have [0,23] games
      String g = game.getGraph();
      gg = game.getJava_Graph_Not_to_be_used();

//      game.login(316095660);  // please use your ID only as a key. uncomment this will upload your results to the server
      _ar = new Arena();
      _ar.setGraph(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      String info = game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(game.getPokemons());

      // TODO: 06/12/2020 make graph not local variabels
      locateAgents();
      game.startGame();
      ArrayList<CL_Agent> agents = _ar.getAgents();
      for (CL_Agent agent : agents) {
         game.chooseNextEdge(agent.getID(), agent.get_curr_fruit().get_edge().getDest());
      }
      System.out.println(agents);
      while (game.isRunning()) {
         long t = game.timeToEnd();
//         String lg = game.move();
         agents = Arena.getAgents(game.move(), gg);
//         game.move();
         for (CL_Agent agent : agents) {
            if (agent.getDestList().size() == 0) {
               agent.set_curr_fruit(null);
            }
         }
         closestPokemon(agents);
//         for (CL_Agent agent : agents) {
//            List<node_data> closestPokemonPath = closestPokemon(agent);
            for (int i = 0; i < minDestList(agents); i++) {
//            while (!agent.getDestList().isEmpty() && agent.get_curr_fruit() != null) {
               String lg = game.move();
               agents = Arena.getAgents(lg, gg);
               System.out.println("before sleep: " + lg);
               chooseNextForAll(agents);
//               int newDest = agent.getAndRemoveDest();
//               if (newDest != agent.getSrcNode()) {
//                  game.chooseNextEdge(agent.getID(), newDest);
//                  game.move();
//                  System.out.println(game.getAgents());
//                  log(agents);

                  long tts = getMinTimeToSleep(agents);
                  try {
                     System.out.println("time to sleep: " + tts);
                     Thread.sleep(tts);
                     System.out.println("after sleep: " + game.getAgents());
//                     if (agent.get_curr_edge() != null && agent.get_curr_edge().equals(agent.get_curr_fruit().get_edge())) {
//                        tts = agent.getTimeAfterEating();
//                        System.out.println("suppose to eat. time to sleep: " + tts);
//                        game.move();
//                        Thread.sleep(tts);
//                     }
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }
//               }
            }
//         }
      }
      System.out.println(game.toString());
   }

//   private static void test1() {
//      game = Game_Server_Ex2.getServer(scenario); // you have [0,23] games
//      String g = game.getGraph();
//      gg = game.getJava_Graph_Not_to_be_used();
////      game.login(316095660);  // please use your ID only as a key. uncomment this will upload your results to the server
//      node_data nn = gg.getNode(10);
//      _ar = new Arena();
//      _ar.setGraph(gg);
//      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
//      String info = game.toString();
//      System.out.println(info);
//      System.out.println(g);
//      System.out.println(game.getPokemons());
//      ArrayList<Integer> destList = locateAgents();
//      game.startGame();
//      int newDest;
//      ArrayList<CL_Agent> agents = _ar.getAgents();
//      for (CL_Agent agent : agents) {
//         game.chooseNextEdge(agent.getID(), agent.get_curr_fruit().get_edge().getDest());
//      }
////      for (int i = 0; i < destList.size(); i++) {
////         newDest = destList.get(i);
////         game.chooseNextEdge(i, newDest);
////      }
////      game.startGame();
//      System.out.println(agents);
//      System.out.println(game.timeToEnd());
//      while (game.isRunning()) {
//         long t = game.timeToEnd();
////         String lg = game.move();
////         agents = _ar.getAgents();
////         _ar.setAgents(agents);
//         for (CL_Agent agent : agents) {
////            CL_Agent agent = log.get(a);
////            System.out.println(i + ") " + a + ") " + agent + "  move to node: " + agent.getSrcNode());
////            System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + "   turned to node: " + agent.getSrcNode());
//            int dest = agent.getNextNode();
//            int src = agent.getSrcNode();
////            long timeToSleep = timeToSleep(r);
//            int id = agent.getID();
////            if (dest == -1) {
//            List<node_data> closestPokemonPath = closestPokemon(agent);
//            while (!closestPokemonPath.isEmpty() && agent.get_curr_fruit() != null) {
//               newDest = closestPokemonPath.remove(0).getKey();
//               if (newDest != agent.getSrcNode()) {
//                  game.chooseNextEdge(id, newDest);
//                  game.move();
//                  System.out.println("Agent: " + id + ", val: " + agent.getValue() + "   turned to node: " + newDest);
//                  System.out.println(agent.get_curr_fruit().get_edge());
//                  System.out.println(agent.get_curr_fruit());
////                     System.out.println(i + ") " + a + ") " + agent + "  move to node: " + newDest);
////                     System.out.println(game.getPokemons());
//               }
//            }
////            }
//         }
//      }
//      System.out.println(game.toString());
//   }

   private void log(ArrayList<CL_Agent> agents) {
      for (CL_Agent agent : agents) {
         System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + ", speed: " + agent.getSpeed() + "  src: " + agent.getSrcNode() + "  turned to node: " + agent.getNextNode());
         System.out.println("fruit edge: " + agent.get_curr_fruit().get_edge());
         System.out.println("fruit: " + agent.get_curr_fruit());
         System.out.println("agent edge: " + agent.get_curr_edge());
      }
   }

   private void chooseNextForAll(ArrayList<CL_Agent> agents) {
      for (CL_Agent agent : agents) {
         if (agent.getNextNode() == -1) {
            int newDest = agent.getAndRemoveDest();
            game.chooseNextEdge(agent.getID(), newDest);
         }
      }
   }

   private static int minDestList(ArrayList<CL_Agent> agents) {
      int min = Integer.MAX_VALUE;
      for (CL_Agent agent : agents) {
         int temp = agent.getDestList().size();
         if (min > temp) {
            min = temp;
         }
      }
      return min;
   }

   private static void closestPokemon(List<CL_Agent> agents) {
      dw_graph_algorithms ga = new DWGraph_Algo(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
      for (CL_Agent agent : agents) {
         int pSrc = findBestPokemon(agent, pokemons, ga);
         if (pSrc != -1) {
            List<node_data> path = ga.shortestPath(agent.getSrcNode(), pSrc);
//            if (agent.getSrcNode() == pSrc) {
               path.add(gg.getNode(agent.get_curr_fruit().get_edge().getDest()));
//            }
            agent.setDestList(path);
         } else {
            agent.setDestList(new ArrayList<>());
         }
      }
   }

   private static int findBestPokemon(CL_Agent agent, ArrayList<CL_Pokemon> pokemons, dw_graph_algorithms ga) {
      int dest;
      if (agent.get_curr_fruit() == null) {
         agent.set_curr_fruit(pokemons.get(0));
      }
      dest = agent.get_curr_fruit().get_edge().getSrc();
      for (CL_Pokemon pokemon : pokemons) {
//         looping on the pokemons to look after the best pokemon to catch
         int pSrc = pokemon.get_edge().getSrc();
         double closestAgent = Double.MAX_VALUE;
         if (pokemon.getClosestAgent() != null) {
            closestAgent = ga.shortestPathDist(pokemon.getClosestAgent().getSrcNode(), pSrc);
         }
         double newDist = ga.shortestPathDist(agent.getSrcNode(), pSrc);
         double oldDist = ga.shortestPathDist(agent.getSrcNode(), agent.get_curr_fruit().get_edge().getSrc());
         if (closestAgent >= newDist && shouldSwitchPokemon(newDist, oldDist, pokemon.getValue(), agent)) {
//            set old closest agent's pokemon to null
            if (pokemon.getClosestAgent() != null) {
               pokemon.getClosestAgent().set_curr_fruit(null);
            }
            pokemon.setClosestAgent(agent);
            agent.set_curr_fruit(pokemon);
            dest = pSrc;
         }
      }
      return dest;
   }

   private static boolean shouldSwitchPokemon(double newDist, double oldDist, double newValue, CL_Agent agent) {
      int factor = 1;
      long t = game.timeToEnd();
      double oldValue = agent.get_curr_fruit().getValue();
      if (newDist <= oldDist * factor) {
         if (newValue >= oldValue * factor) {
//               return true if the new pokemon is closer and the value is bigger
            return true;
         }
//            checking the ratio between the value's of the pokemons
         return ((oldValue / oldDist) * factor > (newValue / newDist));
      } else {
         if ((oldValue / oldDist) * factor <= (newValue / newDist)) {
//               checking if there is enough time to catch the new pokemon
            return (agent.getSpeed() / newDist) > t;
         }
      }
      return false;
   }

   private static ArrayList<Integer> locateAgents() {
      ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
      PriorityQueue<CL_Pokemon> mostValuesPokemons = new PriorityQueue<>(pokemons);
      ArrayList<Integer> destList = new ArrayList<>();
      int numOfAgents = getNumOfAgent();
      for (int i = 0; i < numOfAgents; i++) {
         int src = -1;
         int dest = -1;
         CL_Pokemon pokemon = mostValuesPokemons.poll();
         if (pokemon != null) {
            src = pokemon.get_edge().getSrc();
            dest = pokemon.get_edge().getDest();
         }
         game.addAgent(src);
         destList.add(dest);
      }
      ArrayList<CL_Agent> agents = Arena.getAgents(game.getAgents(), gg);
      for (CL_Agent agent : agents) {
         for (CL_Pokemon pokemon : pokemons) {
            if (pokemon.get_edge().getSrc() == agent.getSrcNode()) {
               agent.set_curr_fruit(pokemon);
               pokemon.setClosestAgent(agent);
            }
         }
      }
      _ar.setAgents(agents);
      return destList;
   }

   private static CL_Agent getAgentById(int id) {
      for (CL_Agent agent : Arena.getAgents(game.getAgents(), gg)) {
         if (agent.getID() == id) {
            return agent;
         }
      }
      return null;
   }

   private static boolean isGoingToEatPokemon(CL_Agent agent) {
      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().equals(agent.get_curr_edge());
   }

   private static long getMinTimeToSleep(ArrayList<CL_Agent> agents) {
      long minT = -1;
      long t;
      for (CL_Agent agent : agents) {
         if (agent.get_curr_fruit() == null) {
            t = 0;
            System.err.println("null");
         } else if (isGoingToEatPokemon(agent)) {
            t = agent.getTimeAfterEating();
         } else {
            t = agent.getTimeToSleep();
         }
         if (minT == -1 || t < minT) {
            minT = t;
         }
      }
      return minT;
   }

   private static int getNumOfAgent() {
      int numOfAgents = 0;
      try {
         JSONObject line;
         line = new JSONObject(game.toString());
         JSONObject gameServer = line.getJSONObject("GameServer");
         numOfAgents = gameServer.getInt("agents");
      } catch (JSONException e) {
         e.printStackTrace();
      }
      return numOfAgents;
   }
}
