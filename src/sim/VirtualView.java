/*
 *	  Spidy Simulator
 *	Copyright (C) 2012  Nicolàs Alejandro Di Risio <nicolas@dirisio.net>
 *
 *	This file is part of Spidy Simulator.
 *
 *	Spidy Simulator is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Spidy Simulator is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Spidy Simulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package sim;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import com.sun.opengl.util.Animator;

/* FIXME: change mouse rotation and zoom to be centered in axes origin */
/* @author Nicolàs Alejandro Di Risio */
class VirtualView extends JFrame implements KeyListener, MouseListener,
								MouseMotionListener, MouseWheelListener
{
	private static float deltaZView = 0, turnViewX = 0, turnViewY = 0;
	private static boolean autoRotation = false;
	private static float rotation = 0;
	private int height = 400, width = 600;
	private int sizeWH = width;
	private float w, h;
	private boolean mousePressed;
	private float mouseRot_firstX, mouseRot_firstY;
	private float mouseRot_TurnViewX, mouseRot_TurnViewY;
	private float mouseRot_SpeedRotationX = 0.2f,
			mouseRot_SpeedRotationY = 0.2f, mouseRot_SpeedZooming = 0.05f;
	public boolean start;
	private static int[] engDegLast = new int[18];
	private static int[] engDegNew = new int[18];
	private static float[] legLenghts = { 0.1f, 0.2f, 0.2f };

	public void start()
	{
		start = true;
		VirtualView frame = new VirtualView();
		frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt)
			{
				System.out.println("Closing simulator...");
				start = false;
			}
		});
	}

	public int newCouple(int engineCode, int degree)
	{
		int engine = eng2num(engineCode);
		if (engine < 0 || degree > 90 || degree < -90)
			return -1;
		engDegNew[engine] = -1 * degree;
		return 1;
	}

	private int eng2num(int engineCode)
	{
		int engine = 0;
		if (engineCode < 0 && engineCode > 52)
			return -1;
		if ((engine = (engineCode % 10)) > 2)
			return -1;
		engine += ((engineCode / 10) * 3);
		if (engine < 0 && engine > 52)
			return -1;
		return engine;
	}

	VirtualView()
	{
		setSize(width, height);
		setTitle("Your Friendly Neighbourhood Spidy");
		GraphicListener listener = new GraphicListener();
		GLCanvas canvas = new GLCanvas(new GLCapabilities());
		canvas.addGLEventListener(listener);
		getContentPane().add(canvas);
		Animator animator = new Animator(canvas);
		animator.start();
		/* Listeners Definition */
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
	}

	/*
	 * GraphicListener implementation
	 */
	private class GraphicListener implements GLEventListener
	{

		/*
		 * Called every time the panel must be refresh
		 */
		public void display(GLAutoDrawable arg0)
		{
			GL gl = arg0.getGL();
			float[] xSides = { 0.1f, -0.1f };
			float yHeight = 0.2f;
			float[] zSides = { 0.0f, 0.3f, 0.6f };
			int firstEngine = 0;
			/* Clear screen */
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			gl.glEnable(GL.GL_DEPTH_TEST);
			// gl.glEnable(GL.GL_POINT_SMOOTH); No antialiasing
			gl.glDisable(GL.GL_CULL_FACE); /* No face will be cut */
			/* Setting projection of view */
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glScalef(sizeWH / w, sizeWH / h, 1);
			gl.glRotatef(turnViewX, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(turnViewY, 1.0f, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, 0.0f, deltaZView);
			GLU glu = new GLU();
			GLUquadric quadric = glu.gluNewQuadric();
			glu.gluQuadricNormals(quadric, GL.GL_TRUE);
			/* Model view automatic rotation */
			if (autoRotation)
				rotation++;
			gl.glMatrixMode(GL.GL_MODELVIEW); /* Load M */
			gl.glLoadIdentity(); /* M= I */
			gl.glTranslatef(0.0f, 0.0f, 0.0f); /* translate axes */
			gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f); /* rotate them */
			gl.glColor3f(1.0f, 0.2f, 0.7f);
			float lenght = 0.6f;
			gl.glTranslatef(0.0f, 0.0f, -lenght / 2); /* at middle */
			// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
			// skin.drawLeg(gl, new Vertex3f(xSides[side], yHeight,
			// zSides[leg]), side, new double[] {
			// (engineDegrees[firstEngine] * Math.PI / 180),
			// (engineDegrees[firstEngine + 1] * Math.PI / 180),
			// (engineDegrees[firstEngine + 2] * Math.PI / 180) });
			// }
			// }
			/* drawing relatively each leg */
			float xPrec = 0, yPrec = 0, zPrec = 0;
			float xInd, yInd, zInd;
			float xInd2, yInd2, zInd2;
			double[] anglesRad = new double[3];
			for (int side = 0; side < 2; side++) {
				for (int leg = 0; leg < 3; leg++) {
					/* Initialization */
					firstEngine = (leg * 6) + (side * 3);
					anglesRad[0] = engDegNew[firstEngine] * Math.PI / 180;
					anglesRad[1] =
									engDegNew[firstEngine + 1] * Math.PI
											/ 180;
					anglesRad[2] =
									engDegNew[firstEngine + 2] * Math.PI
											/ 180;
					engDegLast[firstEngine] = engDegNew[firstEngine];
					engDegLast[firstEngine+1] = engDegNew[firstEngine+1];
					engDegLast[firstEngine+2] = engDegNew[firstEngine+2];
					gl.glBegin(GL.GL_LINE_STRIP);
					/* Engine 1 - Origin */
					xPrec = xSides[side];
					yPrec = yHeight;
					zPrec = zSides[leg];
					gl.glVertex3d(xPrec, yPrec, zPrec);
					/*
					 * Engine 2 - independent movement of anglesRad[0]
					 */
					xPrec +=
								Math.pow(-1, side) * legLenghts[0]
										* Math.cos(anglesRad[0]);
					yPrec += 0;
					zPrec += legLenghts[0] * Math.sin(anglesRad[0]);
					gl.glVertex3d(xPrec, yPrec, zPrec);
					/*
					 * Engine 3 - independent movement of anglesRad[1]
					 */
					xInd =
							(float) (Math.pow(-1, side) * legLenghts[1] * Math.cos(anglesRad[1]));
					yInd = (float) (legLenghts[1] * Math.sin(anglesRad[1]));
					zInd = 0;
					/*
					 * axes rotation on y of anglesRad[0] and translation from
					 * engine 2
					 */
					xPrec +=
								xInd * (float) (Math.cos(anglesRad[0]))
										- (float) (Math.pow(-1, side)) * zInd
										* (float) (Math.sin(anglesRad[0]));
					yPrec += yInd;
					zPrec +=
								zInd * (float) (Math.cos(anglesRad[0]))
										+ (float) (Math.pow(-1, side)) * xInd
										* (float) (Math.sin(anglesRad[0]));
					gl.glVertex3d(xPrec, yPrec, zPrec);
					/*
					 * Ground - independent movement of anglesRad[2]
					 */
					xInd =
							(float) (Math.pow(-1, side) * legLenghts[2] * Math.cos(anglesRad[2]));
					yInd = (float) (legLenghts[2] * Math.sin(anglesRad[2]));
					zInd = 0;
					/* axes rotation on z of anglesRad[1] */
					xInd2 =
							xInd * (float) (Math.cos(anglesRad[1]))
									- (float) (Math.pow(-1, side)) * yInd
									* (float) (Math.sin(anglesRad[1]));
					yInd2 =
							yInd * (float) (Math.cos(anglesRad[1]))
									+ (float) (Math.pow(-1, side)) * xInd
									* (float) (Math.sin(anglesRad[1]));
					zInd2 = zInd;
					/*
					 * axes rotation on y of anglesRad[0] and translation at
					 * engine 3
					 */
					xPrec +=
								xInd2 * (float) (Math.cos(anglesRad[0]))
										- (float) (Math.pow(-1, side)) * zInd2
										* (float) (Math.sin(anglesRad[0]));
					yPrec += yInd2;
					zPrec +=
								zInd2 * (float) (Math.cos(anglesRad[0]))
										+ (float) (Math.pow(-1, side)) * xInd2
										* (float) (Math.sin(anglesRad[0]));
					/* If it is really farer than engine 3 */
					gl.glVertex3d(xPrec, yPrec, zPrec);
					gl.glEnd();
				}
			}
		}

		/*
		 * Called at every display change.
		 */
		public void displayChanged(GLAutoDrawable arg0, boolean arg1,
									boolean arg2)
		{
		}

		/*
		 * Called after display has been created. So it is first setup of
		 * display.
		 */
		public void init(GLAutoDrawable arg0)
		{
			/*
			 * This is to enable color blending operations based on the alpha
			 * channel. This is necessary to have antialiasing being effective.
			 * 
			 * GL gl = arg0.getGL(); gl.glEnable(GL.GL_BLEND);
			 * gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			 */
			/* Creating light */
			GL gl = arg0.getGL();
			gl.glEnable(GL.GL_LIGHTING);
			float ambient[] = { 0.2f, 0.2f, 0.2f, 0 };
			gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, ambient, 0);
			gl.glEnable(GL.GL_LIGHT0);
			float position[] = { -1.0f, 0.5f, 0.7f, 1 };
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
			float intensity[] = { 1, 1, 1, 1 };
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, intensity, 0);
			gl.glEnable(GL.GL_LIGHT1);
			float position2[] = { 0, 0.5f, -1.0f, 1 };
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, position2, 0);
			float intensity2[] = { 1, 1, 1, 0 };
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, intensity2, 0);
			float specIntensity2[] = { 1, 1, 1, 1 };
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specIntensity2, 0);
			gl.glEnable(GL.GL_COLOR_MATERIAL);
			gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
			float specColor[] = { 1, 1, 1, 1 };
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, specColor, 0);
			gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 60);

		}

		/*
		 * Called every time is detected a reshape in display.
		 */
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
							int arg4)
		{
			w = arg3;
			h = arg4;
		}
	}

	/*
	 * JFrame key control methods
	 */
	public void keyPressed(KeyEvent arg0)
	{
		if (arg0.getKeyCode() == KeyEvent.VK_UP) {
			deltaZView += 0.05;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			deltaZView -= 0.05;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			turnViewX += 3.5;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			turnViewX -= 3.5;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_R) {
			/* Turn on and off auto rotation of model */
			autoRotation = !autoRotation;
		}
	}

	public void keyReleased(KeyEvent arg0)
	{
	}

	public void keyTyped(KeyEvent arg0)
	{
	}

	/*
	 * Serial Number. It is used to get incompatible, uses of this class if it
	 * changes too much in future.
	 */
	private static final long serialVersionUID = 1;

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0)
	{
		deltaZView +=
						(mouseRot_SpeedZooming * (arg0.getWheelRotation() * arg0.getScrollAmount()));
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		if (mousePressed) {
			turnViewX =
						mouseRot_TurnViewX + mouseRot_SpeedRotationX
								* (arg0.getX() - mouseRot_firstX);
			turnViewY =
						mouseRot_TurnViewY + mouseRot_SpeedRotationY
								* (arg0.getY() - mouseRot_firstY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		/* Nothing to do */
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		/* Nothing to do */
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		/* Nothing to do */
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		/* Nothing to do */
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		mousePressed = true;
		mouseRot_firstX = arg0.getX();
		mouseRot_firstY = arg0.getY();
		mouseRot_TurnViewX = turnViewX;
		mouseRot_TurnViewY = turnViewY;
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		mousePressed = false;
	}
}
