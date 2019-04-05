import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class Main {
	


	public static void main(String[] args) {
		GeneratorWithoutDoors generator = new GeneratorWithoutDoors();
		
		Map map = generator.createEmptyMap();
		generator.fillMap(map);
		generator.printMap(map);
		MainWindow mw = new MainWindow(generator, map);
	}

	
}
