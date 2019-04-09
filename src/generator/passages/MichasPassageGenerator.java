package generator.passages;

import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.*;
import java.util.stream.Collectors;

public class MichasPassageGenerator implements IPassageGenerator {

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

	private Room buildWay(Level map, Tile door, Tile door2, Random rand) {
		//get adjacent rooms
		List<Room> startRooms = door.getNeighbours().stream().map(x -> x.getRoom()).collect(Collectors.toList());
		List<Room> endRooms = door2.getNeighbours().stream().map(x -> x.getRoom()).collect(Collectors.toList());
		//take one by random
		Room startRoom = startRooms.get(rand.nextInt(startRooms.size()));
		Room endRoom = endRooms.get(rand.nextInt(endRooms.size()));
		//take a tile within the room
		Tile startTile = startRoom.getTiles().get(rand.nextInt(startRoom.getTiles().size()));
		Tile endTile = endRoom.getTiles().get(rand.nextInt(endRoom.getTiles().size()));
		List<Tile> way = getWay(map, startTile, endTile, true);
		List<Tile> way2 = getWay(map, startTile, endTile, false);
		long wallCrossings = way.stream().filter(x -> x.getType() == TileType.Wall).count();
		long wallCrossings2 = way2.stream().filter(x -> x.getType() == TileType.Wall).count();
		//debug
		//way.stream().forEach(x -> x.setSymbol('H'));
		//way2.stream().forEach(x -> x.setSymbol('V'));
		//System.out.println(wallCrossings+"-"+wallCrossings2);
		if(wallCrossings2<wallCrossings)
		{
			way=way2;
		}
		way.removeIf(x -> x.getType()== TileType.Floor);
		boolean outside = false;
		List<Tile> newRoomTiles = new ArrayList<Tile>();
		Room newRoom = new Room(map);

		for (Tile tile : way) {
			newRoomTiles.add(tile);
			tile.setType(TileType.Floor);
			if(tile.getType()==TileType.Wall)
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
										neighbour.setType(neighbour.getType()==TileType.None?TileType.Wall:neighbour.getType());
										if(neighbour.getType()==TileType.Wall)
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
		return newRoom;
	}

	@Override
	public void generatePassages(Level map, Random rand) {
/*		boolean waysAdded = false;
		M1:do
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
		                	Room newFloor = buildWay(map,door,door2,rand);
		                	continue M1;
		                }
					}
				}
			}
			graph = builder.createUndirectedGraph();
		}
		while(waysAdded);
*/
	}
}
