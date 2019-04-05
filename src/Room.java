
public class Room {
	private Map map;
	private int w;
	private int h;
	private int t;
	private int u;
	private boolean marked;
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public int getU() {
		return u;
	}
	public void setU(int u) {
		this.u = u;
	}
	public Room(Map map, int w, int h, int t, int u) {
		super();
		this.map = map;
		this.w = w;
		this.h = h;
		this.t = t;
		this.u = u;
	}
	public boolean isMarked() {
		return marked;
	}
	
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	
}
