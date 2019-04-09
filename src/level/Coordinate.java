package level;

public class Coordinate {
	public final int x;
	public final int y;
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		// ugly Java shit (T_T)
		if (!(other instanceof Coordinate)){
			return false;
		}
		Coordinate other_ = (Coordinate) other;

		return other_.x == this.x && other_.y == this.y;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + x;
		hash = hash * 31 + y;
		return hash;
	}
}
