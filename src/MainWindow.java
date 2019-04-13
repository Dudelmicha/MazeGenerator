import level.Level;
import level.Room;
import level.Tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import generator.Generator;

public class MainWindow {
	private JFrame frame;
	private JPanel mainPanel;
	private JScrollPane mapView;
	private Level currentMap;
	public MainWindow(Generator generator, Level map) {
		currentMap = map;
		mapView = createMapView(currentMap); 
		mainPanel = new JPanel(new GridLayout(1, 1));
		mainPanel.add(mapView,0,0);
		JButton nextFloor = new JButton("next Floor");
		nextFloor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				currentMap = generator.generate(60, 40);
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
	private JScrollPane createMapView(Level map) {
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
		JScrollPane mapViewScroll = new JScrollPane(mapViewGrid);
		for(Room room : map.getRooms())
		{
			for (Tile tile : room.getTiles()) {
					final int x = tile.getX();;
					final int y = tile.getY();
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
							buttons[y][x].onClick();
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
	
	
		
		return mapViewScroll;
	}
}
