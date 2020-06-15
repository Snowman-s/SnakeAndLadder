package snakeandladder.roulette;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;
import snakeandladder.display.GLDisplay;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class CardRoulette extends Roulette {
    protected final int min;
    protected final int max;

    private int nowNumber = 0;

    private TextRenderer textRenderer;

    public CardRoulette(int min, int max, int number) {
        super(number);
        this.min = min;
        this.max = max;

        textRenderer = new TextRenderer(Font.decode("Arial 50"));
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        textRenderer.beginRendering(glAutoDrawable.getSurfaceWidth(), glAutoDrawable.getSurfaceHeight());

        textRenderer.draw(Integer.toString(nowNumber), 0, 0);

        textRenderer.endRendering();
    }

    @Override
    public void task(TaskCallArgument arg) {
        if (!isStopped()) {
            nowNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        }
    }

    @Override
    public void prepareStop() {
        nowNumber = number;
        perfectStop();
    }

    public void setNowNumber(int nowNumber) {
        this.nowNumber = nowNumber;
    }
}
