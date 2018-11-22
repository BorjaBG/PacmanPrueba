import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;
import java.applet.AudioClip;
import java.net.*;
import java.awt.Font;
import java.util.Random;

public final class Main extends Applet implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Thread updateThread;	// thread in which the game will run
	long startTime;			// used to keep track of timing and to prevent applet from running too fast


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

	// try to grab the keyboard focus.
	requestFocus();
}


public void getMainGraphics() // Load and process the most common graphics
{
	MediaTracker tracker;
	int i = 0;

	tracker = new MediaTracker(this);

	// this code doesn't load any graphics yet!

	try
	{
		tracker.waitForAll();
	}
	catch (InterruptedException e)
	{
	}

}


public void run()
{
	// This is the most important method.  It loops over and
	// over again as the game is running.  It makes the calls
	// that move things and then draw them.
	int mouthOpenAngle = 20;
	int dMouthOpenAngle = 5;
	int maxMouthOpenAngle = 100;
	int minMouthOpenAngle = 1;

	while (updateThread != null)
	{
		try
		{
			// this code slows the applet down if it is on a really fast machine
			long sleepTime = Math.max(startTime - System.currentTimeMillis(), 20);
			updateThread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
		}
		startTime = System.currentTimeMillis() + 40;

		// DRAW STUFF HERE:

		// clear what we drew last time.
		gBuf.clearRect(0, 0, MAX_X, MAX_Y);

		//Move PacMan
		x = x + dx;
		y = y + dy;

		// Don't let him go off the sides of the screen
		if (x > MAX_X - PACMAN_SIZE)
		{
			x = MAX_X - PACMAN_SIZE;
			dx = 0;
		}
		if (x < 0)
		{
			x = 0;
			dx = 0;
		}

		// Don't let him go off the top or bottom of the screen
		if (y > MAX_Y - PACMAN_SIZE)
		{
			y = MAX_Y - PACMAN_SIZE;
			dy = 0;
		}
		if (y < 0)
		{
			y = 0;
			dy = 0;
		}

		// set the drawing color
		gBuf.setColor(Color.yellow);

		// draw a PacMan
		gBuf.fillArc(x, y, PACMAN_SIZE, PACMAN_SIZE, 20, 320);

		// repaint() will call paint(Graphics) which will call update(Graphics)
		repaint();
	}
}

// This method is called when the applet is run.
// It initiallizes the thread and gets things going.
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


public boolean keyDown(java.awt.Event e, int key)
{

	// This method handles key presses.
	// For now all the statements are placeholders.

	// it is nice to have a print statement here.  
	// it can be quickly uncommented and the output
	// used to get keycodes since I am too lazy to
	// look them up.
	
	//System.out.println(key);

	if (key == 1006) // left arrow
	{
	   	dx = -5;
	   	dy = 0;
		return false;
	}
	if (key == 1007) // right arrow
	{
 		dx = 5;
 		dy = 0;
	    return false;
	}

	if (key == 1004) // up arrow
	{
	  	dy = -5;
	  	dx = 0;
		return false;
	}
	if (key == 1005) // down arrow
	{
	 	dy = 5;
	 	dx = 0;
		return false;
	}

	if (key == 32) // space bar
	{
		
		return false;
	}

	if (key == 112) // 'P' key
	{
		
		return false;
	}

	return false;
}
	
public boolean keyUp(java.awt.Event e, int key)
{
	// This method is called when a key is released.

	// right now it just has place holders.
	
	
	if (key == 1006 || key == 1007)  // left or right key released
	{
		dx = 0;
		return false;
	}

	if (key == 1004 || key == 1005) // up or down key released.
	{
		dy = 0;
		return false;
	}

	if (key == 32)  // space bar released
	{
		
		return false;
	}
	return false;
}

// To ensure Java 1.1 compatibility, request focus on mouseDown
public boolean mouseDown(java.awt.Event e, int x, int y)
{
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

public void update(Graphics g)
{
	// draw the offscreen buffer to the screen!
	// This buffer was drawn on by the run() method
	// and by any methods run() might have called.
	g.drawImage(imgBuf, 0, 0, this);

}

	Graphics gBuf;	// used for double-buffered graphics
	Image imgBuf;	// also used for double-buffered graphics
	static final int MAX_X = 800;  // widest the playing screen can be
	static final int MAX_Y = 800;  // tallest the playing screen can be

	int dx = 0;
	int dy = 0;
	static final int PACMAN_SIZE = 30;
	int x = 0;
	int y = 0;
}

