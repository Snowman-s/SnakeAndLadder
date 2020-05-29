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
        //taskCallableList = new CopyOnWriteArrayList<>();
        //glRendererList = new CopyOnWriteArrayList<>();

        //glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        Field field = new Field.Builder(0)
                .gridNum(50)
                .randomiseSnakeAndLadder()
                .build();

        System.out.println("field = " + field);
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
}
