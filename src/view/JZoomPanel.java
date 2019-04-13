package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class JZoomPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	@Override public void mouseClicked(MouseEvent mouseEvent) { }
	@Override public void mousePressed(MouseEvent mouseEvent) { if (mouseEvent.getButton() == 3) { pos = mouseEvent.getPoint(); drag = true;} }
	@Override public void mouseReleased(MouseEvent mouseEvent) { if (mouseEvent.getButton() == 3) { drag = false;} }
	@Override public void mouseEntered(MouseEvent mouseEvent) { }
	@Override public void mouseExited(MouseEvent mouseEvent) { }
	@Override public void mouseDragged(MouseEvent mouseEvent) {
		if (drag) {
			transform.translate((mouseEvent.getX() - pos.x)/transform.getScaleX(), (mouseEvent.getY() - pos.y)/transform.getScaleY());
			pos = mouseEvent.getPoint();
			repaint();
		}
	}
	@Override public void mouseMoved(MouseEvent mouseEvent) { pos = mouseEvent.getPoint(); }
	@Override public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		float s = 1 + mouseWheelEvent.getWheelRotation() * -0.01f;

		try {
			Point2D offset = transform.inverseTransform(pos, null);
			transform.scale(s,s);
			Point2D dst = transform.inverseTransform(pos, null);
			transform.translate(dst.getX()-offset.getX(), dst.getY()-offset.getY());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
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
		transform.setToScale(1,1);
		repaint();
	}

	final AffineTransform transform = new AffineTransform();
	private Point pos;
	private boolean drag = false;
}
