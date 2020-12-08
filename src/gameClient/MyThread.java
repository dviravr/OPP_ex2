package gameClient;
import api.*;

import java.util.ArrayList;
import java.util.List;


public class MyThread extends Thread {

    private static Arena _ar;
    private static game_service _game;
    private static directed_weighted_graph _gg;
    private CL_Agent _agent;

    MyThread(String name, CL_Agent agent, game_service game, directed_weighted_graph gg, Arena ar) {
        super(name);
        _agent = agent;
        _ar = ar;
        _game = game;
        _gg = gg;
    }

    @Override
    public void run() {
        int newDest;
        while (_game.isRunning()) {
            long t = _game.timeToEnd();
//
//            System.out.println(i + ") " + a + ") " + agent + "  move to node: " + agent.getSrcNode());
//            System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + "   turned to node: " + agent.getSrcNode());
            int dest = _agent.getNextNode();
            int src = _agent.getSrcNode();
//            long timeToSleep = timeToSleep(r);
            int id = _agent.getID();
//            if (dest == -1) {
            List<node_data> closestPokemonPath = closestPokemon(_agent);
            while (!closestPokemonPath.isEmpty() && _agent.get_curr_fruit() != null) {
                newDest = closestPokemonPath.remove(0).getKey();
                if (newDest != _agent.getSrcNode()) {
                    _game.chooseNextEdge(id, newDest);
                    _game.move();
                    System.out.println("Agent: " + id + ", val: " + _agent.getValue() + "   turned to node: " + newDest);
                    System.out.println(_agent.get_curr_fruit().get_edge());
                    System.out.println(_agent.get_curr_fruit());
//                     System.out.println(i + ") " + a + ") " + agent + "  move to node: " + newDest);
//                     System.out.println(game.getPokemons());
                    try {
                            Thread.sleep(_agent.getTimeToSleep());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    if(isGoingToEatPokemon(_agent)){
                       _game.move();
                        try {
                            Thread.sleep(_agent.getTimeAfterEating());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
//            }
        }

        System.out.println(_game.toString());

    }



    private static List<node_data> closestPokemon(CL_Agent agent) {
        dw_graph_algorithms ga = new DWGraph_Algo(_gg);
       // _ar.setPokemons(Arena.json2Pokemons(game.getPokemons(), _ar.getPokemons(), gg));
        ArrayList<CL_Pokemon> pokemons = _ar.getPokemons();
        int pSrc = findBestPokemon(agent, pokemons, ga);
        if (pSrc != -1) {
            List<node_data> path = ga.shortestPath(agent.getSrcNode(), pSrc);
            if (agent.getSrcNode() == pSrc) {
                path.add(_gg.getNode(agent.get_curr_fruit().get_edge().getDest()));
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
        long t = _game.timeToEnd();
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

    private static boolean isGoingToEatPokemon(CL_Agent agent) {
        return agent.get_curr_fruit() != null && agent.get_curr_fruit().get_edge().getSrc() == agent.getSrcNode() && agent.get_curr_fruit().get_edge().getDest() == agent.getNextNode();
    }
}
