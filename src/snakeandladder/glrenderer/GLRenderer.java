package snakeandladder.glrenderer;

import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;

@FunctionalInterface
public interface GLRenderer {
    void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable);
}
