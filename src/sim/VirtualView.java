/*
 * Spidy Simulator Copyright (C) 2012 Nicolàs Alejandro Di Risio
 * <nicolas@dirisio.net>
 * 
 * This file is part of Spidy Simulator.
 * 
 * Spidy Simulator is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Spidy Simulator is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Spidy Simulator. If not, see <http://www.gnu.org/licenses/>.
 */
package sim;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import objLoader.SimpleObjFile;
import com.sun.opengl.util.Animator;

/* @author Nicolàs Alejandro Di Risio */
class VirtualView extends JFrame implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener {
	public float[] xSides = { 0.05f, -0.05f }; /* Origin of each leg */
	public float yHeight = 0.1f;
	public float[] zSides = { -0.3f, 0.0f, 0.3f };
	public float yOffset = -0.8f; /* Offset of leg model */
	public float legLengths[] = { 1.6f, 1.0f }; /* length of legs */
	public int winHeight = 400, winWidth = 600; /* Window Dimensions */
	public float mouseRot_SpeedZooming = 1.0f; /* Mouse zoom speed */
	public float mouseRot_SpeedRotationX = 1.0f;
	public float mouseRot_SpeedRotationY = 1.0f;
	/* Internal Variable Declaration */
	static private int[] engineDegrees = new int[18];
	private boolean mousePressed, autoRotation = false;
	private float mouseRot_firstX, mouseRot_firstY, mouseRot_TurnViewX,
			mouseRot_TurnViewY, deltaZoom = 0, turnViewX = 0, turnViewY = 0;

	public void start() {
		VirtualView frame = new VirtualView();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public int newCouple(int engineCode, int degree) {
		int engine = eng2num(engineCode);
		if (engine < 0 || degree > 90 || degree < -90)
			return -1;
		engineDegrees[engine] = -1 * degree;
		return 1;
	}

	private int eng2num(int engineCode) {
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

	public VirtualView() {
		setSize(winWidth, winHeight);
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
	private class GraphicListener implements GLEventListener {
		SimpleObjFile legFiles[] = new SimpleObjFile[6];
		LegSkinning skin[] = new LegSkinning[6];
		private float rotation = 0;
		private int sizeWH = winWidth;
		private float w, h;
		SimpleObjFile bodyFile;

		/*
		 * Called after display has been created. So it is first setup of
		 * display.
		 */
		public void init(GLAutoDrawable arg0) {
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
			/* Body Initialization */
			bodyFile = SimpleObjFile.getFromFile("res/body.obj");
			/* Leg Initialization */
			int side;
			for (int i = 0; i < 6; i++) {
				legFiles[i] = SimpleObjFile.getFromFile("res/leg.obj");
				if ((i % 2) == 0)
					side = 1;
				else
					side = -1;
				skin[i] = new LegSkinning(legFiles[i], side, yOffset,
						legLengths);
			}
		}

		/*
		 * Called every time the panel must be refresh
		 */
		public void display(GLAutoDrawable arg0) {
			GL gl = arg0.getGL();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
					| GL.GL_STENCIL_BUFFER_BIT);
			gl.glEnable(GL.GL_DEPTH_TEST);
			// gl.glEnable(GL.GL_POINT_SMOOTH); /* No antialiasing */
			gl.glDisable(GL.GL_CULL_FACE); /* No face will be cut */
			gl.glEnable(GL.GL_NORMALIZE);
			GLU glu = new GLU();
			GLUquadric quadric = glu.gluNewQuadric();
			glu.gluQuadricNormals(quadric, GL.GL_TRUE);
			/* Setting projection of view and zoom */
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			float scale = (sizeWH + deltaZoom * 5);
			if (scale <= 100.0f) {
				scale = 100.0f;
				deltaZoom = (scale - sizeWH) / 5;
			}
			gl.glScalef(scale / w, scale / h, 1);
			/* Rotation of view */
			gl.glRotatef(turnViewX, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(turnViewY, 1.0f, 0.0f, 0.0f);
			/* Model view automatic rotation */
			if (autoRotation) {
				rotation++;
			}
			/* Texture */
			gl.glMatrixMode(GL.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(0.8f, 0.1f, 0.8f);
			/* Body Draw */
			gl.glMatrixMode(GL.GL_MODELVIEW); /* Load M */
			gl.glLoadIdentity(); /* M = I */
			gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f); /* rotate model */
			gl.glTranslatef(0, 0.14f, 0.2f);
			gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			bodyFile.draw(gl);
			/* Leg Draw */
			gl.glColor3f(0.64f, 0.37f, 0.22f);
			for (int side = 0; side < 2; side++) {
				for (int leg = 0; leg < 3; leg++) {
					int legNumber = side + (leg * 2);
					skin[legNumber].setAngles(new int[] {
							engineDegrees[legNumber * 3],
							engineDegrees[legNumber * 3 + 1],
							engineDegrees[legNumber * 3 + 2] });
					skin[legNumber].updateFile(7);
					gl.glLoadIdentity(); /* M = I */
					gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f); /* rotate model */
					gl.glTranslatef(xSides[side], yHeight, zSides[leg]);
					gl.glRotatef(-90.0f, 0.0f, 1.0f - (2.0f * side), 0.0f);
					gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					legFiles[legNumber].draw(gl);
				}
			}
		}

		/*
		 * Called at every display change.
		 */
		public void displayChanged(GLAutoDrawable arg0, boolean arg1,
				boolean arg2) {
		}

		/*
		 * Called every time is detected a reshape in display.
		 */
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
				int arg4) {
			w = arg3;
			h = arg4;
		}
	}

	/*
	 * JFrame key control methods
	 */
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_UP) {
			turnViewY += 3.5;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			turnViewY -= 3.5;
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

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	/*
	 * Serial Number. It is used to get incompatible, uses of this class if it
	 * changes too much in future.
	 */
	private static final long serialVersionUID = 1;

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		deltaZoom += (mouseRot_SpeedZooming * (arg0.getWheelRotation() * arg0
				.getScrollAmount()));
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (mousePressed) {
			turnViewX = mouseRot_TurnViewX + mouseRot_SpeedRotationX
					* (arg0.getX() - mouseRot_firstX);
			turnViewY = mouseRot_TurnViewY + mouseRot_SpeedRotationY
					* (arg0.getY() - mouseRot_firstY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		/* Nothing to do */
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		/* Nothing to do */
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		/* Nothing to do */
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		/* Nothing to do */
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mousePressed = true;
		mouseRot_firstX = arg0.getX();
		mouseRot_firstY = arg0.getY();
		mouseRot_TurnViewX = turnViewX;
		mouseRot_TurnViewY = turnViewY;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
	}
}
