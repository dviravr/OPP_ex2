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

   public static void main(String[] args) { // TODO: 06/12/2020 static
      test1();
//      test2();
   }

   //
   private static void test2() {
//      game = Game_Server_Ex2.getServer(2); // you have [0,23] games
//      String g = game.getGraph();
//      gg = game.getJava_Graph_Not_to_be_used();
//
////      game.login(316095660);  // please use your ID only as a key. uncomment this will upload your results to the server
//      _ar = new Arena();
//      _ar.setGraph(gg);
//      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
//      String info = game.toString();
//      System.out.println(info);
//      System.out.println(g);
//      System.out.println(game.getPokemons());

//      for (int i = 0; i < getNumOfAgent(); i++) {
//         // TODO: 06/12/2020 need to change locateAgents() that it get int i that represent the id
//         // TODO: 06/12/2020 the locateAgents need to
////         String ind = "" + i;
//         Thread thredi = new Thread("thread number: " + i);
//         System.out.println("agent: " + i + " start");
//         thredi.start();
//      }
      Thread client = new Thread(new Ex2());
      client.start();
      startGame();
//      game.startGame();
   }

   private static void startGame() {
//      game.startGame();
//      notifyAll();
   }

   @Override
   public void run() {
      game = Game_Server_Ex2.getServer(2); // you have [0,23] games
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
      // locateAgents(graph,)
      int x = locateAgents().remove(0);
      CL_Agent agent = getAgentById(0);
      game.chooseNextEdge(agent.getID(), x);
      long time;
//      try {
//         wait();
//      } catch (InterruptedException e) {
//         e.printStackTrace();
//      }
      game.startGame();

      while (game.isRunning()) {
         game.move();
         List<node_data> closestPokemonPath = closestPokemon(agent);
         while (!closestPokemonPath.isEmpty() && agent.get_curr_fruit() != null) {
            int newDest = closestPokemonPath.remove(0).getKey();
            if (newDest != agent.getSrcNode()) {
               game.chooseNextEdge(agent.getID(), newDest);
               game.move();
               System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + "   turned to node: " + newDest);
               System.out.println(agent.get_curr_fruit().get_edge());
               System.out.println(agent.get_curr_fruit());
               try {
                  Thread.sleep(agent.getTimeToSleep());
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
//                     System.out.println(i + ") " + a + ") " + agent + "  move to node: " + newDest);
//                     System.out.println(game.getPokemons());
            }
         }


//         game.move();
//         time = agent.getTimeToSleep();// TODO: 06/12/2020   set_SDT(0);
//         try {
//            Thread.sleep(time); // TODO: 06/12/2020 chenge set sdt
//         } catch (InterruptedException e) {
//            e.printStackTrace();
//         }
//         if (!agent.isMoving()) {
//            agent.setCurrNode(agent.getNextNode());
//            agent.setNextNode(closestPokemon(agent).remove(0).getKey());
//         }
      }
   }

   private static void test1() {
      game = Game_Server_Ex2.getServer(2); // you have [0,23] games
      String g = game.getGraph();
      gg = game.getJava_Graph_Not_to_be_used();
//      game.login(316095660);  // please use your ID only as a key. uncomment this will upload your results to the server
      node_data nn = gg.getNode(10);
      _ar = new Arena();
      _ar.setGraph(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
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
//      game.startGame();
      int i = 0;
      System.out.println(game.timeToEnd());
      ArrayList<CL_Agent> agents = Arena.getAgents(game.move(), gg);
      _ar.setAgents(agents);
      while (game.isRunning()) {
         long t = game.timeToEnd();
//         String lg = game.move();
         agents = Arena.getAgents(game.move(), gg);
//         _ar.setAgents(agents);
         for (CL_Agent agent : agents) {
//            CL_Agent agent = log.get(a);
//            System.out.println(i + ") " + a + ") " + agent + "  move to node: " + agent.getSrcNode());
//            System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + "   turned to node: " + agent.getSrcNode());
            int dest = agent.getNextNode();
            int src = agent.getSrcNode();
//            long timeToSleep = timeToSleep(r);
            int id = agent.getID();
//            if (dest == -1) {
            List<node_data> closestPokemonPath = closestPokemon(agent);
            while (!closestPokemonPath.isEmpty() && agent.get_curr_fruit() != null) {
               newDest = closestPokemonPath.remove(0).getKey();
               if (newDest != agent.getSrcNode()) {
                  game.chooseNextEdge(id, newDest);
                  game.move();
                  System.out.println("Agent: " + id + ", val: " + agent.getValue() + "   turned to node: " + newDest);
                  System.out.println(agent.get_curr_fruit().get_edge());
                  System.out.println(agent.get_curr_fruit());
//                     System.out.println(i + ") " + a + ") " + agent + "  move to node: " + newDest);
//                     System.out.println(game.getPokemons());
               }
            }
//            }
         }
         i++;
      }
      System.out.println(game.toString());
   }

   private static List<node_data> closestPokemon(CL_Agent agent) {
      dw_graph_algorithms ga = new DWGraph_Algo(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
      int pSrc = findBestPokemon(agent, pokemons, ga);
      if (pSrc != -1) {
         List<node_data> path = ga.shortestPath(agent.getSrcNode(), pSrc);
         if (agent.getSrcNode() == pSrc) {
            path.add(gg.getNode(agent.get_curr_fruit().get_edge().getDest()));
         }
//      todo: maybe sleep until there is a new pokemon
         return path;
      } else {
         return new ArrayList<>();
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
         if (closestAgent > newDist && shouldSwitchPokemon(newDist, oldDist, pokemon.getValue(), agent)) {
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
//      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
      PriorityQueue<CL_Pokemon> mostValuesPokemons = new PriorityQueue<>(pokemons);
//      ArrayList<CL_Agent> agents = new ArrayList<>();
      ArrayList<Integer> destList = new ArrayList<>();
      int numOfAgents = getNumOfAgent();
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
            pokemon.setClosestAgent(agent);
//            agents.add(agent);
         }
         destList.add(dest);
      }
//      ArrayList<CL_Agent> agents = Arena.getAgents(game.move(), gg);
//      _ar.setAgents(agents);
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
      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().getSrc() == agent.getSrcNode() && agent.get_curr_fruit().get_edge().getDest() == agent.getNextNode();
//      return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().equals(agent.get_curr_edge());
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
