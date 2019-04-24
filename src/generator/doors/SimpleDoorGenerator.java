package generator.doors;

import algorithm.Union;
import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleDoorGenerator implements IDoorGenerator {

	private int neighbourIs(Tile[][] m, int j, int k, TileType t) {
		int up = j > 0 && m[j-1][k].isTileType(t) ? 1 : 0;
		int down = j+1 < m.length && m[j+1][k].isTileType(t) ? 1 : 0;
		int left = k > 0 && m[j][k-1].isTileType(t) ? 1 : 0;
		int right = k+1 < m[0].length && m[j][k+1].isTileType(t) ? 1 : 0;
		return up+down+left+right;
	}

	@Override
	public void generateDoors(Level map, Random rand) {
		Tile[][] m = map.getM();
		List<Union<Room>> roomUnions = map.getRooms().stream().map(x -> new Union<Room>(x)).collect(Collectors.toList());
		int mergeSkips = 0;
		M1: while(roomUnions.size()>1)
		{
			Union<Room> r1 = roomUnions.get(rand.nextInt(roomUnions.size()));
			Union<Room> r2 = roomUnions.get(rand.nextInt(roomUnions.size()));
			if(r1.getRoot() != r2.getRoot()) { //merge
				for (Room room1 : r1.getAllT()) {
					List<Tile> walls1 = new ArrayList<Tile>(room1.getWalls());
					Collections.shuffle(walls1);
					for(Tile tile1 : walls1) {
						for (Room room2 : r2.getAllT()) {
							List<Tile> walls2 = new ArrayList<Tile>(room2.getWalls());
							Collections.shuffle(walls2);
							for(Tile tile2 : walls2) {
								boolean f1 =tile1.getNeighbours().stream().filter(x -> x.isTraversable()).count()>1;
								boolean f2 =tile2.getNeighbours().stream().filter(x -> x.isTraversable()).count()>1;
								
								boolean f3 =tile1.getNeighbours().stream().allMatch(x -> x.getRoom()==room1 || x.getRoom()==room2);
								boolean f4 =tile2.getNeighbours().stream().allMatch(x -> x.getRoom()==room1 || x.getRoom()==room2);
								
								if(f1&&f2&&f3&&f4&&(tile1==tile2||tile1.getNeighbours().contains(tile2)||tile2.getNeighbours().contains(tile1)))
								{
									if(!tile1.isTraversable())
									{										
										tile1.setType(TileType.Door);
									}
									if(!tile2.isTraversable())
									{										
										tile2.setType(TileType.Door);
									}
									roomUnions.add(r1.unite(r2));
									roomUnions.remove(r1);
									roomUnions.remove(r2);
									mergeSkips=0;
									continue M1;
								}
							}	
						}
					}	
				}
			}
			if(mergeSkips > roomUnions.size()*roomUnions.size()) {
				break;
			}
			mergeSkips++;
			//push back r1
			roomUnions.remove(r1); 
			roomUnions.add(r1);
		}
	}
}
