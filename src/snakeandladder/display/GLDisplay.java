package snakeandladder.display;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.keyboard.KeyReceiver;
import snakeandladder.log.MyLogger;
import snakeandladder.taskcallable.TaskCallable;

import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static java.lang.System.exit;

public class GLDisplay {
    private final GLDisplayInnerClass glDisplayInnerClass;

    private GLDisplay(TaskCallable onDisplay, GLRenderer onRender) {
        glDisplayInnerClass = new GLDisplayInnerClass(onDisplay, onRender);
    }

    public static GLDisplay getInstance(TaskCallable onDisplay, GLRenderer glRenderer) {
        return new GLDisplay(Objects.requireNonNull(onDisplay), Objects.requireNonNull(glRenderer));
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

    public void increaseKeyPressedCount() {
        glDisplayInnerClass.keyReceiver.increaseKeyPressedFrame();
    }

    public void resetKey(short key){
        glDisplayInnerClass.keyReceiver.resetKey(key);
    }

    public void endWindow() {
        glDisplayInnerClass.glWindow.destroy();
        exit(0);
    }

    private class GLDisplayInnerClass implements GLEventListener {
        private int width, height;
        private float widthByHeight;

        private final GLWindow glWindow;
        private final FPSAnimator animator;

        private final GLU glu;
        private final GLUT glut;

        private final KeyReceiver keyReceiver;

        private final TaskCallable onDisplay;
        private final GLRenderer onRender;

        private final TaskCallable.TaskCallArgument arg;

        private GLDisplayInnerClass(TaskCallable onDisplay, GLRenderer onRender) {
            this.onDisplay = onDisplay;
            this.onRender = onRender;
            arg = new TaskCallable.TaskCallArgument();

            GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
            glWindow = GLWindow.create(caps);

            glWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDestroyed(WindowEvent evt) {
                    exit(0);
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
            GL2 gl2 = glAutoDrawable.getGL().getGL2();
            gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            arg.addFrameCount();
            onDisplay.task(arg);
            onRender.render(GLDisplay.this, glAutoDrawable);
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glLoadIdentity();
            this.widthByHeight = (float) (width = i2) / (float) (height = i3);
            glu.gluPerspective(30.0, widthByHeight, 0.01, 100.0);
            glu.gluLookAt(0, 2, -1.5, 0, 0, 0, 0, 0, 1);
        }
    }
}
