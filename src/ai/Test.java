package ai;
import java.awt.Point;
import java.util.LinkedList;

public class Test {
	/* TODO: checked must be 0 initially to signal NOT set value, or something different
	 */
	static boolean[][] realmap; // will store real map used
	static boolean[][] rigid, checked; //initially false
	static int[][] cost;
	static Point hero;
	static Point current;// this is useless?
	static boolean next, genesis; //true if there is next node available
	static LinkedList<Point> Nodes = new LinkedList<Point>();
	static Point nextOne;
	static int[] generationOffspring;
	
	public static void main(String[] args) {
		genesis = true;
		makeMap(10, 10); // init cost[][] and rigid[][];
		//TODO: out-source into makeMap the generationOffspring initiallisation
		generationOffspring = new int[rigid.length * rigid[0].length];
		heroStartPosition(0, 0);
		//rigid terrain spartan test:
		bfs(hero.x, hero.y, 0, true); //TODO: heroStartPosition must be inital bfs!!!!
		printMap();
		System.out.println("test: "+ outsideArrayOrRigid(0,0));
		System.out.println("node size: "+Nodes.size());
		//LinkedList test:
		/* Test was successful!
		Nodes.add(new Point(0,0));
		Nodes.add(new Point(0,1));
		Nodes.removeFirst();
		Nodes.add(new Point(8,8)); */
		while (!Nodes.isEmpty()) {System.out.println(Nodes.removeFirst());}
		//System.out.println("sup");
	}
	
	public static void bfs(int x, int y, int g, boolean next) {
		//generationOffspring[g] = 0;
		check(x, y + 1, cost[x][y], g); 	//NORTH
		check(x + 1, y + 1, cost[x][y], g);	//NORTHEAST
		check(x + 1, y, cost[x][y], g);		//EAST
		check(x + 1, y - 1, cost[x][y], g);	//SOUTHEAST
		check(x, y - 1, cost[x][y], g);		//SOUTH
		check(x - 1, y - 1, cost[x][y], g);	//SOUTHWEST
		check(x - 1, y, cost[x][y], g);		//WEST
		check(x - 1, y + 1, cost[x][y], g);	//NORTHWEST
		rigid[x][y] = true;
		if (next) { // is it time for progeny?
			System.out.println("reached with g:"+g+ " go:"+generationOffspring[g]);
			//genesis = false; //creates world just once
			next = false;
			if (generationOffspring[g] != 0) {//no offspring?
				for (int i = 0; i < generationOffspring[g]; i++) {
					nextOne = Nodes.removeFirst();
					bfs(nextOne.x, nextOne.y, g + 1, false);// g++ != g + 1 ._.
				}
				if(!Nodes.isEmpty()) {
					nextOne = Nodes.removeFirst();
					//kill record of "adam"
					generationOffspring[g + 1] -= 1;
					//this marks the beginning of world regeneration:
					bfs(nextOne.x, nextOne.y, g + 1, true);
				}
			}
		}
		//now randomly call working nodes
	}
	/*
	 * checks coordinates a and b and applies bfs to them, only to use within bfs.
	 */
	public static void check(int a, int b, int costOfCaller, int g) { // uses bfs' x y, 
		//TODO: generation is salvation
		if (!outsideArrayOrRigid(a, b)) {			
			if (cost[a][b] != 0) {
				if(costOfCaller + 1 < cost[a][b]) {
					cost[a][b] = costOfCaller + 1;
					//add to Nodes stack to follow later:
					Nodes.add(new Point(a, b));
					//every "add" is a "children" in the current generation!
					generationOffspring[g]++;
				}
			} else {
				//add to Nodes stack to follow later:
				Nodes.add(new Point(a, b));
				//every "add" is a "children" in the current generation!
				generationOffspring[g]++;
				cost[a][b] = costOfCaller + 1;
			}
		}
	}
	public static void heroStartPosition(int x, int y) {
		hero = new Point(x, y);
		current = new Point(x, y);
		//cost[x][y] = 0; //not necessary, initial int arrays are of 0's
	}
	public static boolean outsideArrayOrRigid(int x, int y) {
		try {
			return rigid[x][y]; //in array, either "walk-able" or not
		} catch (ArrayIndexOutOfBoundsException ex) {
			return true; //not in array
		}
	}
	/**
	 * prints rigid/cost maps _intuitively_: 
	 * "bottom left" is x, y = 0; 
	 * "up right" is x, y = their respective max value
	 */
	public static void printMap() {
		//map.length is "x" value!  
		//map.length for first dim, map[0].length for second, also map[3].length ...)
		for (int i = rigid[0].length - 1; i > -1; i--) {
			for (int n = 0; n < rigid.length; n++) {
				if (rigid[n][i]) { // the n/i reversing WILL be the final bug fix
					System.out.print(cost[n][i] + " "); //read from cost array
				} else {
					System.out.print(cost[n][i] + " ");
				}
			}
			System.out.print("   ");
			for (int n = 0; n < rigid.length; n++) {
				if (rigid[n][i]) { // the n/i reversing WILL be the final bug fix
					System.out.print('#' + " "); //read from cost array
				} else {
					System.out.print('.' + " ");
				}
			}	
			System.out.println();
		}
	}
	public static void makeMap(int dim0, int dim1) {
		rigid = new boolean[dim0][dim1];
		checked = new boolean[dim0][dim1];
		cost = new int[dim0][dim1];
	}
}
