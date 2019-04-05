import java.util.ArrayList;
import java.util.List;

public class Tile {
	private char symbol;
	private int x;
	private int y;
	private Room room;
	private Map map;
	public Tile(Map m, int x, int y) {
		super();
		this.map = m;
		this.x = x;
		this.y = y;
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
	public char getSymbol() {
		return symbol;
	}
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
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
	
	
	
	public Integer getDistance(Tile door2) {
		return Math.abs(getX()-door2.getX()) + Math.abs(getY()-door2.getY());
	}
	
}
