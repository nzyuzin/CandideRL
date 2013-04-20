package game.ai;

import game.utility.DirectionProcessor;
import game.utility.Position;
import game.utility.Direction;
import game.map.Map;

import java.util.LinkedList;

class PathFinder {
	private static boolean[][] rigid; //initially false
	private static int[][] cost; //used by bfs, is returned by getCostArray
	private static LinkedList<Position> Nodes = new LinkedList<Position>();
	private static Position nextOne;
	private static int[] generationOffspring;
	private static Position initializer;
	
	public static void init(Position target) {
		initializer = target;
		makeMap(Map.toBooleanArray());
		bfs(target.x, target.y, 0, true); //heroStartPosition must be inital bfs!!!!
	}
	
	public static Direction chooseQuickestWay(Position from) {
		int bestX = from.x;
		int bestY = from.y;
		double shortestDistance = from.distanceTo(initializer);
		
		for (int x = from.x - 1; x <= from.x + 1; x++)
			for (int y = from.y - 1; y <= from.y + 1; y++)
				if (	!outsideOfArray(x, y) &&
						rigid[x][y] != false &&
						cost[x][y] <= cost[bestX][bestY] &&
						initializer.distanceTo(new Position(x, y)) < shortestDistance) {
					bestX = x;
					bestY = y;
				}
		
		return DirectionProcessor.getDirectionFromPositions(from, new Position(bestX, bestY));
	}
	
	private static void bfs(int x, int y, int g, boolean next) {
		check(x, y + 1, cost[x][y], g); 	//NORTH
		check(x + 1, y + 1, cost[x][y], g);	//NORTHEAST
		check(x + 1, y, cost[x][y], g);		//EAST
		check(x + 1, y - 1, cost[x][y], g);	//SOUTHEAST
		check(x, y - 1, cost[x][y], g);		//SOUTH
		check(x - 1, y - 1, cost[x][y], g);	//SOUTHWEST
		check(x - 1, y, cost[x][y], g);		//WEST
		check(x - 1, y + 1, cost[x][y], g);	//NORTHWEST
		if (next) { // is it time for progeny?
			if (generationOffspring[g] != 0) {//no offspring?
				for (int i = 0; i < generationOffspring[g]; i++) {
					nextOne = Nodes.removeFirst();
					bfs(nextOne.x, nextOne.y, g + 1, false);
				}
				if(!Nodes.isEmpty()) {
					nextOne = Nodes.removeFirst();
					generationOffspring[g + 1] -= 1;
					bfs(nextOne.x, nextOne.y, g + 1, true);
				}
			}
		}
	}
	
	/*
	 * checks coordinates a and b and applies bfs to them,
	 *  only to be used within bfs.
	 */
	
	private static void check(int a, int b, int costOfCaller, int g) { 
		/*
		 * only alternated Nodes get to procreate further children
		 */
		if (outsideOfArray(a, b))
			return;
		if (rigid[a][b] == false) {
			cost[a][b] = 999;
			return;
		}
		if (cost[a][b] != 0) {
			if(costOfCaller + 1 < cost[a][b]) {
				cost[a][b] = costOfCaller + 1;
				//add to Nodes stack to follow later:
				Nodes.add(new Position(a, b));
				//every "add" is a "children" in the current generation!
				generationOffspring[g]++;
			}
		} else {
			//add to Nodes stack to follow later:
			Nodes.add(new Position(a, b));
			//every "add" is a "children" in the current generation!
			generationOffspring[g]++;
			cost[a][b] = costOfCaller + 1;
		}
	}
	
	private static boolean outsideOfArray(int x, int y) {
		try {
			if (rigid[x][y]);
			return false; //in array, either "walk-able" or not
		} catch (ArrayIndexOutOfBoundsException ex) {
			return true; //not in array
		}
	}
	
	private static void makeMap(boolean[][] toRigid) {
		cost = new int[toRigid.length][toRigid[0].length];
		rigid = toRigid;
		//TODO: too small?
		generationOffspring = new int[toRigid.length * toRigid[0].length];
	}
	
}
