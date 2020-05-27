package snakeandladder;

import com.jogamp.opengl.GL2;
import snakeandladder.display.GLDisplay;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.taskcallable.TaskCallable;

import java.util.LinkedList;
import java.util.List;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    public static void main(String[] args) {
        taskCallableList = new LinkedList<>();
        glRendererList = new LinkedList<>();

        glRendererList.add((glDisplay, glAutoDrawable) -> {
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glRotated(0.3,0,1,0);
            glDisplay.getGLUT().glutWireCube(0.1F);
        });

        glDisplay = GLDisplay.getInstance(t -> {
            if (glDisplay.isKeyPressed(VK_ESCAPE)) {
                glDisplay.endWindow();
            }
            for (TaskCallable taskCallable : taskCallableList) {
                taskCallable.task(t);
            }
        }, (disp, autoDrawable) -> {
            for (GLRenderer renderer : glRendererList) {
                renderer.render(disp, autoDrawable);
            }
        });
    }
}
