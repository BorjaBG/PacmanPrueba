
public class Fantasma {
	private int[][] myMaze;

	private int x, y;
	private int dirX = 1;
	private int dirY = 0;
	private boolean lastChoiceX = false;

	private int speed = 6;
	//private comprobarpos posicion = new comprobarpos();
	/**
	 * Ghost constructor comment.
	 */
	public Fantasma() {
		super();
	}
	
	// Genera el camino que seguira el fantasma
	private void generatePath(int x, int y, int pX, int pY) {
		int oldDirX = dirX;
		int oldDirY = dirY;

		if (x > pX) {
			dirX = -1;
		}
		if (x < pX) {
			dirX = 1;
		}

		if (y > pY) {
			dirY = -1;
		}
		if (y < pY) {
			dirY = 1;
		}

		if (dirX == 0 && dirY == 0) {
			dirX = -oldDirX;
			dirY = -oldDirY;

		}

		if (dirX != 0 && myMaze[x + dirX][y] == Maze.WALL) {
			dirX = 0;
		}

		if (dirY != 0 && myMaze[x][y + dirY] == Maze.WALL) {
			dirY = 0;
		}
		if (dirX == 0 && dirY == 0) {
			dirX = -oldDirX;
			dirY = -oldDirY;

		}

		if (dirX != 0 && dirY != 0) {
			if (lastChoiceX) {
				dirX = 0;

			} else {
				dirY = 0;
			}
			lastChoiceX = !lastChoiceX;
		}

		if (dirX != 0 && dirY != 0) {
			System.out.println("¡¿Que?!");
		}
	

	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	// Comprueba si tiene opcion de llegar a Pacman
	private boolean hasChoice() {
		if (x % Main.PACMAN_SIZE == 0 && y % Main.PACMAN_SIZE == 0) {
			int mazeX = x / Main.PACMAN_SIZE;
			int mazeY = y / Main.PACMAN_SIZE;

			int score = myMaze[mazeX][mazeY - 1] + myMaze[mazeX + 1][mazeY] * 2 + myMaze[mazeX][mazeY + 1] * 4 + myMaze[mazeX - 1][mazeY] * 8;

			if (score == 14 || score == 13 || score == 11 || score == 7) {
				dirX = -dirX;
				dirY = -dirY;
				return false;
			}

			if (score == 5 || score == 10) {
				return false;
			}

			return true;

		}
		return false;
	}
	public void move(int pacX, int pacY) {

		if (hasChoice()) {
			int mazeX = x / Main.PACMAN_SIZE;
			int mazeY = y / Main.PACMAN_SIZE;

			generatePath(mazeX, mazeY, pacX, pacY);
			

		}

		x += dirX * speed;
		y += dirY * speed;
		

	}

	public void setMaze(int[][] maze) {
		myMaze = new int[maze.length][maze[0].length];
		for (int i = 0; i < myMaze.length; i++) {
			for (int j = 0; j < myMaze[i].length; j++) {
				if (maze[i][j] == Maze.WALL) {
					myMaze[i][j] = Maze.WALL;
				} else {
					myMaze[i][j] = Maze.EMPTY_SQUARE;
				}
			}
		}
	}
	public void setXY(int anX, int aY) {
		x = anX * Main.PACMAN_SIZE;
		y = aY * Main.PACMAN_SIZE;
		dirX = 1;
		dirY = 1;

		generatePath(anX, aY, anX, aY);

	}
	
}
