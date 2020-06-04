package snakeandladder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.field.FieldRenderer;
import snakeandladder.glrenderer.AbstractGLRenderer;
import snakeandladder.player.Player;
import snakeandladder.utility.ColorUtility;

import java.util.List;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

public class FieldPlayerRenderer extends AbstractGLRenderer {
    private final FieldRenderer fieldRenderer;
    private final List<Player> playerList;

    public FieldPlayerRenderer(FieldRenderer fieldRenderer, List<Player> playerList) {
        this.fieldRenderer = fieldRenderer;
        this.playerList = playerList;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        fieldRenderer.render(glDisplay, glAutoDrawable);
        GL2 gl = glAutoDrawable.getGL().getGL2();
        playerList.forEach(p -> {
            gl.glPushMatrix();
            float x = fieldRenderer.getGridXAsFloat(p.getGridNumber()),
                    y = fieldRenderer.getGridYAsFloat(p.getGridNumber()),
                    z = fieldRenderer.getGridZAsFloat(p.getGridNumber());
            gl.glTranslated(x, y + fieldRenderer.getGridWidth() / 2F, z);

            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE,
                    ColorUtility.getColor(0, 1, 1, 1), 0);
            glDisplay.getGLUT().glutSolidCube(fieldRenderer.getGridWidth() / 2F);

            gl.glPopMatrix();
        });
    }
}
