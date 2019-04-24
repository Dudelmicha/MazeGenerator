package level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Room {
	private Level map;
	private int w;
	private int h;
	private int t;
	private int u;
	private boolean marked;
	private ArrayList<Tile> walls = new ArrayList<Tile>();
	private Set<Tile> doors = new HashSet<Tile>();
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public int getU() {
		return u;
	}
	public void setU(int u) {
		this.u = u;
	}
	public Room(Level map, int w, int h, int t, int u) {
		super();
		this.map = map;
		this.w = w;
		this.h = h;
		this.t = t;
		this.u = u;
	}
	public Room(Level map) {
		super();
		this.map = map;
	}
	public boolean isMarked() {
		return marked;
	}
	
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	public void addWall(Tile wall) {
		walls.add(wall);
	}
	
	public ArrayList<Tile> getWalls() {
		return walls;
	}
	public void addDoor(Tile newDoor) {
		walls.remove(newDoor);
		doors.add(newDoor);
	}
	public Set<Tile> getDoors() {
		return doors;
	}
	public void addTile(Tile tile) {
		tiles.add(tile);
	}
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
	public Set<Tile> getAllTiles() {
		HashSet<Tile> tiles = new HashSet<Tile>();
		tiles.addAll(getDoors());
		tiles.addAll(getTiles());
		tiles.addAll(getWalls());
		return tiles;
	}
}
