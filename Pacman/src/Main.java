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

	Graphics gBuf;	// Se utiliza para un doble buffer de los graficos
	Image imgBuf;	// Tambien se utiliza para un doble buffer de los graficos
	
	static final int PACMAN_SIZE = 36;
	
	Thread updateThread;	// Hilo en el que funcionara el juego
	long startTime;			// Cuenta el tiempo y evita que el applet funcione muy rapido
	
	int x = 0;		//Posicion x de Pacman
	int dx[] = {0,0}; // cantidad que la x va ha cambiar
	int y = 0;		//Posicion y de Pacman
	int dy[] = {0,0}; // cantidad que la y va ha cambiar
	
	
	int curCol; //La columna en la que esta Pacman
	int curRow; //La fila en la que esta Pacman
	int nextX; // La proxima posicion de Pacman en pixeles= 9 * PACMAN_SIZE;
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
		static final int MAX_X = PACMAN_SIZE * MAZE_SIZE;  // Maxima anchura de la ventana
		static final int MAX_Y = PACMAN_SIZE * MAZE_SIZE;  // Maxima altura de la ventana
		final static int SPEED = 6;
		
		private Fantasma fantasma = new Fantasma();
		//private comprobarpos posicion = new comprobarpos();
		
public void getMainGraphics() { // Carga y procesa los graficos mas basicos (no funciona){
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
	// Pone del tamaño que queramos la ventana del applet
	resize(MAX_X, MAX_Y);

	// Load the images we will use from the web
	getMainGraphics();

	// Garbage collector
	System.gc();

	// Fondo
	setBackground(Color.black);

	// Se utiliza para que se recargen los graficos sin que parpadee la pantalla
	imgBuf = createImage(MAX_X, MAX_Y);
	gBuf = imgBuf.getGraphics();

	fantasma.setMaze(mazeArray);
	fantasma.setXY(3, 3);
	
	/*// try to grab the keyboard focus.
	requestFocus();*/
}

public boolean keyDown(java.awt.Event e, int key)
{

	// Este metodo se utiliza cundo pulsas una de las teclas

	if (key == LEFT_ARROW_BUTTON) // Flecha a la izquierda
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
	if (key == RIGHT_ARROW_BUTTON) // Flecha a la derecha
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

if (key == UP_ARROW_BUTTON) // Flecha hacia arriba
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
if (key == DOWN_ARROW_BUTTON) // Flecha hacia abajo
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

if (key == SPACE_BAR) // Barra espaciadora
	{
	return false;
}

return false;
}

public boolean keyUp(java.awt.Event e, int key) {
	// Este metodo se utiliza al soltar alguna de las teclas utilizadas, pero ahora mismo no esta en uso

	if (key == LEFT_ARROW_BUTTON || key == RIGHT_ARROW_BUTTON) // Soltar flecha izquierda derecha
		{
		return false;
	}

	if (key == UP_ARROW_BUTTON || key == DOWN_ARROW_BUTTON) // Soltar flecha arriba o abajo
		{
		return false;
	}

	if (key == SPACE_BAR) // Barra espaciadora
		{

		return false;
	}
	return false;
}

// Pide el focus cuando se clicka en la ventana
public boolean mouseDown(java.awt.Event e, int x, int y) {
	requestFocus();
	return false;
}

public void paint(Graphics g) // Dibujaria varias cosas estaticas en pantalla pero al no haberlas solo llama al update
{
	update(g);
}


public void run() {
	//Es el metodo mas importante ya que esta todo el tiempo activo y hace las llamadas necesarias

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

	//	Intenta coger el focus del teclado
	requestFocus();
	
	while (updateThread != null) {
		doComputeSleepTime();
		startTime = System.currentTimeMillis() + 40;

		// Dibuja

		curCol = (x + PACMAN_SIZE / 2) / PACMAN_SIZE;
		curRow = (y + PACMAN_SIZE / 2) / PACMAN_SIZE;

		if (mazeArray[curRow][curCol] == Maze.PELLET) {
			mazeArray[curRow][curCol] = Maze.EMPTY_SQUARE;
		}

		 /*do ghost stuff
		ghost.move(curRow, curCol);*/
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
			
		doDrawMouth();
		//Mueve a Pacman
		doMovePacman(nextX, nextY, hitWall, i);
		// Mueve la boca de Pacman
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
		// Limpia lo que dibujamos la anterior vez
		gBuf.clearRect(0, 0, MAX_X, MAX_Y);
		pellets = doDrawMaze();
		
		//set the drawing color
		gBuf.setColor(Color.yellow);

		 // Dibuja a PacMan
		 gBuf.fillArc(x, y, PACMAN_SIZE, PACMAN_SIZE, mouthStartAngle + mouthOpenAngle / 2, 360 - mouthOpenAngle);

		// Dibuja un fantasma
		fantasma.move(curRow, curCol);			
		gBuf.setColor(Color.red);
		gBuf.fillRect(fantasma.getY(), fantasma.getX(), PACMAN_SIZE, PACMAN_SIZE);
		
		// repaint() llama a paint(Graphics) y este llama a actualizar los graficos
		repaint();
			
		//doResetMaze(pellets);
		}
		if (getX() == fantasma.getX()) {
			if (getY() == fantasma.getY()) {
				try {
					System.out.println("Has muerto");
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
		}
}

private boolean doDidWeHitAWall(boolean hitWall) {
	// Mira cada celda y la pinta correctamente
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
			// Controla la velocidad del applet
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

		// Para que no se vaya por la izquierda o derecha
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

		// Para que no se vaya por arriba o por abajo
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

// Se llama a este metodo cuando el applet para 
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
//Celdas
public Main() {
	super();
	for (int i = 0; i < walls.length;i++)
	{
		walls[i] = true;
	}
	
}

}

