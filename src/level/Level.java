package level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Level {
	private Tile[][] m;
	private List<Room> rooms = new ArrayList<Room>();

	public int width() { return m[0].length; }
	public int height() { return m.length; }
	public Tile tile(Coordinate c) { return m[c.y][c.x]; }

	// data encapsulation!?
	public Tile[][] getM() {
		return m;
	}
	public void setM(Tile[][] m) {
		this.m = m;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Level(int width, int height) {
		m = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			m[y] = new Tile[width];
			for (int x = 0; x < width; x++) {
				m[y][x] = new Tile(this, x, y);
			}
		}
	}
	public Level(Tile[][] m) {
		super();
		this.m = m;
	}
	
	public Level(Tile[][] m, List<Room> rooms) {
		this(new Tile[m.length][m[0].length]);
		for(int y=0; y<m.length; y++)
		{
			for(int x=0; x<m[y].length; x++)
			{
				m[y][x] = new Tile(this,x,y);
			}
		}
		
		
		for(Room room : rooms)
		{
			for (int y = room.getU() - 1; y < room.getU() + room.getH() + 2; y++) {
				for (int x = room.getT() - 1; x < room.getT() + room.getW() + 2; x++) {
					this.m[y][x] = m[y][x];
				}
			}
		}
		this.rooms = rooms;
	}

	public Level(Tile[][] m, Stream<Room> filter) {
		this(m, filter.collect(Collectors.toList()));
	}

	public void addRoom(Room room) {
		this.rooms.add(room);
		for (int y = room.getU() ; y < room.getU() + room.getH() ; y++) {
			for (int x = room.getT() ; x < room.getT() + room.getW() ; x++) {
				room.addTile(this.m[y][x]);
			}
		}
	}

	public void addRoom(int w, int h, int t, int u) {
		addRoom(new Room(this, w, h, t, u));
	}

	
	public void printMap() {
		System.out.println("--------------");
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				char c = getM()[y][x].getSymbol();
				System.out.print(c);
				if (x == width() - 1)
					System.out.print("\n");
			}
		}	
	}
}