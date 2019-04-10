package algorithm;

import level.Tile;

public class Distancies {

	public Integer manhatten(Tile tile, Tile tile2) {
		return Math.abs(tile.getX()-tile2.getX()) + Math.abs(tile.getY()-tile2.getY());
	}
}
