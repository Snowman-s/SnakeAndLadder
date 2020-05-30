package snakeandladder.glrenderer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;

public abstract class AbstractGLRenderer implements GLRenderer {
    @Override
    public final void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glPushMatrix();
        renderBody(glDisplay, glAutoDrawable);
        gl.glPopMatrix();
    }

    protected abstract void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable);
}
