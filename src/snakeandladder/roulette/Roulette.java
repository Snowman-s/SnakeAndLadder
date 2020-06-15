package snakeandladder.roulette;

import snakeandladder.glrenderer.AbstractGLRenderer;
import snakeandladder.taskcallable.TaskCallable;

public abstract class Roulette extends AbstractGLRenderer implements TaskCallable {
    protected final int number;

    private volatile boolean stopped;

    public Roulette(int number) {
        this.number = number;
    }

    public abstract void prepareStop();

    protected synchronized final void perfectStop(){
        stopped = true;
    }

    public synchronized final boolean isStopped(){
        return stopped;
    }
}
