package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 implements Runnable {

   private static Arena _ar;
   private static game_service game;
   private static directed_weighted_graph gg;
   private static final int scenario = 11;
   BigFrame _win;

   public static void main(String[] args) throws InterruptedException { // TODO: 06/12/2020 static


     //test3();
      test2();
   }

   private static void test2() {
      Thread client = new Thread(new Ex2());
      client.start();
     // MyStartGame();
//      game.startGame();
   }


//
//   private static void MyStartGame(@NotNull MyThread[] myThreads){
//      game.startGame();
//      for (MyThread th : myThreads) {
//         th.start();
//      }
//   }
//



   @Override
   public void run() {
      game = Game_Server_Ex2.getServer(scenario); // you have [0,23] games
      String g = game.getGraph();
      gg = game.getJava_Graph_Not_to_be_used();

      //game.login(316095660);  // please use your ID only as a key. uncomment this will upload your results to the server
      _ar = new Arena();
      _ar.setGraph(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      String info = game.toString();
      System.out.println(info);
      System.out.println(g);
      System.out.println(game.getPokemons());

      locateAgents();
      _win = new BigFrame(_ar,scenario,game);

      game.startGame();

      ArrayList<CL_Agent> agents = _ar.getAgents();
      for (CL_Agent agent : agents) {
         game.chooseNextEdge(agent.getID(), agent.get_curr_fruit().get_edge().getDest());
      }
      System.out.println(agents);
      game.move();
      while (game.isRunning()) {
         long t = game.timeToEnd();
         String lg = game.move();
         _ar.setAgents(Arena.getAgents(lg,_ar.getAgents(),gg));
   //      _win.updateGame(game);
         _win.update(_ar);
         agents = Arena.getAgents(lg, agents, gg);
         System.out.println(lg);
         long tts = getMinTimeToSleep(agents);
         try {
            Thread.sleep(tts);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         for (CL_Agent agent : agents) {
            int newDest = closestPokemon(agent);
            game.chooseNextEdge(agent.getID(), newDest);
         }
      }
      System.out.println(game.toString());
   }


   private static int closestPokemon(CL_Agent agent) {
      dw_graph_algorithms ga = new DWGraph_Algo(gg);
      _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
      ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
      int pSrc = findBestPokemon(agent, pokemons, ga);
      int dest = -1;
      if (pSrc != -1) {
         List<node_data> path = ga.shortestPath(agent.getSrcNode(), pSrc);
         path.add(gg.getNode(agent.get_curr_fruit().get_edge().getDest()));
         do {
            dest = path.remove(0).getKey();
         } while (dest == agent.getSrcNode() && !path.isEmpty());
      }
      return dest;

   }

   private static int findBestPokemon(CL_Agent agent, ArrayList<CL_Pokemon> pokemons, dw_graph_algorithms ga) {
      int dest;
      if (agent.get_curr_fruit() == null || atePokemon(agent, pokemons)) {
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

   private static boolean atePokemon(CL_Agent agent, ArrayList<CL_Pokemon> pokemons) {
      for (CL_Pokemon pokemon : pokemons) {
         if (agent.get_curr_fruit().get_edge().equals(pokemon.get_edge()) &&
                 agent.get_curr_fruit().getLocation().equals(pokemon.getLocation()) &&
                 agent.get_curr_fruit().getValue() == pokemon.getValue()) {
            return false;
         }
      }
      return true;
   }

   private static long getMinTimeToSleep(ArrayList<CL_Agent> agents) {
      long minT = -1;
      long t;
      ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg);

      for (CL_Agent agent : agents) {
         if (agent.get_curr_fruit() == null) {
            t = 0;
         } else if (atePokemon(agent, pokemons)) {
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
