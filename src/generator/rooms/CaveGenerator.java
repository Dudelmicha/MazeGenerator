package generator.rooms;

import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveGenerator implements IRoomGenerator {

	public void cave(Level map, int s, Random rand) {
		Tile[][] m = map.getM();
		int w = rand.nextInt(10) + 5;
		int h = rand.nextInt(6) + 3;
		int t = rand.nextInt(map.width() - w - 2) + 1;
		int u = rand.nextInt(map.height() - h - 2) + 1;

		int d = 0;
		int e = 0, f = 0;
		//check if the room would match
		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				if (m[y][x].getType() == TileType.Floor)
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
					if (((s2 ^ t2) != 0) && m[y][x].getType() == TileType.Wall) {
						d++;
						if (rand.nextInt(d) == 0) {
							doorPos.add(new int[] {x,y});
						}
					}
				}
			}
			if (d == 0)	return;
		}

		Room curRoom = new Room(map, w, h, t, u);
		map.addRoom(curRoom);

		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				int s2 = (x < t || x > t + w) ? 1 : 0;
				int t2 = (y < u || y > u + h) ? 1 : 0;
				if( (s2 ^ t2) != 0 )
				{
					m[y][x].setType(TileType.Wall);
					curRoom.addWall(m[y][x] );
				}
				else if( (s2 & t2) != 0 )
				{
					m[y][x].setType(TileType.Wall);
					curRoom.addWall(m[y][x] );
				}
				else
				{
					m[y][x].setType(TileType.Floor);
				}
				curRoom.addWall(m[y][x]);
			}
		}

	}

	@Override
	public void generateRooms(Level map, Random rand) {
		for (int j = 0; j < 1000; j++)
			cave(map, j, rand);
	}
}
