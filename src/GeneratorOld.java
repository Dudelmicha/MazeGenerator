import level.Level;
import level.Room;
import level.Tile;
import level.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This Generator iss derived from Link: https://gist.github.com/munificent/b1bcd969063da3e6c298be070a22b604
public class GeneratorOld {
	final static int H = 40;
	final static int W = 80;
	final static Random rand = new Random();
	
	public GeneratorOld() {
		
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
					if (m[y][x].isTileType(TileType.Floor))
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
						if (((s2 ^ t2) != 0) && m[y][x].isTileType(TileType.Wall)) {
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
			
//			for (int y = u - 1; y < u + h + 2; y++) {
//				for (int x = t - 1; x < t + w + 2; x++) {
//					int s2 = (x < t || x > t + w) ? 1 : 0;
//					int t2 = (y < u || y > u + h) ? 1 : 0;
//					m[y][x] = (s2 & t2) != 0 ? '!' : (s2 ^ t2) != 0 ? '#' : '.';
//				}
//			}
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
						m[y][x].setType(TileType.Door);
						curRoom.addWall(m[y][x] );
					}
					else 
					{
						m[y][x].setType(TileType.Floor);
					}
					curRoom.addWall(m[y][x]);
				}
			}
		
		//add content
		//for (int j = 0; j < (s != 0 ? 1 : g(6) + 1); j++)
		//	m[g(h) + u][g(w) + t] = s != 0 ? '@' : g(4) == 0 ? '$' : 65 + g(62);
	}

	private boolean neighbourIs(int[][] m, int j, int k, int c) {
		int up = m[j-1][k];
		int down = m[j+1][k];
		int left = m[j][k-1];
		int right = m[j][k+1];
		return up == c || down == c || left == c || right == c;
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
