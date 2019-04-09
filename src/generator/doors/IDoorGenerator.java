package generator.doors;

import level.Level;

import java.util.Random;

public interface IDoorGenerator {
	void generateDoors(Level map, Random rand);
}
