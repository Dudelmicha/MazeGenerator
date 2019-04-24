import generator.Generator;
import view.JLevelView;
import view.JZoomPanel;

import javax.swing.*;
import java.awt.*;


public class MazeApp {
	static JLevelView levelView;
	static JZoomPanel zoomPanel;
	static JPanel controlPanel;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(1400, 900);

		Generator levelGenerator = new Generator();
		levelView = new JLevelView(levelGenerator.generate(80, 60));

		zoomPanel = new JZoomPanel();
		zoomPanel.add(levelView);

		frame.add(zoomPanel, BorderLayout.CENTER);

		controlPanel = new JPanel(new GridLayout());

		JButton createLevelButton = new JButton("Create Level");
		createLevelButton.addActionListener(actionEvent -> {
			levelView.setLevel(levelGenerator.generate(80,60));
			zoomPanel.repaint();
		});

		controlPanel.add(createLevelButton);

		frame.add(controlPanel, BorderLayout.EAST);
		frame.setVisible(true);
		zoomPanel.setZoomToFit(levelView.getPreferredSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
