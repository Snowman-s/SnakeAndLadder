package snakeandladder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.field.Field;
import snakeandladder.field.SquareFieldRenderer;
import snakeandladder.glrenderer.GLRenderer;
import snakeandladder.log.MyLogger;
import snakeandladder.player.Player;
import snakeandladder.properties.MappedProperties;
import snakeandladder.properties.PropertiesReader;
import snakeandladder.roulette.CardRoulette;
import snakeandladder.taskcallable.TaskCallable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jogamp.newt.event.KeyEvent.VK_ENTER;
import static com.jogamp.newt.event.KeyEvent.VK_ESCAPE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;
    private static List<Player> playerList;
    private static Field field;

    private static Player moveStandByPlayer;

    private static FieldPlayerRenderer fieldPlayerRenderer;
    private static CardRoulette roulette;
    private static int diceNum;

    private static Phase phase = Phase.ChangeTurn;

    private static MappedProperties properties;

    private static Random diceRandom;

    private static final float[] positionLight = new float[]{0, 0, 0, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        properties = PropertiesReader.load();

        long fieldSeed = properties.getLong(MappedProperties.LongKey.fieldSeed);
        long diceSeed = properties.getLong(MappedProperties.LongKey.diceSeed);

        diceRandom = new Random(diceSeed);

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        int gridNum = properties.getInt(MappedProperties.IntKey.gridNumber);

        field = new Field.Builder(fieldSeed)
                .gridNum(gridNum)
                .randomiseSnakeAndLadder()
                .build();

        playerList = Player.createPlayerList(properties.getInt(MappedProperties.IntKey.playerNumber), gridNum - 1);

        SquareFieldRenderer fieldRenderer = new SquareFieldRenderer(field);
        fieldPlayerRenderer = new FieldPlayerRenderer(fieldRenderer, playerList);
        glRendererList.add(fieldPlayerRenderer);

        System.out.println("field_seed = " + fieldSeed);
        System.out.println("dice_seed = " + diceSeed);

        setPlayer(0);
    }

    public static void setPhase(Phase phase) {
        Main.phase.end();
        Main.phase = phase;
    }

    private static void setNextPlayer() {
        setPlayer(playerList.indexOf(moveStandByPlayer) + 1);
    }

    private static void setPlayer(int index) {
        if (index >= playerList.size()) {
            index = 0;
        }
        moveStandByPlayer = playerList.get(index);

        fieldPlayerRenderer.emphasizePlayer(moveStandByPlayer);

        setPhase(Phase.ChangeTurn);
    }

    public static void task(TaskCallable.TaskCallArgument arg) {
        phase.task(arg);
        taskCallableList.forEach(t -> t.task(arg));
    }

    public static void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        GL2 gl = autoDrawable.getGL().getGL2();
        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
        glRendererList.forEach(r -> r.render(display, autoDrawable));
    }

    private static class PlayerMove implements TaskCallable {
        int taskCalledCount = -1;
        int moveCount;
        final Player movePlayer;

        public PlayerMove(int moveCount, Player movePlayer) {
            this.moveCount = moveCount;
            this.movePlayer = movePlayer;
        }

        @Override
        public void task(TaskCallArgument arg) {
            taskCalledCount++;
            roulette.setNowNumber(Math.max(moveCount, 0));
            if (taskCalledCount % 30 != 0 || taskCalledCount == 0) return;

            if (moveCount > 0) {
                movePlayer.addGridNumber(1);
            } else if (moveCount == 0) {
                Optional<Field.GridPair> warpSet = field.getLadderAndSnake().stream()
                        .filter(s -> s.from() == movePlayer.getGridNumber())
                        .findAny();

                warpSet.ifPresentOrElse(
                        gridPair -> movePlayer.setGridNumber(gridPair.to()),
                        () -> {
                            setNextPlayer();
                            taskCallableList.remove(this);
                        });
            } else if (moveCount == -1) {
                setNextPlayer();
                taskCallableList.remove(this);
            }
            moveCount--;
        }
    }

    enum Phase implements TaskCallable {
        ChangeTurn { //ターン切り替え時

            @Override
            void _task(TaskCallArgument arg) {
                int minDice = properties.getInt(MappedProperties.IntKey.minDice);
                int maxDice = properties.getInt(MappedProperties.IntKey.maxDice);
                diceNum = diceRandom.nextInt(maxDice + 1 - minDice) + minDice;
                roulette = new CardRoulette(minDice, maxDice, diceNum);

                taskCallableList.add(roulette);
                glRendererList.add(roulette);

                phase = DicePrepare;
            }
        },

        DicePrepare { //ダイスを振るためにエンターキーを待つ状態

            @Override
            void _task(TaskCallArgument arg) {
                if (glDisplay.isKeyPressed(VK_ENTER)) {
                    glDisplay.resetKey(VK_ENTER);

                    roulette.prepareStop();

                    setPhase(Dice);
                }
            }
        },
        Dice { //ダイスを振って、その目が確定するのを待っている状態

            @Override
            void _task(TaskCallArgument arg) {
                if (roulette.isStopped()) {
                    taskCallableList.add(new PlayerMove(diceNum, moveStandByPlayer));

                    setPhase(Moving);
                }
            }
        },
        Moving { // ダイスの通りにプレイヤーが進んでいる状態

            @Override
            void _task(TaskCallArgument arg) {
//playerMoveに移譲
            }

            @Override
            void end() {
                taskCallableList.remove(roulette);
                glRendererList.remove(roulette);
                roulette = null;
            }
        };

        @Override
        public final void task(TaskCallable.TaskCallArgument arg) {
            if (glDisplay.isKeyPressed(VK_ESCAPE)) {
                glDisplay.endWindow();
            }
            _task(arg);
        }

        abstract void _task(TaskCallable.TaskCallArgument arg);

        void end() {

        }
    }
}
