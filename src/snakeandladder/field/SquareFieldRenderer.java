package snakeandladder.field;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.glrenderer.AbstractGLRenderer;

public class SquareFieldRenderer extends AbstractGLRenderer {
    private final Field field;

    public SquareFieldRenderer(Field field) {
        this.field = field;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
    }
}
