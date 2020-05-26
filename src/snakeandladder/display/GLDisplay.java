package snakeandladder.display;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import snakeandladder.keyboard.KeyReceiver;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

public class GLDisplay {
    private final GLDisplayInnerClass glDisplayInnerClass;

    private GLDisplay() {
        glDisplayInnerClass = new GLDisplayInnerClass();
    }

    public static GLDisplay getInstance() {
        return new GLDisplay();
    }

    public int getWidth() {
        return glDisplayInnerClass.width;
    }

    public int getHeight() {
        return glDisplayInnerClass.height;
    }

    public float getWidthByHeight() {
        return glDisplayInnerClass.widthByHeight;
    }

    public GLU getGLU() {
        return glDisplayInnerClass.glu;
    }

    public GLUT getGLUT() {
        return glDisplayInnerClass.glut;
    }

    public boolean isKeyPressed(short key) {
        return glDisplayInnerClass.keyReceiver.isKeyPressed(key);
    }

    private static class GLDisplayInnerClass implements GLEventListener {
        private int width, height;
        private float widthByHeight;

        private final GLWindow glWindow;
        private final FPSAnimator animator;

        private final GLU glu;
        private final GLUT glut;

        private final KeyReceiver keyReceiver;

        private GLDisplayInnerClass() {
            GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
            glWindow = GLWindow.create(caps);

            glWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyed(WindowEvent evt) {
                    System.exit(0);
                }
            });

            glu = new GLU();
            glut = new GLUT();
            glWindow.addGLEventListener(this);

            //glWindow.setFullscreen(true);
            glWindow.setSize(500, 500);

            animator = new FPSAnimator(60, true);
            animator.add(glWindow);
            animator.start();
            glWindow.setVisible(true);

            glWindow.addKeyListener(keyReceiver = new KeyReceiver());
        }

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            glAutoDrawable.setGL(new DebugGL2(gl));
            gl.glClearColor(0F, 0F, 0F, 1);
            gl.glEnable(GL_BLEND);
            gl.glEnable(GL_LINE_SMOOTH);
            gl.glEnable(GL_DEPTH_TEST);
            gl.glEnable(GL_LIGHTING);
            gl.glEnable(GL_LIGHT0);
            gl.glCullFace(GL_BACK);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            animator.setUpdateFPSFrames(60, null);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {
            if (animator != null) animator.stop();
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glLoadIdentity();
            this.widthByHeight = (float) (width = i2) / (float) (height = i3);
            glu.gluPerspective(30.0, widthByHeight, 0.01, 100.0);
        }
    }
}
