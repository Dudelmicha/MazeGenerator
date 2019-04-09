package generator;

import generator.doors.IDoorGenerator;
import generator.doors.SimpleDoorGenerator;
import generator.passages.IPassageGenerator;
import generator.passages.MichasPassageGenerator;
import generator.rooms.CaveGenerator;
import generator.rooms.IRoomGenerator;
import level.Level;

import java.util.Random;

public class Generator {
	public Generator() { rand = new Random(); }
	public Generator(int seed) { rand = new Random(seed); }

	public Level generate(int width, int height) {
		Level map = new Level(width, height);

		roomGenerator.generateRooms(map, rand);
		doorGenerator.generateDoors(map, rand);
		passageGenerator.generatePassages(map, rand);

		return map;
	}

	private Random rand;
	private IRoomGenerator roomGenerator = new CaveGenerator();
	private IDoorGenerator doorGenerator = new SimpleDoorGenerator();
	private IPassageGenerator passageGenerator = new MichasPassageGenerator();
}
