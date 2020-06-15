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
    private final int sqPlayerNum;
    private Player emphasizedPlayer = null;

    public FieldPlayerRenderer(FieldRenderer fieldRenderer, List<Player> playerList) {
        this.fieldRenderer = fieldRenderer;
        this.playerList = List.copyOf(playerList);

        sqPlayerNum = (int) Math.ceil(Math.sqrt(playerList.size()));
    }

    public void emphasizePlayer(Player player) {
        this.emphasizedPlayer = player;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        fieldRenderer.render(glDisplay, glAutoDrawable);
        GL2 gl = glAutoDrawable.getGL().getGL2();
        for (int i = 0; i < playerList.size(); i++) {
            Player p = playerList.get(i);
            gl.glPushMatrix();
            float x = fieldRenderer.getGridXAsFloat(p.getGridNumber()),
                    y = fieldRenderer.getGridYAsFloat(p.getGridNumber()),
                    z = fieldRenderer.getGridZAsFloat(p.getGridNumber());
            float distancePlayer = sqPlayerNum == 1 ?
                    0 : fieldRenderer.getGridWidth() / 3F / (sqPlayerNum - 1);
            gl.glTranslated(x + (i / sqPlayerNum - sqPlayerNum / 2F + .5) * distancePlayer,
                    y + fieldRenderer.getGridWidth() / 2F,
                    z + (i % sqPlayerNum - sqPlayerNum / 2F + .5F) * distancePlayer);

            float[] playerColor = p == emphasizedPlayer ?
                    ColorUtility.getColor(1, 0, 0, 1) :
                    ColorUtility.getColor(0, 1, 1, 1);

            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE,
                    playerColor, 0);
            gl.glScaled(0.15F, 1F, 0.15F);
            glDisplay.getGLUT().glutSolidCube(fieldRenderer.getGridWidth());

            gl.glPopMatrix();
        }
    }
}
