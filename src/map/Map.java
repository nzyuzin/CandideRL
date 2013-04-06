package map;
import movement.Direction;

public final class Map {
	private static int mapsize;
	private static Containable[][] map;
	private static char floor = '.', wall = '#';
	
	Map(int size) {
		mapsize = size;
		map = new Containable[size][size];
		for(int i = 1; i < size - 1; i++)
			for(int j = 1; j < size -1; j++)
				map[i][j].charOnMap = floor;
		for(int i = 0; i < size; i++) {
			map[0][i].charOnMap = wall;
			map[size - 1][i].charOnMap = wall;
			map[i][0].charOnMap = wall;
			map[i][size - 1].charOnMap = wall;
		}
	}
	
	private static boolean isEmpty(int xcoor,int ycoor) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		if(map[xcoor][ycoor].charOnMap == floor) return true;
		return false;
	}
	
	private static void makeEmpty(int xcoor, int ycoor) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		if(map[xcoor][ycoor].charOnMap != wall) {
			map[xcoor][ycoor].x = -1;
			map[xcoor][ycoor].y = -1;
			map[xcoor][ycoor].charOnMap = floor;
		}
	}
	
	private static void setContent(int xcoor, int ycoor, Containable content) {
		assert xcoor < mapsize && xcoor >= 0 &&
				ycoor < mapsize && ycoor >= 0;
		content.x = xcoor;
		content.y = ycoor;
		map[xcoor][ycoor] = content;
	}
	
	private static Containable getContent(int xcoor, int ycoor) {
		return map[xcoor][ycoor];
	}
	
	//TODO write method that will return content of maps cell
	// to check if content is 'hitable' for creature
	
	public static boolean canMoveContent(Containable that, Direction there) {
		if(there == Direction.UP) {
			if(isEmpty(that.x,that.y + 1))return true;
				return false;
		}		
		if(there == Direction.DOWN) {
			if(isEmpty(that.x,that.y - 1)) return true;
			return false;
		}
		if(there == Direction.LEFT) {
			if(isEmpty(that.x - 1,that.y)) return true;
			return false;
		}
		if(there == Direction.RIGHT) {
			if(isEmpty(that.x + 1,that.y)) return true;
			return false;
		}
		return false;
	}
	
	public static void moveContent(Containable that,Direction there) {
		if(there == Direction.UP) {
			if(isEmpty(that.x,that.y + 1)) {
				makeEmpty(that.x, that.y++);
				setContent(that.x, that.y, that);
				return;
			}
			else
				//TODO process this properly
				return;
		}		
		if(there == Direction.DOWN) {
			if(isEmpty(that.x,that.y - 1)) {
				makeEmpty(that.x, that.y--);
				setContent(that.x, that.y, that);
				return;
			}
			else
				//TODO process this properly
				return;
		}
		if(there == Direction.LEFT) {
			if(isEmpty(that.x - 1,that.y)) {
				makeEmpty(that.x--, that.y);
				setContent(that.x, that.y, that);
				return;
			}
			else
				//TODO process this properly
				return;
		}
		if(there == Direction.RIGHT) {
			if(isEmpty(that.x + 1,that.y)) {
				makeEmpty(that.x++, that.y);
				setContent(that.x, that.y, that);
			}
			else
				//TODO process this properly
				return;
		}
	}
}
