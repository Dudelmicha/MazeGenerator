package generator.rooms;

import level.Level;

import java.util.Random;

public interface IRoomGenerator {
	void generateRooms(Level map, Random rand);
}
