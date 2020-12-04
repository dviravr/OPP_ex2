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

   public static void main(String[] args) {
      test1();
   }

   private static void test1() {
      game = Game_Server_Ex2.getServer(23); // you have [0,23] games
      String g = game.getGraph();
      directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
      //game.login(12345);  // please use your ID only as a key. uncomment this will upload your results to the server
      node_data nn = gg.getNode(10);
      String info = game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(game.getPokemons());
      ArrayList<Integer> destList = locateAgents(gg);
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
            long timeToSleep = timeToSleep(gg, r);
            int id = r.getID();
            if (dest == -1) {
               List<node_data> closestPokemonPath = closestPokemon(gg, r);
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

   private static long timeToSleep(directed_weighted_graph g, CL_Agent agent) {
      long t;
      double dist = 1;
      edge_data e = agent.get_curr_edge();
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
//      for (CL_Pokemon pokemon : pokemons) {
//         check if the agent is going to eat one of the pokemons
         if (isGoingToEatPokemon(agent)) {
            double x = agent.getLocation().distance(agent.get_curr_fruit().getLocation());
//            double x = agent.getLocation().distance(pokemon.getLocation());
            double y = agent.getLocation().distance(g.getNode(e.getDest()).getLocation());
            dist = x / y;
//            break;
         }
//      }
//      time = distance / speed
      t = (long) ((e.getWeight() * dist * 1000) / agent.getSpeed());
      return t;
   }

   private static List<node_data> closestPokemon(directed_weighted_graph g, CL_Agent agent) {
      dw_graph_algorithms ga = new DWGraph_Algo(g);
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
      double dist = Double.MAX_VALUE;
      int dest = -1;
      int pokemonDest = -1;
      for (CL_Pokemon pokemon : pokemons) {
         Arena.updateEdge(pokemon, g);
         int pSrc = pokemon.get_edge().getSrc();
         double temp = ga.shortestPathDist(agent.getSrcNode(), pSrc);
         double closetAgentDist = ga.shortestPathDist(pokemon.getClosestAgentNode(), pSrc);
         if (temp < dist && (closetAgentDist == -1 || temp < closetAgentDist)) {
            pokemon.setClosestAgentNode(agent.getSrcNode());
            dist = temp;
            dest = pSrc;
            pokemonDest = pokemon.get_edge().getDest();
         }
      }
      if (agent.getSrcNode() == dest) {
         dest = pokemonDest;
      }
      if (dest != -1) {
//      todo: maybe sleep until there is a new pokemon
         return ga.shortestPath(agent.getSrcNode(), dest);
      } else {
         return new ArrayList<>();
      }
   }

   private static ArrayList<Integer> locateAgents(directed_weighted_graph g) {
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
      PriorityQueue<CL_Pokemon> mostValuesPokemons = new PriorityQueue<>();
      ArrayList<Integer> destList = new ArrayList<>();
      for (CL_Pokemon pokemon : pokemons) {
         Arena.updateEdge(pokemon, g);
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
//      todo: set closet agent
//         todo: check if we can set agent.cuur_fruit
         game.addAgent(src);
         destList.add(dest);
      }
      return destList;
   }

   private static boolean isGoingToEatPokemon(CL_Agent agent) {
//      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().getSrc() == agent.getSrcNode() && agent.get_curr_fruit().get_edge().getDest() == agent.getNextNode();
      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().equals(agent.get_curr_edge());
   }
}
