package generator.doors;

import algorithm.Union;
import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleDoorGenerator implements IDoorGenerator {

	private int neighbourIs(Tile[][] m, int j, int k, TileType t) {
		int up = j > 0 && m[j-1][k].getType() == t ? 1 : 0;
		int down = j+1 < m.length && m[j+1][k].getType() == t ? 1 : 0;
		int left = k > 0 && m[j][k-1].getType()== t ? 1 : 0;
		int right = k+1 < m[0].length && m[j][k+1].getType()== t ? 1 : 0;
		return up+down+left+right;
	}

	@Override
	public void generateDoors(Level map, Random rand) {
		Tile[][] m = map.getM();
		List<Union<Room>> roomUnions = map.getRooms().stream().map(x -> new Union<Room>(x)).collect(Collectors.toList());
		int generation = 0;
		while(roomUnions.size()>1)
		{
			generation++;
			Union<Room> r1 = roomUnions.get(rand.nextInt(roomUnions.size()));
			Union<Room> r2 = roomUnions.get(rand.nextInt(roomUnions.size()));
			if(r1.getRoot() != r2.getRoot()) { //merge
				List<Tile> sharedWalls = new ArrayList<Tile>();
				for (Room room1 : r1.getAllT()) {
					for(Tile wall1 : room1.getWalls()) {
						for (Room room2 : r2.getAllT()) {
							sharedWalls.addAll(room2.getWalls().stream()
									.filter(
											x -> x.equals(wall1)
													&& neighbourIs(m, x.getY(), x.getX(), TileType.Floor) > 1
													&& x.getType() == TileType.Wall)
									.collect(Collectors.toList()));
						}
					}
				}
				if(!sharedWalls.isEmpty()) {
					roomUnions.remove(r1);
					roomUnions.remove(r2);
					roomUnions.add(r1.unite(r2));
					Tile newDoor = sharedWalls.get(rand.nextInt(sharedWalls.size()));
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
	}
}
