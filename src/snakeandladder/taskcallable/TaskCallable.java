package snakeandladder.taskcallable;

import snakeandladder.display.GLDisplay;

@FunctionalInterface
public interface TaskCallable {
    void task(GLDisplay glDisplay);
}
