package view;

import level.Level;
import level.Tile;

import javax.swing.*;
import java.awt.*;

public class JLevelView extends JPanel {
	public JLevelView(Level level) { this(level,32); }
	public JLevelView(Level level, int tileSize ) {
		this.level = level;
		this.tileSize = tileSize;
		this.panelWidth = tileSize * level.width();
		this.panelHeight = tileSize * level.height();
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (level == null) return;
		for (int x = 0; x < level.width(); ++x) {
			for (int y = 0; y < level.height(); ++y) {
				drawTile(graphics, x, y, level.tile(x,y));
			}
		}
	}

	private void drawTile(Graphics g, int x, int y, Tile tile) {
		Color c = g.getColor();
		switch (tile.getType()) {
			case None:
				g.setColor(Color.BLACK);
				break;
			case Wall:
				g.setColor(Color.DARK_GRAY);
				break;
			case Floor:
				g.setColor(Color.LIGHT_GRAY);
				break;
			case Door:
				g.setColor(Color.BLUE);
				break;
		}
		g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
		g.setColor(c);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(panelWidth, panelHeight);
	}

	public void setLevel(Level level) { this.level = level; }

	private Level level;
	private int tileSize;
	private int panelWidth;
	private int panelHeight;
}
