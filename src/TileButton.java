import level.Level;
import level.Room;

import java.awt.Color;

import javax.swing.JButton;

public class TileButton extends JButton{
	private Level map;
	private Room room;
	private int x;
	private int y;
	private char symbol;
	public TileButton(Level map, int x, int y, char symbol) {
		super();
		this.map = map;
		this.x = x;
		this.y = y;
		this.symbol = symbol;
		setText(""+symbol);
		setColor();
	}
	public void setRoom(Room room) {
		this.room = room;
		setColor();
	}
	
	public void onClick() {
		boolean marked = !room.isMarked();
		room.setMarked(marked);
		setColor();
	}
	private void setColor() {
		if(symbol == '+' || symbol == '\'') {
			setBackground(Color.YELLOW);
		}
		else if(symbol == ' ') {
			setBackground(null);
		}
		else if(room != null && room.isMarked())
		{
			setBackground(Color.GREEN);
		}
		else
		{
			setBackground(Color.RED);
		}
	}
	
}
