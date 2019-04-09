package algorithm;


import level.Coordinate;
import level.Tile;
import level.Level;


import java.util.ArrayDeque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar {

	private static float sqrDistance(Coordinate c1, Coordinate c2) {
		int deltaX = c2.x - c1.x;
		int deltaY = c2.y - c1.y;
		return deltaX*deltaX + deltaY*deltaY;
	}

	private static class Entry {
		Entry(Coordinate pos, Coordinate prev, float dist) {
			position = pos;
			previous = prev;
			distance = dist;
		}
		Coordinate position;
		Coordinate previous;
		float distance;
	}

	/**
	 * calculates the distance map for every tile you can reach from the starting tile
	 * @param map the Level to work on
	 * @param origin the origin from which the distance is calculated
	 * @return returns an int[][] with the distances from origin for every tile or -1
	 */
	public static int[][] distances(Level map, Tile origin) {
		int[][] distanceMap = new int[map.width()][map.height()];

		Queue<Entry> tileQueue = new ArrayDeque<>();

		// add start Point;
		Coordinate startPos = new Coordinate(origin.getX(), origin.getY());
		tileQueue.add(new Entry(startPos, startPos, sqrDistance(origin.position(), origin.position())));

		while (!tileQueue.isEmpty()) {
			Entry current = tileQueue.poll();

			// update distance
			Tile currentTile = map.tile(current.position);
			if (!currentTile.isTraversable()) {
				distanceMap[current.position.x][current.position.y] = Integer.MAX_VALUE;
				continue;
			}
			distanceMap[current.position.x][current.position.y] = 1 + distanceMap[current.previous.x][current.previous.y];

			List<Tile> neighbours = currentTile.getNeighbours();
			for (Tile t : neighbours) {
				if (distanceMap[t.getX()][t.getY()] == 0) {
					Coordinate pos = new Coordinate(t.getX(), t.getY());
					tileQueue.add(new Entry(pos, current.position, 0));
				}
			}
		}
		return distanceMap;
	}

	public static int distance(Level map, Tile start, Tile stop) {
		int[][] distanceMap = distances(map, stop);
		return distanceMap[start.getX()][start.getY()];
	}

	public static int distanceApprox(Level map, Tile start, Tile stop) {
		int[][] distanceMap = new int[map.width()][map.height()];

		PriorityQueue<Entry> tileHeap = new PriorityQueue<>(100, (a, b) -> (int) Math.signum(a.distance - b.distance));

		// add start Point;
		Coordinate startPos = new Coordinate(start.getX(), start.getY());
		tileHeap.add(new Entry(startPos, startPos, sqrDistance(start.position(), stop.position())));

		while (!tileHeap.isEmpty()) {
			Entry current = tileHeap.poll();

			// update distance
			Tile currentTile = map.tile(current.position);
			if (!currentTile.isTraversable()) {
				distanceMap[current.position.x][current.position.y] = Integer.MAX_VALUE;
				continue;
			}
			distanceMap[current.position.x][current.position.y] = 1 + distanceMap[current.previous.x][current.previous.y];

			// success
			if (current.position.x == stop.getX() && current.position.y == stop.getY())
				return distanceMap[current.position.x][current.position.y];

			List<Tile> neighbours = map.tile(current.position).getNeighbours();
			for (Tile t : neighbours) {
				if (distanceMap[t.getX()][t.getY()] == 0) {
					Coordinate pos = new Coordinate(t.getX(), t.getY());
					tileHeap.add(new Entry(pos, current.position, sqrDistance(pos, stop.position())));
				}
			}
		}
		return -1;
	}
}
