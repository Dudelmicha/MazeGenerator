import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Union<T> {
	private T t;
	private List<Union<T>> children = new ArrayList<Union<T>>();
	private int rank;
	private Union<T> root;
	public Union(T t) {
		this.t=t;
		this.root=this;
	}
	public Union<T> unite(Union<T> other)
	{
		if(other.rank > rank )
		{
			return other.unite(this);
		}
		else
		{
			children.add(other);
			rank += other.rank;
			other.root = this;
			return this;
		}
	}
	public Union<T> find(T t)
	{
		if(this.t == t)
		{
			return this;
		}
		else
		{
			Optional<Union<T>> res = children.stream().filter(x -> x.find(t) == t).findFirst();
			return res.orElseGet(null);
		}
	}
	public Union<T> getRoot() {
		while(root.root != root)
		{
			root = root.root;
		}
		return root;
	}
	
	public T getT() {
		return t;
	}
	
	public List<T> getAllT(){
		ArrayList<T> ts = new ArrayList<T>();
		ts.add(t);
		for( Union<T> child : children) {
			if(!ts.contains(child.t))
			{
				ts.add(child.t);
				child.getAllT(ts);
			}
		}
		return ts;
	}
	private List<T> getAllT(ArrayList<T> ts) {
		ts.add(t);
		for( Union<T> child : children) {
			if(!ts.contains(child.t))
			{
				ts.add(child.t);
				child.getAllT(ts);
			}
		}
		return ts;
	}
	public int getRank() {
		return rank;
	}
}
