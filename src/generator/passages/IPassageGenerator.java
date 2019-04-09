package generator.passages;

import level.Level;

import java.util.Random;

public interface IPassageGenerator {
	void generatePassages(Level map, Random rand);
}
