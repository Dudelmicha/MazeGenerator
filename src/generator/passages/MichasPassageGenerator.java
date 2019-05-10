package generator.passages;

import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import algorithm.AStar;
import algorithm.BFSSearch;
import algorithm.Distancies;

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
		long wallCrossings = way.stream().filter(x -> x.isTileType(TileType.Wall)).count();
		long wallCrossings2 = way2.stream().filter(x -> x.isTileType(TileType.Wall)).count();
		if(wallCrossings2<wallCrossings)
		{
			way=way2;
		}
		Room room = new Room(map);
		List<Tile> outer = way.stream().filter(x -> !x.isTraversable()).collect(Collectors.toList());
		Tile prev = outer.get(0);
		for(int start=1; start<outer.size(); start++)
		{
			Tile cur = outer.get(start);
			Stream<Tile> prevOuters = prev.getNeighbours(x->x.isTileType(TileType.None));
			Stream<Tile> curOuters = cur.getNeighbours(x->x.isTileType(TileType.None));
			if(prevOuters.findAny().isPresent() && curOuters.findAny().isPresent())
			{
				room.addTile(cur);
				cur.setType(TileType.Floor);

			}
			prev = cur;
		}
		
		
		for(Tile t : room.getTiles())
		{
			//change all neighbour None's to Walls
			for(Tile n : t.getNeighbours(x->!x.isTraversable()).collect(Collectors.toList()))
			{
				n.setType(TileType.Wall);
				room.addWall(n);
			}			
		}
		Tile firstRoomTile = room.getTiles().get(0);
		Tile lastRoomTile = room.getTiles().get(room.getTiles().size()-1);
		Optional<Tile> actualStartTileOpt = firstRoomTile.getNeighbours(x->x.getRoom()!=null).findFirst();
		Optional<Tile> actualEndTileOpt = lastRoomTile.getNeighbours(x->x.getRoom()!=null).findFirst();
		
		if(actualStartTileOpt.isPresent() && actualEndTileOpt.isPresent())
		{
			Tile actualStartTile = actualStartTileOpt.get();
			Tile actualEndTile = actualEndTileOpt.get();
			actualStartTile.makeSimpleDoor(actualEndTile, actualStartTile.getRoom(), actualEndTile.getRoom());
		}
		else
		{
			System.out.println("This should not happen");
		}
		
		map.addRoom(room);
		return room;

	}

	@Override
	public void generatePassages(Level map, Random rand) {
		boolean waysAdded = false;
		M1:do
		{
			Set<Tile> allDoors = new HashSet<Tile>();
			for (Room room : map.getRooms()) {
				Set<Tile> passages = room.getDoors();
				allDoors.addAll(passages);
			}
			

			waysAdded = false;
			for (Tile door : allDoors) {
					int[][] distances = BFSSearch.distances(map, door);
					for (Tile door2 : allDoors) {
					if(door != door2) {

						int indirectWay = BFSSearch.distanceLookup(map, distances, door2); 
		                int directWayFCost = Distancies.manhatten(door, door2);
		                if(indirectWay == 0 //no way
		                	||	
		                		(indirectWay > directWayFCost*2 
		                		&& directWayFCost > 10)) {
		                	System.out.println("directWayFCost"+directWayFCost*3);
		                	System.out.println("indirectWayFCost"+indirectWay);

		                	waysAdded = true;
		                	Room newFloor = buildWay(map,door,door2,rand);
		                	map.printMap();
		                	continue M1;
		                }
					}
				}
			}
		}
		while(waysAdded);

	}
}
