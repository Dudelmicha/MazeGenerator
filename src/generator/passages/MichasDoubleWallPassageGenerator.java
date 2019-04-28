package generator.passages;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import algorithm.AStar;
import algorithm.BFSSearch;
import algorithm.Distancies;
import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

/*
 * This algorithm connects sections, which are only connected through double walls .##. 
 */
public class MichasDoubleWallPassageGenerator implements IPassageGenerator {

	@Override
	public void generatePassages(Level map, Random rand) {

		M1: for (Room room1 : map.getRooms()) {
			for (Tile wall : room1.getWalls()) {
				Optional<Tile> room1tileOpt = wall.getNeighbours(x -> x.isTileType(TileType.Floor)).findFirst();
				if (room1tileOpt.isPresent()) {
					int[][] lookup = BFSSearch.distances(map, room1tileOpt.get());
					for (Room room2 : map.getRooms()) {
						if (room1 == room2) {
							continue;
						}

						for (Tile wall2 : room2.getWalls()) {
							if (Distancies.manhatten(wall, wall2) == 1) {

								Optional<Tile> room2tileOpt = wall2.getNeighbours(x -> x.isTileType(TileType.Floor)).findFirst();
								if (room1tileOpt.isPresent() && room2tileOpt.isPresent()) {
									Tile room1tile = room1tileOpt.get();
									Tile room2tile = room2tileOpt.get();
									int dist = BFSSearch.distanceLookup(map, lookup, room2tile);
									System.out.println(dist);
									if (dist==0) // there is no way
																								// between
									{
										wall.getRoom().addDoor(wall);
										wall2.getRoom().addDoor(wall);
										wall.getRoom().addDoor(wall2);
										wall2.getRoom().addDoor(wall2);
										wall.setType(TileType.Door);
										wall2.setType(TileType.Door);
										System.out.println("added door");
										continue M1;
									}
								}
							}

						}
					}
				}
			}
		}
	}
}
