package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class JZoomPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	@Override public void mouseClicked(MouseEvent mouseEvent) { }
	@Override public void mousePressed(MouseEvent mouseEvent) { if (mouseEvent.getButton() == 3) { pos = mouseEvent.getPoint(); drag = true;} }
	@Override public void mouseReleased(MouseEvent mouseEvent) { if (mouseEvent.getButton() == 3) { drag = false;} }
	@Override public void mouseEntered(MouseEvent mouseEvent) { }
	@Override public void mouseExited(MouseEvent mouseEvent) { }
	@Override public void mouseDragged(MouseEvent mouseEvent) {
		if (drag) {
			transform.translate(mouseEvent.getX() - pos.x, mouseEvent.getY() - pos.y);

			/// TODO: make nice! (fix borders for zoom and call transform.translate() only one time)
			Dimension preferred = getPreferredSize();
			float w2 = (preferred.width - getWidth()) * 0.5f;
			if (transform.getTranslateX() < -w2) transform.translate(-w2-transform.getTranslateX(),0);
			else if (transform.getTranslateX() > w2) transform.translate(w2-transform.getTranslateX(), 0);

			float h = preferred.height - getHeight();
			if (transform.getTranslateY() > 0) transform.translate(0, -transform.getTranslateY());
			else if (transform.getTranslateY() < -h) transform.translate(0, -h-transform.getTranslateY());

			pos = mouseEvent.getPoint();
			repaint();
		}
	}
	@Override public void mouseMoved(MouseEvent mouseEvent) { }
	@Override public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		float s = 1 + mouseWheelEvent.getWheelRotation() * 0.01f;
		transform.scale(s,s);
		repaint();
	}


	public JZoomPanel() {
		super();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setColor(Color.WHITE);
		g.fillRect(0,0, getWidth(), getHeight());

		AffineTransform t = g.getTransform();
		g.setTransform(transform);

		super.paint(graphics);

		g.setTransform(t);
	}

	public void resetViewport() {
		transform.setToTranslation(0,0);
		repaint();
	}

	final AffineTransform transform = new AffineTransform();
	private Point pos;
	private boolean drag = false;
}
