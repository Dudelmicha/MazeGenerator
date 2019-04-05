import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Map {
	private int[][] m;
	private List<Room> rooms = new ArrayList<Room>();

	public int[][] getM() {
		return m;
	}

	public void setM(int[][] m) {
		this.m = m;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	
	public Map(int[][] m) {
		super();
		this.m = m;
	}
	
	public Map(int[][] m, List<Room> rooms) {
		this(new int[m.length][m[0].length]);
		for (int[] col : this.m) {
			Arrays.setAll(col, x -> ' ');	
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

	public Map(int[][] m, Stream<Room> filter) {
		this(m, filter.collect(Collectors.toList()));
	}

	public void addRoom(Room room) {
		this.rooms.add(room);
	}

	public void addRoom(int w, int h, int t, int u) {
		addRoom(new Room(this, w, h, t, u));
	}

}