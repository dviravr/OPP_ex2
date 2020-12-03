package gameClient;

import Server.Game_Server_Ex2;
import api.*;

import java.util.ArrayList;
import java.util.List;

public class Ex2 {

   private static game_service game;

   public static void main(String[] args) {
      test1();
   }

   private static void test1() {
      game = Game_Server_Ex2.getServer(12); // you have [0,23] games
      String g = game.getGraph();
      directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
      //game.login(12345);  // please use your ID only as a key. uncomment this will upload your results to the server
      node_data nn = gg.getNode(10);
      String info = game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(game.getPokemons());
      int src_node = 4;  // arbitrary node, you should start at one of the fruits
      game.addAgent(src_node);
      int new_dest = 3;
      game.startGame();
      game.chooseNextEdge(0, new_dest);
      int i = 0;
      while (game.isRunning()) {
         long t = game.timeToEnd();
         String lg = game.move();
         List<CL_Agent> log = Arena.getAgents(lg, gg);
         for (int a = 0; a < log.size(); a++) {
            CL_Agent r = log.get(a);
            int dest = r.getNextNode();
            int src = r.getSrcNode();
            int id = r.getID();
            if (dest == -1) {
               List<node_data> closestPokemonPath = closestPokemon(gg, src);
               while (!closestPokemonPath.isEmpty()) {
                  new_dest = closestPokemonPath.remove(0).getKey();
                  game.chooseNextEdge(id, new_dest);
                  System.out.println(i + ") " + a + ") " + r + "  move to node: " + new_dest);
                  System.out.println(game.getPokemons());
               }
            }
         }
         i++;
      }
      System.out.println(game.toString());
   }

   private static List<node_data> closestPokemon(directed_weighted_graph g, int agent) {
      dw_graph_algorithms ga = new DWGraph_Algo(g);
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
      double dist = Double.MAX_VALUE;
      int dest = -1;
      int pokemonDest = -1;
      for (CL_Pokemon pokemon : pokemons) {
         Arena.updateEdge(pokemon, g);
         int pSrc = pokemon.get_edge().getSrc();
         double temp = ga.shortestPathDist(agent, pSrc);
         double closetAgentDist = ga.shortestPathDist(pokemon.getClosestAgent(), pSrc);
         if (temp < dist && (closetAgentDist == -1 || temp < closetAgentDist)) {
            pokemon.setClosestAgent(agent);
            dist = temp;
            dest = pSrc;
            pokemonDest = pokemon.get_edge().getDest();
         }
      }
      if (agent == dest) {
         dest = pokemonDest;
      }
      if (dest != -1) {
//      todo: maybe sleep until there is a new pokemon
         return ga.shortestPath(agent, dest);
      } else {
         return new ArrayList<>();
      }
   }
}
