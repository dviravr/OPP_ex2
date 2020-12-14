package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 *
 * @author boaz.benmoshe
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
	private static Point3D MIN = new Point3D(0, 100, 0);
	private static Point3D MAX = new Point3D(0, 100, 0);
	private directed_weighted_graph _graph;
	private ArrayList<Agent> _agents;
	private ArrayList<Pokemon> _pokemons;
	private List<String> _info;

	public Arena() {
		_info = new ArrayList<>();
		_pokemons = new ArrayList<>();
		_agents = new ArrayList<>();
	}

	public static ArrayList<Agent> getAgents(String json, ArrayList<Agent> agents, directed_weighted_graph gg) {
//		get agents from server
		ArrayList<Agent> ans = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray ags = jsonObject.getJSONArray("Agents");
			for (int i = 0; i < ags.length(); i++) {
				Agent c = new Agent(gg, 0);
				c.update(ags.get(i).toString());
				ans.add(c);
				for (Agent agent : agents) {
//					restore the currPokemon of every agent
					if (agent.getID() == c.getID()) {
						c.setCurrPokemon(agent.getCurrPokemon());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static ArrayList<Pokemon> json2Pokemons(String json, ArrayList<Pokemon> pokemons, directed_weighted_graph g) {
//		get pokemons from server
		ArrayList<Pokemon> ans = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray ags = jsonObject.getJSONArray("Pokemons");
			for (int i = 0; i < ags.length(); i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int type = pk.getInt("type");
				double value = pk.getDouble("value");
				String pos = pk.getString("pos");
				Pokemon pokemon = new Pokemon(new Point3D(pos), type, value, 0, null);
				ans.add(pokemon);
				updateEdge(pokemon, g);
				for (Pokemon p : pokemons) {
					if (pokemon.equals(p)) {
						pokemon.setClosestAgent(p.getClosestAgent());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static void updateEdge(Pokemon pokemon, directed_weighted_graph g) {
//		update the edge of pokemon by his location
		for (node_data n : g.getV()) {
			for (edge_data e : g.getE(n.getKey())) {
				if (isOnEdge(pokemon.getLocation(), e, pokemon.getType(), g)) {
					pokemon.setEdge(e);
				}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if (dist > d1 - EPS2) {
			ans = true;
		}
		return ans;
	}

	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p, src, dest);
	}

	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if (type < 0 && dest > src) {
			return false;
		}
		if (type > 0 && src > dest) {
			return false;
		}
		return isOnEdge(p, src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		boolean first = true;
		for (node_data n : g.getV()) {
			geo_location p = n.getLocation();
			if (first) {
				x0 = p.x();
				x1 = x0;
				y0 = p.y();
				y1 = y0;
				first = false;
			} else {
				if (p.x() < x0) {
					x0 = p.x();
				}
				if (p.x() > x1) {
					x1 = p.x();
				}
				if (p.y() < y0) {
					y0 = p.y();
				}
				if (p.y() > y1) {
					y1 = p.y();
				}
			}
		}
		Range xr = new Range(x0, x1);
		Range yr = new Range(y0, y1);
		return new Range2D(xr, yr);
	}

	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		return new Range2Range(world, frame);
	}

	public ArrayList<Agent> getAgents() {
		return _agents;
	}

	public void setAgents(ArrayList<Agent> agents) {
		_agents = agents;
	}

	public ArrayList<Pokemon> getPokemons() {
		return _pokemons;
	}

	public void setPokemons(ArrayList<Pokemon> pokemons) {
		_pokemons = pokemons;
	}

	public directed_weighted_graph getGraph() {
		return _graph;
	}

	public void setGraph(directed_weighted_graph g) {
		_graph = g;
	}

	public List<String> get_info() {
		return _info;
	}

	public void set_info(List<String> info) {
		_info = info;
	}
}
