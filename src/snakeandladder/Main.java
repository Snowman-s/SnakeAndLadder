package snakeandladder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.field.Field;
import snakeandladder.field.SquareFieldRenderer;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.taskcallable.TaskCallable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        long seed = System.currentTimeMillis();

        Field field = new Field.Builder(seed)
                .gridNum(45)
                .randomiseSnakeAndLadder()
                .build();

        SquareFieldRenderer fieldRenderer = new SquareFieldRenderer(field);

        glRendererList.add(fieldRenderer);

        System.out.println("seed = " + seed);
        System.out.println("field.getLadderAndSnake() = " + field.getLadderAndSnake());
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
        GL2 gl = autoDrawable.getGL().getGL2();
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0, 0, 0, 1F}, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.5F, 0.5F, 0.5F, 0.5F}, 0);
        for (GLRenderer renderer : glRendererList) {
            renderer.render(display, autoDrawable);
        }
    }
}
