import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;
import java.applet.AudioClip;
import java.net.*;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public final class Main extends Applet implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int P_BUTTON = 112;
	private static final int SPACE_BAR = 32;
	private static final int DOWN_ARROW_BUTTON = 1005;
	private static final int UP_ARROW_BUTTON = 1004;
	private static final int RIGHT_ARROW_BUTTON = 1007;
	private static final int LEFT_ARROW_BUTTON = 1006;
	public static boolean visited = false;
	public static final boolean walls[] = new boolean[4];

	Graphics gBuf;	// used for double-buffered graphics
	Image imgBuf;	// also used for double-buffered graphics
	
	static final int PACMAN_SIZE = 36;
	
	Thread updateThread;	// thread in which the game will run
	long startTime;			// used to keep track of timing and to prevent applet from running too fast
	
	int x = 0;		//Posicion x de Pacman
	int dx[] = {0,0}; // amount x position will change
	int y = 0;		//Posicion y de Pacman
	int dy[] = {0,0}; // amount y will change
	
	
	int curCol; //the column pacman is 
	int curRow; //the row pacman is in
	int nextX; // the next position of pacman in pixels= 9 * PACMAN_SIZE;
	int nextY; // = 9 * PACMAN_SIZE;
	
	int mouthStartAngle = 180;		//Direccion a la que Pacman apunta
	
	//Este es el mapa, el 0 es abierto, el 1 es una pared y el 2 son los puntos
		int[][] mazeArray =
				//Hasta la columna 17 y la linea 17
			{ 	{1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,2,2,2,2,2,1,2,1,2,2,2,1},
				{1,2,1,2,1,1,1,2,1,2,1,2,1},
				{1,2,2,2,2,2,2,2,2,2,2,2,1},
				{1,2,1,2,1,1,1,1,1,2,1,2,1},
				{1,1,1,2,2,2,2,2,2,2,1,2,1},
				{1,2,1,2,1,2,1,1,1,2,1,1,1},
				{1,2,1,1,1,2,1,2,2,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,1,1,2,1},
				{1,2,2,2,2,2,2,2,2,2,2,2,1},
				{1,2,1,2,1,2,1,2,1,1,1,2,1},
				{1,2,2,2,2,2,1,2,2,2,2,2,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1}
				/*{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{0,1,1,0,1,1,0,0,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}*/
				
			};
		final static int MAZE_SIZE = 13;
		static final int MAX_X = PACMAN_SIZE * MAZE_SIZE;  // widest the playing screen can be
		static final int MAX_Y = PACMAN_SIZE * MAZE_SIZE;  // tallest the playing screen can be
		final static int SPEED = 6;
		
		private Fantasma fantasma = new Fantasma();
		
public void getMainGraphics() { // Load and process the most common graphics{
		MediaTracker tracker;
		int i = 0;

		tracker = new MediaTracker(this);

		// this code doesn't load any graphics yet!

		try{
			tracker.waitForAll();
		}catch (InterruptedException e)
		{
	}

}
		

public void init()
{
	// Make the applet window the size we want it to be
	resize(MAX_X, MAX_Y);

	// Load the images we will use from the web
	getMainGraphics();

	// Garbage collection call.  Not really needed.
	System.gc();

	// Make a black background
	setBackground(Color.black);

	// Set up double-buffered graphics.
	// This allows us to draw without flickering.
	imgBuf = createImage(MAX_X, MAX_Y);
	gBuf = imgBuf.getGraphics();

	fantasma.setMaze(mazeArray);
	fantasma.setXY(3, 3);
	
	// try to grab the keyboard focus.
	//requestFocus();
}

public boolean keyDown(java.awt.Event e, int key)
{

	// This method handles key presses.
	// For now all the statements are placeholders.

	// it is nice to have a print statement here.  
	// it can be quickly uncommented and the output
	// used to get keycodes since I am too lazy to
	// look them up.
	
	//System.out.println(key);

	if (key == LEFT_ARROW_BUTTON) // left arrow
	{
		if (dx[0] == 0 && dy[0] == 0) {
			dx[0] = -SPEED;
			dx[1] = -SPEED;
			dy[0] = 0;
			dy[1] = 0;
		} else {
			dx[1] = -SPEED;
			dy[1] = 0;
		}
		return false;
	}
	if (key == RIGHT_ARROW_BUTTON) // right arrow
	{
	if (dx[0] == 0 && dy[0] == 0) {
		dx[0] = SPEED;
		dx[1] = SPEED;
		dy[0] = 0;
		dy[1] = 0;
	} else {
		dx[1] = SPEED;
		dy[1] = 0;
	}
	return false;
}

if (key == UP_ARROW_BUTTON) // up arrow
	{
	if (dx[0] == 0 && dy[0] == 0) {
		dx[0] = 0;
		dx[1] = 0;
		dy[0] = -SPEED;
		dy[1] = -SPEED;
	} else {
		dx[1] = 0;
		dy[1] = -SPEED;
	}

	return false;
}
if (key == DOWN_ARROW_BUTTON) // down arrow
	{
	if (dx[0] == 0 && dy[0] == 0) {
		dx[0] = 0;
		dx[1] = 0;
		dy[0] = SPEED;
		dy[1] = SPEED;
	} else {
		dx[1] = 0;
		dy[1] = SPEED;
	}
	return false;
}

if (key == SPACE_BAR) // space bar
	{
	return false;
}

if (key == P_BUTTON) // 'P' key
	{
	return false;
}

return false;
}

public boolean keyUp(java.awt.Event e, int key) {
	// This method is called when a key is released.
	// right now it just has place holders.

	if (key == LEFT_ARROW_BUTTON || key == RIGHT_ARROW_BUTTON) // left or right key released
		{
		return false;
	}

	if (key == UP_ARROW_BUTTON || key == DOWN_ARROW_BUTTON) // up or down key released.
		{
		return false;
	}

	if (key == SPACE_BAR) // space bar released
		{

		return false;
	}
	return false;
}

// To ensure Java 1.1 compatibility, request focus on mouseDown
public boolean mouseDown(java.awt.Event e, int x, int y) {
	requestFocus();
	return false;
}

public void paint(Graphics g) // Draw the control panel and stuff
{
	// Since there are no borders or anything
	// static to draw yet we only need to call
	// the update method.
	update(g);
}


public void run() {
	// This is the most important method.  It loops over and
	// over again as the game is running.  It makes the calls
	// that move things and then draw them.

	int mouthOpenAngle = 20;
	int dMouthOpenAngle = 5;
	int maxMouthOpenAngle = 100;
	int minMouthOpenAngle = 1;

	nextX = 9 * PACMAN_SIZE;
	nextY = 9 * PACMAN_SIZE;

	x = nextX;
	y = nextY;

	boolean hitWall = false;

	int i;

	int pellets = 0;

	//		try to grab the keyboard focus.
	requestFocus();
	
	while (updateThread != null) {
		doComputeSleepTime();
		startTime = System.currentTimeMillis() + 40;

		// DRAW STUFF HERE:

		curCol = (x + PACMAN_SIZE / 2) / PACMAN_SIZE;
		curRow = (y + PACMAN_SIZE / 2) / PACMAN_SIZE;

		if (mazeArray[curRow][curCol] == Maze.PELLET) {
			mazeArray[curRow][curCol] = Maze.EMPTY_SQUARE;
		}

		// do ghost stuff
		//ghost.move(curRow, curCol);
		{
			for (i = 1; i >= 0; i--) {
				nextX = x + dx[i];
				nextY = y + dy[i];
				hitWall = false;

				int col;
				int row;
				hitWall = doDidWeHitAWall(hitWall);
				if (!hitWall) {
					if (i == 1) {
						dx[0] = dx[1];
						dy[0] = dy[1];
					}
					break;
				}

			}
		}
		doDrawMouth();
		//Move PacMan
		doMovePacman(nextX, nextY, hitWall, i);
		// Make the mouth chomp
		{
			mouthOpenAngle = mouthOpenAngle + dMouthOpenAngle;

			if (mouthOpenAngle > maxMouthOpenAngle) {
				mouthOpenAngle = maxMouthOpenAngle;
				dMouthOpenAngle = -10;
			}
			if (mouthOpenAngle < minMouthOpenAngle) {
				mouthOpenAngle = minMouthOpenAngle;
				dMouthOpenAngle = 5;
			}
		}

		// clear what we drew last time.
		gBuf.clearRect(0, 0, MAX_X, MAX_Y);
		pellets = doDrawMaze();
		
		//set the drawing color
		gBuf.setColor(Color.yellow);

		 // draw a PacMan
		 gBuf.fillArc(x, y, PACMAN_SIZE, PACMAN_SIZE, mouthStartAngle + mouthOpenAngle / 2, 360 - mouthOpenAngle);

		// draw ghost
		fantasma.move(curRow, curCol);			
		gBuf.setColor(Color.red);
		gBuf.fillRect(fantasma.getY(), fantasma.getX(), PACMAN_SIZE, PACMAN_SIZE);

		
		// repaint() will call paint(Graphics) which will call update(Graphics)
		repaint();
		

		//doResetMaze(pellets);
	}
}

private boolean doDidWeHitAWall(boolean hitWall) {
	// Process each "cell" of the maze and paint it correctly
	for (int xCorner = 0; xCorner < PACMAN_SIZE; xCorner += PACMAN_SIZE - 1) {
		for (int yCorner = 0; yCorner < PACMAN_SIZE; yCorner += PACMAN_SIZE - 1) {
			int col = (nextX + xCorner) / PACMAN_SIZE;
			int row = (nextY + yCorner) / PACMAN_SIZE;
			if (row < mazeArray.length && col < mazeArray[0].length) {
				if (mazeArray[row][col] == Maze.WALL) {
					hitWall = true;
					break;
				}
			}
		}
	}
	return hitWall;
}

private void doComputeSleepTime() {
	{

		try {
			// this code slows the applet down if it is on a really fast machine
			long sleepTime = Math.max(startTime - System.currentTimeMillis(), 10);
			updateThread.sleep(sleepTime);
		} catch (InterruptedException e) {
		}
	}
}

private void doMovePacman(int nextX, int nextY, boolean hitWall, int i) {
	{
		if (!hitWall) {
			x = nextX;
			y = nextY;
		} else {
			dx[0] = 0;
			dx[1] = 0;
			dy[0] = 0;
			dy[1] = 0;
		}

		// Don't let him go off the sides of the screen
		if (x > MAX_X - PACMAN_SIZE) {
			x = MAX_X - PACMAN_SIZE;
			dx[0] = 0;
			dx[1] = 0;
		}
		if (x < 0) {
			x = 0;
			dx[0] = 0;
			dx[1] = 0;
		}

		// Don't let him go off the top or bottom of the screen
		if (y > MAX_Y - PACMAN_SIZE) {
			y = MAX_Y - PACMAN_SIZE;
			dy[i] = 0;
			dy[1] = 0;
		}
		if (y < 0) {
			y = 0;
			dy[i] = 0;
			dy[1] = 0;
		}
	}
}

private void doDrawMouth() {
	{
		if (dx[0] > 0) {
			mouthStartAngle = 0;
		} else if (dx[0] < 0) {
			mouthStartAngle = 180;
		} else if (dy[0] > 0) {
			mouthStartAngle = 270;
		} else if (dy[0] < 0) {
			mouthStartAngle = 90;
		}
	}
}

private int doDrawMaze() {
	int row;
	int col;
	int pellets;

	// draw maze

	gBuf.setColor(Color.blue);

	pellets = 0;
	
	for (row = 0; row < mazeArray.length; row++) {
		for (col = 0; col < mazeArray[row].length; col++) {
			if (mazeArray[row][col] == Maze.WALL) {
				gBuf.setColor(Color.blue);
				gBuf.fillRect(col * PACMAN_SIZE, row * PACMAN_SIZE, PACMAN_SIZE, PACMAN_SIZE);
			} else if (mazeArray[row][col] == Maze.PELLET) {
				pellets++;
				gBuf.setColor(Color.white);
				gBuf.fillArc(col * PACMAN_SIZE+PACMAN_SIZE/2 - 5, row * PACMAN_SIZE+PACMAN_SIZE/2 -5, 10,10,0,360);
				//gBuf.fillRect(col * PACMAN_SIZE + PACMAN_SIZE / 4, row * PACMAN_SIZE + PACMAN_SIZE / 4, PACMAN_SIZE / 2, PACMAN_SIZE / 2);
				//gBuf.fillOval(col * PACMAN_SIZE + PACMAN_SIZE / 4, row * PACMAN_SIZE + PACMAN_SIZE / 4, PACMAN_SIZE / 2, PACMAN_SIZE / 2);
			}
		}
	}

	return pellets;

}

/*private void doResetMaze(int pellets) {
	int nextX;
	int nextY;
	{

		if (pellets == 0) {
			maze = new Maze();
			mazeArray = maze.generateMazeArray();
			nextX = 9 * PACMAN_SIZE;
			nextY = 9 * PACMAN_SIZE;

			fantasma.setMaze(mazeArray);
			fantasma.setXY(3, 3);

			x = nextX;
			y = nextY;
		}
	}
}*/
public void start()
{
	if (updateThread == null)
	{
		updateThread = new Thread(this, "Game");
		updateThread.start();
		startTime = System.currentTimeMillis();
	}
}

// This method is called when the applet is stopped. 
public void stop()
{
	updateThread = null;
}


public void update(Graphics g)
{
	// draw the offscreen buffer to the screen!
	// This buffer was drawn on by the run() method
	// and by any methods run() might have called.
	g.drawImage(imgBuf, 0, 0, this);

}
/**
 * Cell constructor comment.
 */
public Main() {
	super();
	for (int i = 0; i < walls.length;i++)
	{
		walls[i] = true;
	}
	
}

}

