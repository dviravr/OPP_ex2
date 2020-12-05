package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 {

   private static game_service game;
   private static directed_weighted_graph gg;

   public static void main(String[] args) {
      test1();
   }

   private static void test1() {
      game = Game_Server_Ex2.getServer(23); // you have [0,23] games
      String g = game.getGraph();
      gg = game.getJava_Graph_Not_to_be_used();
      //game.login(12345);  // please use your ID only as a key. uncomment this will upload your results to the server
      node_data nn = gg.getNode(10);
      String info = game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(game.getPokemons());
      ArrayList<Integer> destList = locateAgents();
      game.startGame();
      ArrayList<Long> timeToSleepList = new ArrayList<>();
      int newDest;
      for (int i = 0; i < destList.size(); i++) {
         newDest = destList.get(i);
         game.chooseNextEdge(i, newDest);
//         timeToSleepList.add(timeToSleep(i, newDest)); // todo: what is the agent id
      }
      int i = 0;
      while (game.isRunning()) {
         long t = game.timeToEnd();
         String lg = game.move();
         List<CL_Agent> log = Arena.getAgents(lg, gg);
         for (int a = 0; a < log.size(); a++) {
            CL_Agent r = log.get(a);
            int dest = r.getNextNode();
            int src = r.getSrcNode();
            long timeToSleep = timeToSleep(r);
            int id = r.getID();
            if (dest == -1) {
               List<node_data> closestPokemonPath = closestPokemon(r);
               while (!closestPokemonPath.isEmpty()) {
                  newDest = closestPokemonPath.remove(0).getKey();
                  game.chooseNextEdge(id, newDest);
                  System.out.println(i + ") " + a + ") " + r + "  move to node: " + newDest);
                  System.out.println(game.getPokemons());
               }
            }
         }
         i++;
      }
      System.out.println(game.toString());
   }

   private static long timeToSleep(CL_Agent agent) {
      long t;
      double dist = 1;
      edge_data e = agent.get_curr_edge();
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
//      for (CL_Pokemon pokemon : pokemons) {
//         check if the agent is going to eat one of the pokemons
      if (isGoingToEatPokemon(agent)) {
         double x = agent.getLocation().distance(agent.get_curr_fruit().getLocation());
//            double x = agent.getLocation().distance(pokemon.getLocation());
         double y = agent.getLocation().distance(gg.getNode(e.getDest()).getLocation());
         dist = x / y;
//            break;
      }
//      }
//      time = distance / speed
      t = (long) ((e.getWeight() * dist * 1000) / agent.getSpeed());
      return t;
   }

   private static List<node_data> closestPokemon(CL_Agent agent) {
      dw_graph_algorithms ga = new DWGraph_Algo(gg);
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
      int dest = -1;
      for (CL_Pokemon pokemon : pokemons) {
//         looping on the pokemons to look after the best pokemont to catch
         Arena.updateEdge(pokemon, gg);
         int pSrc = pokemon.get_edge().getSrc();
         double temp = ga.shortestPathDist(agent.getSrcNode(), pSrc);
//         todo: think again what distance we send to the function
         double closetAgentDist = ga.shortestPathDist(pokemon.getClosestAgentNode(), pSrc);
         if (shouldSwitchPokemon(temp, closetAgentDist, pokemon.getValue(), agent)) {
            pokemon.setClosestAgentNode(agent.getSrcNode());
            agent.set_curr_fruit(pokemon);
            dest = pSrc;
         }
      }
      if (agent.getSrcNode() == dest) {
         dest = agent.get_curr_fruit().get_edge().getDest();
      }
      if (dest != -1) {
//      todo: maybe sleep until there is a new pokemon
         return ga.shortestPath(agent.getSrcNode(), dest);
      } else {
         return new ArrayList<>();
      }
   }

   private static boolean shouldSwitchPokemon(double newDist, double oldDist, double newValue, CL_Agent agent) {
      if (agent.get_curr_fruit() != null) {
         long t = game.timeToEnd();
         double oldValue = agent.get_curr_fruit().getValue();
         if (newDist < oldDist) {
            if (newValue >= oldValue) {
//               return true if the new pokemon is closer and the value is bigger
               return true;
            }
//            checking the ratio between the value's of the pokemons
            return ((newDist / oldDist) >= (newValue / oldValue));
         } else {
            if ((newDist / oldDist) <= (newValue / oldValue)) {
//               checking if there is enough time to catch the new pokemon
               return (agent.getSpeed() / newDist) > t;
            }
         }
      }
      return false;
   }

   private static ArrayList<Integer> locateAgents() {
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
      PriorityQueue<CL_Pokemon> mostValuesPokemons = new PriorityQueue<>();
      ArrayList<Integer> destList = new ArrayList<>();
      for (CL_Pokemon pokemon : pokemons) {
         Arena.updateEdge(pokemon, gg);
         mostValuesPokemons.add(pokemon);
      }
      int numOfAgents = 0;
      try {
         JSONObject line;
         line = new JSONObject(game.toString());
         JSONObject gameServer = line.getJSONObject("GameServer");
         numOfAgents = gameServer.getInt("agents");
      } catch (JSONException e) {
         e.printStackTrace();
      }
      for (int i = 0; i < numOfAgents; i++) {
         int src = -1;
         int dest = -1;
         CL_Pokemon pokemon = mostValuesPokemons.poll();
         if (pokemon != null) {
            src = pokemon.get_edge().getSrc();
            dest = pokemon.get_edge().getDest();
         } else {
//            todo: if pokemon is null maybe get a random src
         }
         game.addAgent(src);
         CL_Agent agent = getAgentById(i);
         if (agent != null && pokemon != null) {
//            todo: check if we can count on this that the agents ids start from 0
            agent.set_curr_fruit(pokemon);
            pokemon.setClosestAgentNode(src);
         }
         destList.add(dest);
      }
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
//      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().getSrc() == agent.getSrcNode() && agent.get_curr_fruit().get_edge().getDest() == agent.getNextNode();
      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().equals(agent.get_curr_edge());
   }
}
