

import java.util.Random;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2003 10:25:50 PM)
 * @author: Administrator
 */
public class Maze
{

	static final private int height = 9;
	static final private int width = 11;

	public Main mazearray[][] = new Main[width][height];
	private static int puntos = 0;
	private Random random = new Random();

	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	public static final int EMPTY_SQUARE = 0;
	public static final int WALL = 1;
	public static final int PELLET = 2;
	/**
	 * Maze constructor comment.
	 */
	public Maze()
	{
		super();
		for (int x = 0; x < mazearray.length; x++)
		{
			for (int y = 0; y < mazearray[x].length; y++)
			{
				mazearray[x][y] = new Main();
				if (x == 0 || x == mazearray.length - 1 || y == 0 || y == mazearray[x].length - 1)
				{
					mazearray[x][y].visited = true;
					puntos++;
				}
			}
		}

		generateMaze(1, 1);

	}
	public void generateMaze(int x, int y)
	{
		mazearray[x][y].visited = true;
		mazearray[x][mazearray[x].length - y - 1].visited = true;

		int outDirection = random.nextInt(4);

		int outX;
		int outY;

		for (int i = 0; i < 4; i++)
		{
			outDirection = (outDirection + i) % 4;

			outX = newX(x, outDirection);
			outY = newY(y, outDirection);
			if (!mazearray[outX][outY].visited)
			{
				mazearray[x][y].walls[outDirection] = false;
				mazearray[x][mazearray[x].length - y - 1].walls[mirrorDirection(outDirection)] = false;

				generateMaze(outX, outY, reverseDirection(outDirection));
			}
		}

	}
	public void generateMaze(int x, int y, int inDirection)
	{
		mazearray[x][y].visited = true;
		mazearray[x][mazearray[x].length - y - 1].visited = true;

		mazearray[x][y].walls[inDirection] = false;
		mazearray[x][mazearray[x].length - y - 1].walls[mirrorDirection(inDirection)] = false;

		int outDirection = random.nextInt(4);

		int outX;
		int outY;

		for (int i = 0; i < 4; i++)
		{
			outDirection = (outDirection + i) % 4;

			outX = newX(x, outDirection);
			outY = newY(y, outDirection);
			if (!mazearray[outX][outY].visited && outY <= mazearray[x].length / 2)
			{
				mazearray[x][y].walls[outDirection] = false;
				mazearray[x][mazearray[x].length - y - 1].walls[mirrorDirection(outDirection)] = false;

				generateMaze(outX, outY, reverseDirection(outDirection));
			}
		}

	}

	public int[][] generateMazeArray()
	{
		int[][] array = new int[width * 2 + 1][height * 2 + 1];

		int x, y;

		for (x = 0; x < mazearray.length; x++)
		{
			for (y = 0; y < mazearray[x].length; y++)
			{
				// top row
				if (mazearray[x][y].walls[NORTH] || mazearray[x][y].walls[WEST])
				{
					array[x * 2][y * 2] = WALL;
				}

				if (mazearray[x][y].walls[NORTH])
				{
					array[x * 2 + 1][y * 2] = WALL;
				}

				if (mazearray[x][y].walls[NORTH] || mazearray[x][y].walls[EAST])
				{
					array[x * 2 + 2][y * 2] = WALL;
				}

				// middle row
				if (mazearray[x][y].walls[WEST])
				{
					array[x * 2][y * 2 + 1] = WALL;
				}

				// always open - ok, not on borders
				array[x * 2 + 1][y * 2 + 1] = PELLET;

				if (mazearray[x][y].walls[EAST])
				{
					array[x * 2 + 2][y * 2 + 1] = WALL;
				}

				// bottom row

				if (mazearray[x][y].walls[SOUTH] || mazearray[x][y].walls[WEST])
				{
					array[x * 2][y * 2 + 2] = WALL;
				}

				if (mazearray[x][y].walls[SOUTH])
				{
					array[x * 2 + 1][y * 2 + 2] = WALL;
				}

				if (mazearray[x][y].walls[SOUTH] || mazearray[x][y].walls[EAST])
				{
					array[x * 2 + 2][y * 2 + 2] = WALL;
				}

			}
		}

		int[][] newArray = new int[array.length - 4][array[0].length - 4];

		for (x = 2; x < array.length - 2; x++)
		{
			for (y = 2; y < array[x].length - 2; y++)
			{
				newArray[x - 2][y - 2] = array[x][y];

				if (array[x][y] == WALL)
				{
					newArray[x - 2][y - 2] = WALL;
					System.out.print("X");
				}
				else
				{
					newArray[x - 2][y - 2] = EMPTY_SQUARE;
					System.out.print(" ");
				}
			}
			System.out.println("");
		}

		for (x = 1; x < newArray.length - 2; x++)
		{
			for (y = 1; y < newArray[x].length - 2; y++)
			{
				if (newArray[x][y] == WALL)
				{
					int testVal = newArray[x][y - 1] + 2 * newArray[x + 1][y] + 4 * newArray[x][y + 1] + 8 * newArray[x - 1][y];
					if (testVal == 5 || testVal == 10)
					{
						if (random.nextInt(10) < 2)
						{
							newArray[x][y] = EMPTY_SQUARE;
							newArray[x][newArray[x].length - y - 1] = EMPTY_SQUARE;
						}
					}
				}
			}
		}

		for (x = 0; x < newArray.length; x++)
		{
			for (y = 0; y < newArray[x].length; y++)
			{
				if (newArray[x][y] != WALL)
				{
					newArray[x][y] = PELLET;
				}
			}
			System.out.println("");
		}

		return newArray;

	}
	private int mirrorDirection(int dir)
	{
		if (dir == NORTH)
			return SOUTH;
		if (dir == SOUTH)
			return NORTH;
		return dir;
	}
	private int newX(int x, int direction)
	{
		if (direction == EAST)
		{
			return x + 1;
		}
		if (direction == WEST)
		{
			return x - 1;
		}
		return x;
	}
	private int newY(int y, int direction)
	{
		if (direction == SOUTH)
		{
			return y + 1;
		}
		if (direction == NORTH)
		{
			return y - 1;
		}
		return y;
	}
	private int reverseDirection(int dir)
	{
		if (dir == NORTH)
			return SOUTH;
		if (dir == SOUTH)
			return NORTH;
		if (dir == EAST)
			return WEST;
		return EAST;
	}
}
