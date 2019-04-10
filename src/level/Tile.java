package level;

import java.util.ArrayList;
import java.util.List;

public class Tile {
	private TileType type;
	private int x;
	private int y;
	private Room room;
	private Level map;

	public Tile(Level m, int x, int y) {
		super();
		this.map = m;
		this.x = x;
		this.y = y;
		this.type = TileType.None;
	}

	public Coordinate position() { return new Coordinate(x,y); }
	public boolean isTraversable() {
		switch (this.type) {
			case None:
			case Wall:
				return false;
			case Floor:
			case Door:
				return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setType(TileType type) { this.type = type; }
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public List<Tile> getNeighbours() {
		List<Tile> neighbours = new ArrayList<Tile>();
		Tile[][] m = this.map.getM(); 
		if(y > 0 				&& m[y-1][x] != null) neighbours.add( m[y-1][x]);
		if(y+1 < m.length 		&& m[y+1][x] != null) neighbours.add( m[y+1][x]);
		if(x > 0				&& m[y][x-1] != null) neighbours.add( m[y][x-1]);
		if(x+1 < m[0].length	&& m[y][x+1] != null) neighbours.add( m[y][x+1]);
		return neighbours;
		
	}
	
	public List<Tile> getNeighboursDiagonal() {
		List<Tile> neighbours = new ArrayList<Tile>();
		Tile[][] m = this.map.getM(); 
		if(y > 0 				&& x>0 						&& m[y-1][x-1] != null) neighbours.add( m[y-1][x-1]);
		if(y+1 < m.length 		&& x+1 < m[0].length		&& m[y+1][x+1] != null) neighbours.add( m[y+1][x+1]);
		if(y+1 < m.length 		&& x > 0					&& m[y+1][x-1] != null) neighbours.add( m[y+1][x-1]);
		if(y > 0 				&& x+1 < m[0].length		&& m[y-1][x+1] != null) neighbours.add( m[y-1][x+1]);
		return neighbours;
	}
	
	public List<Tile> getNeighbours3x3() {
		List<Tile> neighbours = new ArrayList<Tile>();
		neighbours.addAll(getNeighbours());
		neighbours.addAll(getNeighboursDiagonal());
		return neighbours;
	}
	
	public boolean isTileType(TileType type) {
		return this.type == type;
	}

	public char getSymbol() {
		switch (type) {
		case Door:
			return 'T';
		case Floor:
			return '.';
		case None:
			return ' ';
		case Wall:
			return '#';
		default:
			return '?';
		}
	}
	
	
	
	
}
