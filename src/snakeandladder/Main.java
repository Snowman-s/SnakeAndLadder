package snakeandladder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.taskcallable.TaskCallable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        RotateCube rotateCube = new RotateCube();

        taskCallableList.add(rotateCube);
        glRendererList.add(rotateCube);

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);
    }

    public static synchronized void task(TaskCallable.TaskCallArgument arg) {
        if (glDisplay.isKeyPressed(VK_ESCAPE)) {
            glDisplay.endWindow();
        }
        for (TaskCallable taskCallable : taskCallableList) {
            taskCallable.task(arg);
        }
    }

    public static synchronized void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        for (GLRenderer renderer : glRendererList) {
            renderer.render(display, autoDrawable);
        }
    }

    static class RotateCube implements TaskCallable, GLRenderer{
        volatile int frameCount = 0;

        @Override
        public synchronized void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glPushMatrix();
            gl.glRotated(frameCount, 0, 1, 0);
            glDisplay.getGLUT().glutWireCube(0.4F);
            gl.glPopMatrix();
        }

        @Override
        public synchronized void task(TaskCallArgument arg) {
            frameCount++;
        }
    }
}
