package snakeandladder.field;

import com.jogamp.nativewindow.util.Point;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.glrenderer.AbstractGLRenderer;
import snakeandladder.utility.ColorUtility;

import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

//正方形+αの形で描画
public class SquareFieldRenderer extends AbstractGLRenderer {
    private final Field field;
    private final int gridNum;
    private final int width;
    /**
     * 初期位置のみに影響し、実際このheightのぶんだけマスが書かれるとは限らない。
     */
    private final int height;

    private final ModOrientation modOrientation;

    private final float GRID_WIDTH = 0.08F;

    public SquareFieldRenderer(Field field) {
        this(field, ModOrientation.Vertical);
    }

    public SquareFieldRenderer(Field field, ModOrientation modOrientation) {
        this.field = Objects.requireNonNull(field);
        this.modOrientation = modOrientation;

        this.gridNum = field.getGridNum();
        int sqGrid = (int) (Math.sqrt(gridNum));

        this.width = sqGrid;
        this.height = sqGrid;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        modOrientation.rotate(gl);
        gl.glScaled(GRID_WIDTH * 2F, GRID_WIDTH * 0.6F, GRID_WIDTH * 2F);
        gl.glTranslated(-width / 2F + 0.5F,
                0,
                -height / 2F - 0.5F);
        gl.glPushMatrix();
        for (int i = 0; i < gridNum; i++) {
            int translateX = (i % width == 0 ? 0 : ((i / width) % 2 < 1 ? 1 : -1));
            int translateZ = (i % width == 0 ? 1 : 0);
            if (i != 0) {
                gl.glBegin(GL_LINES);
                gl.glVertex3d(0, 0, 0);
                gl.glVertex3d(translateX, 0, translateZ);
                gl.glEnd();
            }
            gl.glTranslated(
                    translateX,
                    0,
                    translateZ
            );
            float[] color;
            if (i == 0) {
                color = ColorUtility.getColor(0, 0, 1, 1);
            } else if (i == gridNum - 1) {
                color = ColorUtility.getColor(1, 0, 0, 1);
            } else {
                color = ColorUtility.getColor(1, 1, 1, 1);
            }
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, color, 0);
            glDisplay.getGLUT().glutSolidCube(0.5F);
        }
        gl.glPopMatrix();
        for (Field.GridPair gridPair : field.getLadderAndSnake()) {
            int fromGrid = gridPair.getFrom(), toGrid = gridPair.getTo();
            float[] color;
            if (fromGrid > toGrid) {
                color = ColorUtility.getColor(0, 1, 0, 1);
            } else {
                color = ColorUtility.getColor(1, 1, 0, 1);
            }
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, color, 0);

            Point from = getGridPoint(fromGrid),
                    to = getGridPoint(toGrid);

            gl.glLineWidth(2);
            gl.glBegin(GL_LINE_STRIP);

            gl.glVertex3d(from.getX(), 0, from.getY());
            gl.glVertex3d(from.getX(), 10, from.getY());
            gl.glVertex3d(to.getX(), 10, to.getY());
            gl.glVertex3d(to.getX(), 0, to.getY());

            gl.glEnd();
        }
    }

    Point getGridPoint(int grid) {
        int x = grid % width, y = grid / width + 1;
        if (y % 2 == 0) {
            x = width - x - 1;
        }
        return new Point(x, y);
    }

    public enum ModOrientation {
        Vertical {
            @Override
            protected void rotate(GL2 gl) {
                //nothing
            }
        }, Horizontal {
            @Override
            protected void rotate(GL2 gl) {
                gl.glRotated(90, 0, 1, 0);
            }
        };

        protected abstract void rotate(GL2 gl);
    }
}
