package snakeandladder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.field.Field;
import snakeandladder.field.SquareFieldRenderer;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.player.Player;
import snakeandladder.taskcallable.TaskCallable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;
    private static List<Player> playerList;

    private static float[] positionLight = new float[]{0, 0, 0, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        long seed = System.currentTimeMillis();

        int gridNum = 49;

        Field field = new Field.Builder(seed)
                .gridNum(gridNum)
                .randomiseSnakeAndLadder()
                .build();

        playerList = Player.createPlayerList(4, gridNum);

        SquareFieldRenderer fieldRenderer = new SquareFieldRenderer(field);
        FieldPlayerRenderer fieldPlayerRenderer = new FieldPlayerRenderer(fieldRenderer, playerList);
        glRendererList.add(fieldPlayerRenderer);

        System.out.println("seed = " + seed);
        System.out.println("field.getLadderAndSnake() = " + field.getLadderAndSnake());
    }

    public static synchronized void task(TaskCallable.TaskCallArgument arg) {
        if (glDisplay.isKeyPressed(VK_ESCAPE)) {
            glDisplay.endWindow();
        }
        if (arg.getFrameCount() % 50 == 0) {
            playerList.get(arg.getFrameCount() / 50 % 4).addGridNumber(ThreadLocalRandom.current().nextInt(6) + 1);
        }
        taskCallableList.forEach(t -> t.task(arg));
    }

    public static synchronized void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        GL2 gl = autoDrawable.getGL().getGL2();
        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
        glRendererList.forEach(r -> r.render(display, autoDrawable));
    }
}
