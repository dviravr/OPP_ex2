package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Pokemon implements Comparable<Pokemon> {
	private edge_data _edge;
	private final double _value;
	private final int _type;
	private final Point3D _pos;
	private Agent _closestAgent;

	public Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		_value = v;
		setEdge(e);
		_pos = p;
		_closestAgent = null;
	}

	public String toString() {
		return "F:{v=" + _value + ", t=" + _type + "}";
	}

	public edge_data getEdge() {
		return _edge;
	}

	public void setEdge(edge_data edge) {
		_edge = edge;
	}

	public Point3D getLocation() {
		return _pos;
	}

	public int getType() {
		return _type;
	}

	public Agent getClosestAgent() {
		return _closestAgent;
	}

	public void setClosestAgent(Agent closestAgent) {
		this._closestAgent = closestAgent;
	}

	public double getValue() {
		return _value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pokemon pokemon = (Pokemon) o;
		return Double.compare(pokemon._value, _value) == 0 &&
				_type == pokemon._type &&
				Objects.equals(_edge, pokemon._edge) &&
				Objects.equals(_pos, pokemon._pos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_edge, _value, _type, _pos);
	}

	@Override
	public int compareTo(@NotNull Pokemon o) {
		return Double.compare(o.getValue(), getValue());
	}
}
