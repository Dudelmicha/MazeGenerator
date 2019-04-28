package algorithm;

import java.util.LinkedList;
import java.util.Queue;

import level.Level;
import level.Tile;

public class BFSSearch {
	
	public static int distanceLookup(Level map, int[][] distanceMap, Tile stop) {
		return distanceMap[stop.getY()][stop.getX()];
	}
	
	public static int[][] distances(Level map, Tile start)
	{
		int[][] distanceMap = new int[map.height()][map.width()];
		Queue<Tile> q = new LinkedList<Tile>();
		q.add(start);
		while(!q.isEmpty()) {
			Tile t = q.remove();
			for(Tile n : t.getNeighbours())
			{
				if(n!=t&&n.isTraversable()) {
					int dist = Distancies.manhatten(start, n);
					if(distanceMap[n.getY()][n.getX()]==0 || dist<distanceMap[n.getY()][n.getX()] )
					{
						distanceMap[n.getY()][n.getX()] = dist;
						q.add(n);
					}
				}
			}
		}
//		printDist(distanceMap);
		return distanceMap;
	}
	
	public static void printDist(int[][] dists) {
		System.out.println("--------------");
		for (int y = 0; y < dists.length; y++) {
			for (int x = 0; x < dists[y].length; x++) {
				int c = dists[y][x];
				System.out.print(c+"\t");
			}
			System.out.print("\n");
		}	
	}
}
