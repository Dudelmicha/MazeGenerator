import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;

public class MainWindow {
	private JFrame frame;
	private JPanel mainPanel;
	private JScrollPane mapView;
	private Map currentMap;
	public MainWindow(GeneratorWithoutDoors generator, Map map) {
		currentMap = map;
		mapView = createMapView(currentMap); 
		mainPanel = new JPanel(new GridLayout(1, 1));
		mainPanel.add(mapView,0,0);
		JButton nextFloor = new JButton("nex tFloor");
		nextFloor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				currentMap = new Map(currentMap.getM(), currentMap.getRooms().stream().filter(x -> x.isMarked())); 
				generator.fillMap(currentMap);
				generator.printMap(currentMap);
				mapView = createMapView(currentMap); 
				mainPanel.add(mapView,0,0);
				mainPanel.doLayout();

				mainPanel.doLayout();
				mainPanel.repaint();
				frame.repaint();
			}
		});
		frame = new JFrame();
		frame.setLayout(new BorderLayout(10, 10));
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(nextFloor, BorderLayout.SOUTH);
		frame.setSize(1500, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private JScrollPane createMapView(Map map) {
		int height = map.getM().length;
		int width = map.getM()[0].length;
		
		JPanel mapViewGrid = new JPanel(new GridLayout(height, width));
		TileButton[][] buttons = new TileButton[height][width]; 
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				char symbol = map.getM()[y][x].getSymbol();
				TileButton button = new TileButton(map,x,y,symbol);
				buttons[y][x] = button;
				button.setSize(32,32);
				mapViewGrid.add(button);
			}	
		}
		for(Room room : map.getRooms())
		{
			for (int y = room.getU() - 1; y < room.getU() + room.getH() + 2; y++) {
				for (int x = room.getT() - 1; x < room.getT() + room.getW() + 2; x++) {
					final int curX = x;
					final int curY = y;
					if(room.isMarked()) {
						buttons[y][x].setBackground(Color.green);
					}
					else
					{
						buttons[y][x].setBackground(Color.red);
					}
					buttons[y][x].setRoom(room);
					buttons[y][x].addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							buttons[curY][curX].onClick();
							mainPanel.removeAll();
							mapView = createMapView(map); 
							mainPanel.add(mapView,0,0);
							mainPanel.doLayout();

							mainPanel.doLayout();
							mainPanel.repaint();
							frame.repaint();
						}
					});
				}
			}
		}
		JScrollPane mapViewScroll = new JScrollPane(mapViewGrid);
		
		return mapViewScroll;
	}
}
