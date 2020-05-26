package jogl3DTest;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import jogl3DTest.keyboard.KeyReceiver;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static java.lang.Math.*;

public class Main implements GLEventListener {
    private GLWindow glWindow;
    private FPSAnimator animator;
    private int width, height;
    private float widthByHeight;

    private KeyReceiver keyReceiver;

    private final GLU glu;
    private final GLUT glut;

    private int frameCount = 0;
    private static final float[] ambient = new float[]{0.2f, 0.2f, 0.2F, 1f};
    private static final float[] light = new float[]{0f, 0f, 0F, 1f};
    private static final float[] color = new float[]{1f, 1f, 1f, 1F};

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
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
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glLoadIdentity();
        this.widthByHeight = (float) (width = i2) / (float) (height = i3);
        glu.gluPerspective(30.0, widthByHeight, 0.01, 100.0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        frameCount++;
        if (this.keyReceiver.isKeyPressed(VK_ESCAPE)) {
            glWindow.destroy();
            return;
        }
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl2.glPushMatrix();

        glu.gluLookAt(
                cos(frameCount / 60F) * 0.5, sin(frameCount / 60F) * 0.5, -1F,
                0F, 0F, 0F,
                0, 1, 0
        );

        gl2.glLightfv(GL_LIGHT0, GL_POSITION, light, 0);
        gl2.glLightfv(GL_LIGHT0, GL_AMBIENT, ambient, 0);

        keyReceiver.increaseKeyPressedFrame();

        int rs = frameCount % 100;
        color[0] = (float) (1 - cos(toRadians(frameCount))) / 2F;
        color[1] = (float) (1 - cos(toRadians(frameCount + 120))) / 2F;
        color[2] = (float) (1 - cos(toRadians(frameCount + 240))) / 2F;
        gl2.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, color, 0);
        for (int r = rs; r < 250 + rs; r += 100) {
            for (int i = 0; i < 360; i += 360 / 24) {
                for (int i2 = 0; i2 < 180; i2 += 360 / 24) {
                    gl2.glPushMatrix();
                    gl2.glRotated(i, 1, 0, 0);
                    gl2.glRotated(i2, 0, 1, 0);
                    gl2.glTranslated(r / 500F, 0, 0);
                    glut.glutSolidCube(0.03F);
                    gl2.glPopMatrix();
                }
            }
        }

        gl2.glPopMatrix();
    }
}
