package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class CL_Pokemon implements Comparable<CL_Pokemon> {
	private edge_data _edge;
	private final double _value;
	private final int _type;
	private final Point3D _pos;
	private double min_dist;
	private int min_ro;
	private CL_Agent closestAgent;

	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
		closestAgent = null;
	}

	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public String toString() {
		return "F:{v=" + _value + ", t=" + _type + "}";
	}

	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}

	public int getType() {
		return _type;
	}

	public CL_Agent getClosestAgent() {
		return closestAgent;
	}

	public void setClosestAgent(CL_Agent closestAgent) {
		this.closestAgent = closestAgent;
	}

	//	public double getSpeed() {return _speed;}
	public double getValue() {
		return _value;
	}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}

	@Override
	public int compareTo(@NotNull CL_Pokemon o) {
		return Double.compare(o.getValue(), getValue());
	}
}
