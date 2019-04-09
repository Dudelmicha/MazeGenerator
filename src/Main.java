import level.Level;

public class Main {
	


	public static void main(String[] args) {
		GeneratorWithoutDoors generator = new GeneratorWithoutDoors();
		
		Level map = generator.createEmptyMap();
		generator.fillMap(map);
		generator.printMap(map);
		MainWindow mw = new MainWindow(generator, map);
	}

	
}
