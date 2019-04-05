import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class Main {
	


	public static void main(String[] args) {
		Generator generator = new Generator();
		
		Map map = generator.createEmptyMap();
		for (int j = 0; j < 1000; j++)
			generator.cave(map,j);
		generator.printMap(map);
		MainWindow mw = new MainWindow(generator, map);
	}

	
}
