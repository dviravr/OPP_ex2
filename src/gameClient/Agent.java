package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class Agent {
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _currEdge;
	private node_data _currNode;
	private final directed_weighted_graph _graph;
	private Pokemon _currPokemon;
	private double _value;


	public Agent(directed_weighted_graph g, int startNode) {
		_graph = g;
		setMoney(0);
		_currNode = _graph.getNode(startNode);
		_pos = _currNode.getLocation();
		_id = -1;
		setSpeed(0);
	}

	public void update(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject jsonObject = line.getJSONObject("Agent");
			int id = jsonObject.getInt("id");
			if (id == this.getID() || this.getID() == -1) {
				if (this.getID() == -1) {
					_id = id;
				}
				double speed = jsonObject.getDouble("speed");
				String pos = jsonObject.getString("pos");
				int src = jsonObject.getInt("src");
				int dest = jsonObject.getInt("dest");
				double value = jsonObject.getDouble("value");
				_pos = new Point3D(pos);
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toJSON() {
		return "{\"Agent\":{"
				+ "\"id\":" + getID() + ","
				+ "\"value\":" + getValue() + ","
				+ "\"src\":" + getSrcNode() + ","
				+ "\"dest\":" + getNextNode() + ","
				+ "\"speed\":" + getSpeed() + ","
				+ "\"pos\":\"" + getLocation().toString() + "\""
				+ "}"
				+ "}";
	}

	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = getSrcNode();
		_currEdge = _graph.getEdge(src, dest);
		if (isMoving()) {
			ans = true;
		} else {
			_currEdge = null;
		}
		return ans;
	}

	public int getSrcNode() {
		return _currNode.getKey();
	}

	private void setMoney(double v) {
		_value = v;
	}

	public void setCurrNode(int src) {
		_currNode = _graph.getNode(src);
	}

	public boolean isMoving() {
		return getCurrEdge() != null;
	}

	public String toString() {
		return toJSON();
	}

	public int getID() {
		return this._id;
	}

	public geo_location getLocation() {
		return _pos;
	}

	public double getValue() {
		return this._value;
	}

	public edge_data getCurrEdge() {
		return _currEdge;
	}

	public int getNextNode() {
		int ans = -2;
		if (this._currEdge == null) {
			ans = -1;
		} else {
			ans = this._currEdge.getDest();
		}
		return ans;
	}

	public double getSpeed() {
		return _speed;
	}

	public void setSpeed(double v) {
		_speed = v;
	}

	public Pokemon getCurrPokemon() {
		return _currPokemon;
	}

	public void setCurrPokemon(Pokemon currPokemon) {
		_currPokemon = currPokemon;
	}

	public long getTimeToSleep() {
		long t = 0;
		if (isMoving()) {
			double w = getCurrEdge().getWeight();
			double norm = getNorm();
			double dt = w * norm / this.getSpeed();
			t = (long) (1000.0 * dt);
		}
		return t;
	}

	public long getTimeAfterEating() {
		long t = 0;
		if (isMoving()) {
			double w = getCurrEdge().getWeight();
			double norm = 1 - getNorm();
			double dt = w * norm / this.getSpeed();
			t = (long) (1000.0 * dt);
		}
		return t;
	}

	private double getNorm() {
		geo_location dest = _graph.getNode(getCurrEdge().getDest()).getLocation();
		geo_location src = _graph.getNode(getCurrEdge().getSrc()).getLocation();
		double edge = src.distance(dest);
		double dist = _pos.distance(dest);
		if (getCurrPokemon().getEdge().equals(this.getCurrEdge())) {
			dist = getCurrPokemon().getLocation().distance(src);
		}
		return dist / edge;
	}
}
