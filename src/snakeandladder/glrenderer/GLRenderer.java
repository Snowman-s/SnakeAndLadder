package snakeandladder.glrenderer;

import snakeandladder.display.GLDisplay;

@FunctionalInterface
public interface GLRenderer {
    void render(GLDisplay glDisplay);
}
