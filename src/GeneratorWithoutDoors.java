import algorithm.Union;
import level.Level;
import level.Room;
import level.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

//This Generator iss derived from Link: https://gist.github.com/munificent/b1bcd969063da3e6c298be070a22b604
public class GeneratorWithoutDoors {
	final static int H = 40;
	final static int W = 80;
	final static Random rand = new Random();
	
	public GeneratorWithoutDoors() {
		
	}

	
	private int g(int x) {
		return rand.nextInt(x);
	}

	public void cave(Level map, int s) {
		Tile[][] m = map.getM();
		int w = g(10) + 5;
		int h = g(6) + 3;
		int t = g(W - w - 2) + 1;
		int u = g(H - h - 2) + 1;

		int d = 0;
		int e = 0, f = 0;
		//check if the room would match
		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				if (m[y][x].getSymbol() == '.' )
					return;
			}
		}
		List<int[]> doorPos = new ArrayList<int[]>();
		if (s != 0) {
			//generate walls, and generate e,f (position of the door)
			for (int y = u - 1; y < u + h + 2; y++) {
				for (int x = t - 1; x < t + w + 2; x++) {
					int s2 = (x < t || x > t + w) ? 1 : 0;
					int t2 = (y < u || y > u + h) ? 1 : 0;
					if (((s2 ^ t2) != 0) && m[y][x].getSymbol() == '#') {
						d++;
						if (g(d) == 0) {
							doorPos.add(new int[] {x,y});
						}
					}
				}
			}
			if (d == 0)
				return;

		}
		
//		for (int y = u - 1; y < u + h + 2; y++) {
//			for (int x = t - 1; x < t + w + 2; x++) {
//				int s2 = (x < t || x > t + w) ? 1 : 0;
//				int t2 = (y < u || y > u + h) ? 1 : 0;
//				m[y][x] = (s2 & t2) != 0 ? '!' : (s2 ^ t2) != 0 ? '#' : '.';
//			}
//		}
		Room curRoom = new Room(map, w, h, t, u);
		map.addRoom(curRoom);

		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				int s2 = (x < t || x > t + w) ? 1 : 0;
				int t2 = (y < u || y > u + h) ? 1 : 0;
				if( (s2 ^ t2) != 0 )
				{
					m[y][x].setSymbol('#');
					curRoom.addWall(m[y][x] );
				}
				else if( (s2 & t2) != 0 )
				{
					m[y][x].setSymbol('!');
					curRoom.addWall(m[y][x] );
				}
				else 
				{
					m[y][x].setSymbol('.');
				}
				curRoom.addWall(m[y][x]);
				m[y][x].setRoom(curRoom);
			}
		}
		
		
//		
//		if (d > 0) {
//				for(int[] door : doorPos) {
//					if(neighbourIs(m,door[1],door[0],'\'')) {
//						m[door[1]][door[0]] = '\'';
//					} else if(neighbourIs(m,door[1],door[0],'+')) {
//						m[door[1]][door[0]] = '+';
//					}
//					else { 
//						m[door[1]][door[0]] = ((g(2) != 0))  ? '\'' : '+';	
//					}
//				}
//				
//		}
		
		
		//add content
		//for (int j = 0; j < (s != 0 ? 1 : g(6) + 1); j++)
		//	m[g(h) + u][g(w) + t] = s != 0 ? '@' : g(4) == 0 ? '$' : 65 + g(62);
	}

	private int neighbourIs(Tile[][] m, int j, int k, char c) {
		int up = j > 0 && m[j-1][k].getSymbol() == c ? 1 : 0;
		int down = j+1 < m.length && m[j+1][k].getSymbol()== c ? 1 : 0;
		int left = k > 0 && m[j][k-1].getSymbol()== c ? 1 : 0;
		int right = k+1 < m[0].length && m[j][k+1].getSymbol()== c ? 1 : 0;
		return up+down+left+right;
	}


	public void printMap(Level map) {
		System.out.println("--------------");
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				char c = map.getM()[y][x].getSymbol();
				System.out.print((c == '!' ? '#' : c));
				if (x == W - 1)
					System.out.print("\n");
			}
		}	
	}
	public void fillMap(Level map) {
		Tile[][] m = map.getM();
		for (int j = 0; j < 1000; j++)
			cave(map,j);
		List<Union<Room>> roomUnions = map.getRooms().stream().map(x -> new Union<Room>(x)).collect(Collectors.toList());
		int geneation = 0;
		while(roomUnions.size()>1)
		{
			geneation++;
			System.out.println(geneation);
			printMap(map);
			Union<Room> r1 = roomUnions.get(g(roomUnions.size()));
			Union<Room> r2 = roomUnions.get(g(roomUnions.size()));
			if(r1.getRoot() != r2.getRoot()) { //merge
				List<Tile> sharedWalls = new ArrayList<Tile>();
				for (Room room1 : r1.getAllT()) {
					for(Tile wall1 : room1.getWalls()) {
						for (Room room2 : r2.getAllT()) {
							sharedWalls.addAll(room2.getWalls().stream()
									.filter(
											x -> x.equals(wall1)
											&& neighbourIs(m, x.getY(), x.getX(), '.') > 1
											&& x.getSymbol() == '#')
									.collect(Collectors.toList()));
						}
					}
				}
				if(!sharedWalls.isEmpty()) {
					roomUnions.remove(r1);
					roomUnions.remove(r2);
					roomUnions.add(r1.unite(r2));
					Tile newDoor = sharedWalls.get(g(sharedWalls.size()));
					newDoor.setSymbol('T');
					List<Tile> neighbours = newDoor.getNeighbours();
					neighbours.forEach(x -> x.getRoom().addDoor(newDoor));
				}
				else //try to merge with other stuff than
				{
					roomUnions.remove(r1);
					roomUnions.add(r1); //add on the end
				}
			}
			else //should have been merged
			{
				roomUnions.remove(r1);
				roomUnions.remove(r2);
				roomUnions.add(r1.unite(r2));
			}
		}
		boolean waysAdded = false;
/*		M1:do
		{
		GraphBuilder<Tile, Integer> builder = GraphBuilder.<Tile,Integer>create();
		Set<Tile> allDoors = new HashSet<Tile>();
		for (Room room : map.getRooms()) {
			Set<Tile> passages = room.getDoors();
			allDoors.addAll(passages);
			for (Tile door : passages) {
				for (Tile door2 : passages) {
					if(door != door2)
					{
						builder.connect(door).to(door2).withEdge(door.getDistance(door2));
					}
				}
			}
		}
		
		HipsterGraph<Tile, Integer> graph = builder.createUndirectedGraph();
		
			waysAdded = false;
			for (Tile door : allDoors) {
				SearchProblem<Integer, Tile, WeightedNode<Integer, Tile, Double>> p = GraphSearchProblem
                        .startingFrom(door)
                        .in(graph)
                        .takeCostsFromEdges()
                        .build();
					for (Tile door2 : allDoors) {
					if(door != door2) {
				
		                Algorithm<Integer, Tile, WeightedNode<Integer, Tile, Double>>.SearchResult result = Hipster.createAStar(p).search(door2);
		                
		                int directWayFCost = door.getDistance(door2);
		                if(result.getGoalNode().getCost() > directWayFCost*2 && directWayFCost > 10) {
		                	System.out.println("directWayFCost"+directWayFCost*3);
		                	System.out.println("indirectWayFCost"+(int)Math.round(result.getGoalNode().getCost()));
		                	
		                	waysAdded = true;
		                	Room newFloor = buildWay(map,door,door2);
		                	continue M1;
		                }
					}
				}
			}
			graph = builder.createUndirectedGraph();
		}
		while(waysAdded);
*/	}
	private Room buildWay(Level map, Tile door, Tile door2) {
		//get adjacent rooms
		List<Room> startRooms = door.getNeighbours().stream().map(x -> x.getRoom()).collect(Collectors.toList());
		List<Room> endRooms = door2.getNeighbours().stream().map(x -> x.getRoom()).collect(Collectors.toList());
		//take one by random
		Room startRoom = startRooms.get(g(startRooms.size()));
		Room endRoom = endRooms.get(g(endRooms.size()));
		//take a tile within the room
		Tile startTile = startRoom.getTiles().get(g(startRoom.getTiles().size()));
		Tile endTile = endRoom.getTiles().get(g(endRoom.getTiles().size()));
		List<Tile> way = getWay(map, startTile, endTile, true);
		List<Tile> way2 = getWay(map, startTile, endTile, false);
		long wallCrossings = way.stream().filter(x -> x.getSymbol() == '#').count();
		long wallCrossings2 = way2.stream().filter(x -> x.getSymbol() == '#').count();
		//debug
		//way.stream().forEach(x -> x.setSymbol('H'));
		//way2.stream().forEach(x -> x.setSymbol('V'));
		//System.out.println(wallCrossings+"-"+wallCrossings2);
		if(wallCrossings2<wallCrossings)
		{
			way=way2;
		}
		way.removeIf(x -> x.getSymbol()=='.');
		boolean outside = false;
		List<Tile> newRoomTiles = new ArrayList<Tile>();
		Room newRoom = new Room(map);
		
		for (Tile tile : way) {
			newRoomTiles.add(tile);
			tile.setSymbol('W');
			if(tile.getSymbol()=='#')
			{
				if(outside)
				{					
					break;
				}
				else
				{
					outside = true;
				}
			}
		}
		Set<Room> connectedRooms = way.stream().flatMap(x->x.getNeighbours().stream().map(y -> y.getRoom())).collect(Collectors.toSet());
		connectedRooms.removeIf(x->x == null);
		if(connectedRooms.size()<2) {
			return null;
		}
		List<Tile> newWalls = new ArrayList<Tile>();
		//retrieve actual startroom (might have stopped early, because of other room in between
		Tile startDoor = newRoomTiles.get(0);
		Tile endDoor = newRoomTiles.get(newRoomTiles.size()-1);
		List<Tile> startRoomTiles = startDoor.getNeighbours();
		List<Tile> endRoomTiles = endDoor.getNeighbours();
		startRoomTiles.removeIf(x -> x.getRoom() == null);
		endRoomTiles.removeIf(x -> x.getRoom() == null);
		startRoom = startRoomTiles.get(0).getRoom();
		endRoom = endRoomTiles.get(0).getRoom();
		
		newRoomTiles.forEach(
				tile -> 
				{
					tile.getNeighbours()
						.forEach(
								neighbour -> {
									neighbour.setSymbol(neighbour.getSymbol()==' '?'#':neighbour.getSymbol());
									if(neighbour.getSymbol()=='#')
									{
										newWalls.add(neighbour);
										neighbour.setRoom(newRoom);
									}
								});
					newRoom.addTile(tile);
					tile.setRoom(newRoom);
				});
		
		
		newWalls.forEach(wall -> newRoom.addWall(wall));
		newRoom.addDoor(startDoor);
		newRoom.addDoor(endDoor);
		startDoor.setRoom(newRoom);
		endDoor.setRoom(newRoom);
		startRoom.addDoor(startDoor);
		endRoom.addDoor(endDoor);
		map.addRoom(newRoom);
		printMap(map);
		return newRoom;
	}


	private List<Tile> getWay(Level map, Tile startTile, Tile endTile, boolean horizontalFirst) {
		int startx = startTile.getX();
		int starty = startTile.getY();
		int endx = endTile.getX();
		int endy = endTile.getY();
		List<Tile> way = new ArrayList<Tile>();
		Tile[][] m = map.getM();
		if(horizontalFirst) { //first horizontal
			if(startx < endx) {
				int x = startx;
				for(; x<=endx; x++) way.add(m[starty][x]);
				if(starty < endy) {
					for(int y=starty; y<=endy; y++) way.add(m[y][x]);
				}else {
					for(int y=starty; y>=endy; y--) way.add(m[y][x]);
				}
			}
			else 
			{
				int x = startx;
				for(; x>=endx; x--) way.add(m[starty][x]);
				if(starty < endy) {
					for(int y=starty; y<=endy; y++) way.add(m[y][x]);
				}else {
					for(int y=starty; y>=endy; y--) way.add(m[y][x]);
				}
			}
		}
		else
		{
			if(starty < endy) {
				int y = starty;
				for(; y<=endy; y++) way.add(m[y][startx]);
				if(startx < endx) {
					for(int x=startx; x<=endx; x++) way.add(m[y][x]);
				}else {
					for(int x=startx; x>=endx; x--) way.add(m[y][x]);
				}
			}
			else 
			{
				int y = starty;
				for(; y>=endy; y--) way.add(m[y][startx]);
				if(startx < endx) {
					for(int x=startx; x<=endx; x++) way.add(m[y][x]);
				}else {
					for(int x=startx; x>=endx; x--) way.add(m[y][x]);
				}
			}
		}
		return way;
	}


	public Level createEmptyMap() {
		Tile[][] m = new Tile[H][W];
		Level map = new Level(m);
		for (int y = 0; y < H; y++) {
			m[y] = new Tile[W];
			for (int x = 0; x < W; x++) {
				m[y][x] = new Tile(map, x, y);
			}
		}
		return map;
	}
}
