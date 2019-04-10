import generator.Generator;
import level.Level;

public class Main {
	


	public static void main(String[] args) {
		Generator generator = new Generator();
		Level map = generator.generate(60, 40);
		MainWindow mw = new MainWindow(generator, map);
	}

	
}
