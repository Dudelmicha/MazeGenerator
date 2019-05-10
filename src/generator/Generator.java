package generator;

import generator.doors.IDoorGenerator;
import generator.doors.SimpleDoorGenerator;
import generator.passages.IPassageGenerator;
import generator.passages.MichasDoubleWallPassageGenerator;
import generator.passages.MichasPassageGenerator;
import generator.rooms.CaveGenerator;
import generator.rooms.IRoomGenerator;
import level.Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Random;

public class Generator {
	public Generator() { rand = new Random(); }
	public Generator(int seed) { rand = new Random(seed); }

	public Level generate(int width, int height) {
		Level map = new Level(width, height);

		roomGenerator.generateRooms(map, rand);
		map.printMap();
		doorGenerator.generateDoors(map, rand);
		map.printMap();
		passageGenerator.generatePassages(map, rand);
		map.printMap();
//		passageGenerator2.generatePassages(map, rand);
		map.printMap();
		try {
			Path path = Files.createTempFile("generated",".json");
			System.out.println(path);
			Files.write(path, map.toJSON().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	private Random rand;
	private IRoomGenerator roomGenerator = new CaveGenerator();
	private IDoorGenerator doorGenerator = new SimpleDoorGenerator();
	private IPassageGenerator passageGenerator = new MichasDoubleWallPassageGenerator();
//	private IPassageGenerator passageGenerator2 = new MichasPassageGenerator();
}
