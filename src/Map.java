import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Map {
	private Tile[][] m;
	private List<Room> rooms = new ArrayList<Room>();

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

	
	public Map(Tile[][] m) {
		super();
		this.m = m;
	}
	
	public Map(Tile[][] m, List<Room> rooms) {
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

	public Map(Tile[][] m, Stream<Room> filter) {
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

}