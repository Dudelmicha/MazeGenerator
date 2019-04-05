import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.ImmutableDescriptor;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Algorithm.SearchResult;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
//This Generator iss derived from Link: https://gist.github.com/munificent/b1bcd969063da3e6c298be070a22b604
public class GeneratorWithoutDoors {
	final static int H = 40;
	final static int W = 80;
	final static Random rand = new Random();
	
	public GeneratorWithoutDoors() {
		
	}

	
	private int g(int x) {
		return rand.nextInt(x);
	}

	public void cave(Map map, int s) {
		Tile[][] m = map.getM();
		int w = g(10) + 5;
		int h = g(6) + 3;
		int t = g(W - w - 2) + 1;
		int u = g(H - h - 2) + 1;

		int d = 0;
		int e = 0, f = 0;
		//check if the room would match
		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				if (m[y][x].getSymbol() == '.' )
					return;
			}
		}
		List<int[]> doorPos = new ArrayList<int[]>();
		if (s != 0) {
			//generate walls, and generate e,f (position of the door)
			for (int y = u - 1; y < u + h + 2; y++) {
				for (int x = t - 1; x < t + w + 2; x++) {
					int s2 = (x < t || x > t + w) ? 1 : 0;
					int t2 = (y < u || y > u + h) ? 1 : 0;
					if (((s2 ^ t2) != 0) && m[y][x].getSymbol() == '#') {
						d++;
						if (g(d) == 0) {
							doorPos.add(new int[] {x,y});
						}
					}
				}
			}
			if (d == 0)
				return;

		}
		
//		for (int y = u - 1; y < u + h + 2; y++) {
//			for (int x = t - 1; x < t + w + 2; x++) {
//				int s2 = (x < t || x > t + w) ? 1 : 0;
//				int t2 = (y < u || y > u + h) ? 1 : 0;
//				m[y][x] = (s2 & t2) != 0 ? '!' : (s2 ^ t2) != 0 ? '#' : '.';
//			}
//		}
		Room curRoom = new Room(map, w, h, t, u);
		map.addRoom(curRoom);

		for (int y = u - 1; y < u + h + 2; y++) {
			for (int x = t - 1; x < t + w + 2; x++) {
				int s2 = (x < t || x > t + w) ? 1 : 0;
				int t2 = (y < u || y > u + h) ? 1 : 0;
				if( (s2 ^ t2) != 0 )
				{
					m[y][x].setSymbol('#');
					curRoom.addWall(m[y][x] );
				}
				else if( (s2 & t2) != 0 )
				{
					m[y][x].setSymbol('!');
					curRoom.addWall(m[y][x] );
				}
				else 
				{
					m[y][x].setSymbol('.');
				}
				curRoom.addWall(m[y][x]);
				m[y][x].setRoom(curRoom);
			}
		}
		
		
//		
//		if (d > 0) {
//				for(int[] door : doorPos) {
//					if(neighbourIs(m,door[1],door[0],'\'')) {
//						m[door[1]][door[0]] = '\'';
//					} else if(neighbourIs(m,door[1],door[0],'+')) {
//						m[door[1]][door[0]] = '+';
//					}
//					else { 
//						m[door[1]][door[0]] = ((g(2) != 0))  ? '\'' : '+';	
//					}
//				}
//				
//		}
		
		
		//add content
		//for (int j = 0; j < (s != 0 ? 1 : g(6) + 1); j++)
		//	m[g(h) + u][g(w) + t] = s != 0 ? '@' : g(4) == 0 ? '$' : 65 + g(62);
	}

	private int neighbourIs(Tile[][] m, int j, int k, char c) {
		int up = j > 0 && m[j-1][k].getSymbol() == c ? 1 : 0;
		int down = j+1 < m.length && m[j+1][k].getSymbol()== c ? 1 : 0;
		int left = k > 0 && m[j][k-1].getSymbol()== c ? 1 : 0;
		int right = k+1 < m[0].length && m[j][k+1].getSymbol()== c ? 1 : 0;
		return up+down+left+right;
	}


	public void printMap(Map map) {
		System.out.println("--------------");
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				char c = map.getM()[y][x].getSymbol();
				System.out.print((c == '!' ? '#' : c));
				if (x == W - 1)
					System.out.print("\n");
			}
		}	
	}
	public void fillMap(Map map) {
		Tile[][] m = map.getM();
		for (int j = 0; j < 1000; j++)
			cave(map,j);
		List<Union<Room>> roomUnions = map.getRooms().stream().map(x -> new Union<Room>(x)).collect(Collectors.toList());
		int geneation = 0;
		while(roomUnions.size()>1)
		{
			geneation++;
			System.out.println(geneation);
			printMap(map);
			Union<Room> r1 = roomUnions.get(g(roomUnions.size()));
			Union<Room> r2 = roomUnions.get(g(roomUnions.size()));
			if(r1.getRoot() != r2.getRoot()) { //merge
				List<Tile> sharedWalls = new ArrayList<Tile>();
				for (Room room1 : r1.getAllT()) {
					for(Tile wall1 : room1.getWalls()) {
						for (Room room2 : r2.getAllT()) {
							sharedWalls.addAll(room2.getWalls().stream()
									.filter(
											x -> x.equals(wall1)
											&& neighbourIs(m, x.getY(), x.getX(), '.') > 1
											&& x.getSymbol() == '#')
									.collect(Collectors.toList()));
						}
					}
				}
				if(!sharedWalls.isEmpty()) {
					roomUnions.remove(r1);
					roomUnions.remove(r2);
					roomUnions.add(r1.unite(r2));
					Tile newDoor = sharedWalls.get(g(sharedWalls.size()));
					newDoor.setSymbol('T');
					List<Tile> neighbours = newDoor.getNeighbours();
					neighbours.forEach(x -> x.getRoom().addDoor(newDoor));
				}
				else //try to merge with other stuff than
				{
					roomUnions.remove(r1);
					roomUnions.add(r1); //add on the end
				}
			}
			else //should have been merged
			{
				roomUnions.remove(r1);
				roomUnions.remove(r2);
				roomUnions.add(r1.unite(r2));
			}
		}
		boolean waysAdded = false;
//		do
//		{
		GraphBuilder<Tile, Integer> builder = GraphBuilder.<Tile,Integer>create();
		Set<Tile> allDoors = new HashSet<Tile>();
		for (Room room : map.getRooms()) {
			Set<Tile> doors = room.getDoors();
			allDoors.addAll(doors);
			for (Tile door : doors) {
				for (Tile door2 : doors) {
					if(door != door2)
					{
						builder.connect(door).to(door2).withEdge(door.getDistance(door2));
					}
				}
			}
		}
		HipsterGraph<Tile, Integer> graph = builder.createUndirectedGraph();
		
			waysAdded = false;
			for (Tile door : allDoors) {
				SearchProblem<Integer, Tile, WeightedNode<Integer, Tile, Double>> p = GraphSearchProblem
                        .startingFrom(door)
                        .in(graph)
                        .takeCostsFromEdges()
                        .build();
M1:for (Tile door2 : allDoors) {
					if(door != door2) {
				
		                Algorithm<Integer, Tile, WeightedNode<Integer, Tile, Double>>.SearchResult result = Hipster.createAStar(p).search(door2);
		                
		                int directWayFCost = door.getDistance(door2);
		                if(result.getGoalNode().getCost() > directWayFCost*3 ) {
		                	System.out.println(directWayFCost*3);
		                	System.out.println((int)Math.round(result.getGoalNode().getCost()));
		                	builder.connect(door).to(door2).withEdge((int)Math.round(result.getGoalNode().getCost()));
		                	waysAdded = true;
		                	buildWay(map,door,door2);
		                	break M1;
		                }
					}
				}
			}
			graph = builder.createUndirectedGraph();
//		}
//		while(waysAdded);
	}

	private void buildWay(Map map, Tile door, Tile door2) {
		Tile[][] m = map.getM();
		Room room1 = door.getNeighbours().get(g(door.getNeighbours().size())).getRoom();
		Room room2 = door2.getNeighbours().get(g(door2.getNeighbours().size())).getRoom();
		
		Tile startTile = room1.getTiles().get(g(room1.getTiles().size()));
		Tile endTile = room2.getTiles().get(g(room2.getTiles().size()));
		if(startTile.getX() == endTile.getX()) {
			Tile edge = buildVertical(m,startTile, endTile);
		} else if(startTile.getY() == endTile.getY()) {
			Tile edge = buildHorizontal(m,startTile, endTile);
		} else if(g(2)==0) { //first vertical
			Tile edge = buildVertical(m,startTile, endTile);
			Tile end = buildHorizontal(m,edge, endTile);
		}else { //first horizontal
			
			Tile edge = buildHorizontal(m,startTile, endTile);
			Tile end = buildVertical(m,edge, endTile);
		}
		
		printMap(map);
	}


	private Tile buildHorizontal(Tile[][] m, Tile startTile, Tile endTile) {

		int y = startTile.getY();
		if(startTile.getX() < endTile.getX()) //upward
		{
			for(int x=startTile.getX(); x<endTile.getX(); x++)
			{
				final Tile tile = m[y][x];
				List<Tile> neighours = tile.getNeighbours3x3();
				char symbol = tile.getSymbol();
				switch (symbol) {
				case '.':
				case 'T':
					break;
				case '#':
					
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
							if(neighour.getRoom()!=null)neighour.getRoom().addDoor(tile);
						}
					}
					tile.setSymbol('T');
					if(x+1<endTile.getX()) {						
						m[y][x+1].setSymbol('X');
					}
					break;
				case 'X':
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#');
						}
					}
					if(neighbourIs(m,y,x,'#')>1&&neighbourIs(m,y,x,'T')==0) {
						tile.setSymbol('T'); 
						if(tile.getRoom()!=null)tile.getRoom().addDoor(tile);
					}else
					{						
						tile.setSymbol('.');
					}
					if(x+1<endTile.getX()) {						
						m[y][x+1].setSymbol('X');
					}
					break;
				default:
					break;
				}
			}
			
		}
		else //downward
		{
			for(int x=startTile.getX(); x>endTile.getX(); x--)
			{
				final Tile tile = m[y][x];
				List<Tile> neighours = tile.getNeighbours3x3();
				char symbol = tile.getSymbol();
				switch (symbol) {
				case '.':
				case 'T':
					break;
				case '#':
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
							if(neighour.getRoom()!=null)neighour.getRoom().addDoor(tile);
						}
					}
					tile.setSymbol('T');
					if(x-1>endTile.getX()) {						
						m[y][x-1].setSymbol('X');
					}
					break;
				case 'X':
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#');
						}
					}
					if(neighbourIs(m,y,x,'#')>1&&neighbourIs(m,y,x,'T')==0) {
						tile.setSymbol('T'); 
						if(tile.getRoom()!=null)tile.getRoom().addDoor(tile);
					}else
					{						
						tile.setSymbol('.');
					}
					if(x-1>endTile.getX()) {						
						m[y][x-1].setSymbol('X');
					}
					break;
				default:
					break;
				}
			}
		}
		return m[y][endTile.getX()];
	}


	private Tile buildVertical(Tile[][] m, Tile startTile, Tile endTile) {
		
		int x = startTile.getX();
		if(startTile.getY() < endTile.getY()) //upward
		{
			for(int y=startTile.getY(); y<endTile.getY(); y++)
			{
				final Tile tile = m[y][x];
				List<Tile> neighours = tile.getNeighbours3x3();
				char symbol = tile.getSymbol();
				switch (symbol) {
				case '.':
				case 'T':
					break;
				case '#':
					
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
							if(neighour.getRoom()!=null)neighour.getRoom().addDoor(tile);
						}
					}
					tile.setSymbol('T');
					if(y+1<endTile.getY()) {						
						m[y+1][x].setSymbol('X');
					}
					break;
				case 'X':
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
							if(neighour.getRoom()!=null)neighour.getRoom().addDoor(tile);
						}
					}
					if(neighbourIs(m,y,x,'#')>1&&neighbourIs(m,y,x,'T')==0) {
						tile.setSymbol('T'); 
						if(tile.getRoom()!=null)tile.getRoom().addDoor(tile);
					}
					else
					{						
						tile.setSymbol('.');
					}
					if(y+1<endTile.getY()) {						
						m[y+1][x].setSymbol('X');
					}
					break;
				default:
					break;
				}
			}
			
		}
		else //downward
		{
			for(int y=startTile.getY(); y>endTile.getY(); y--)
			{
				final Tile tile = m[y][x];
				List<Tile> neighours = tile.getNeighbours3x3();
				char symbol = tile.getSymbol();
				switch (symbol) {
				case '.':
				case 'T':
					break;
				case '#':
					
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
							if(neighour.getRoom()!=null)neighour.getRoom().addDoor(tile);
						}
					}
					tile.setSymbol('T');
					if(y-1>endTile.getY()) {						
						m[y-1][x].setSymbol('X');
					}
					break;
				case 'X':
					if(neighbourIs(m,y,x,'#')>1&&neighbourIs(m,y,x,'T')==0) {
						tile.setSymbol('T'); 
						if(tile.getRoom()!=null)tile.getRoom().addDoor(tile);
					}else
					{						
						tile.setSymbol('.');
					}
					for(Tile neighour : neighours)
					{
						if(neighour.getSymbol()==' '||neighour.getSymbol()==0)
						{
							neighour.setSymbol('#'); 
						}
					}
					if(y-1>endTile.getY()) {						
						m[y-1][x].setSymbol('X');
					}
					break;
				default:
					break;
				}
			}
		}
		return m[endTile.getY()][x];
	}


	public Map createEmptyMap() {
		Tile[][] m = new Tile[H][W];
		Map map = new Map(m);
		for (int y = 0; y < H; y++) {
			m[y] = new Tile[W];
			for (int x = 0; x < W; x++) {
				m[y][x] = new Tile(map, x, y);
			}
		}
		return map;
	}
}
