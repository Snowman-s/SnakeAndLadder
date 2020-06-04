package snakeandladder.field;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import snakeandladder.display.GLDisplay;
import snakeandladder.utility.ColorUtility;

import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

//正方形+αの形で描画
public class SquareFieldRenderer extends FieldRenderer {
    private final int width;
    /**
     * 初期位置のみに影響し、実際このheightのぶんだけマスが書かれるとは限らない。
     */
    private final int height;

    private final ModOrientation modOrientation;

    public SquareFieldRenderer(Field field) {
        this(field, ModOrientation.Vertical);
    }

    public SquareFieldRenderer(Field field, ModOrientation modOrientation) {
        super(Objects.requireNonNull(field), 0.08F);
        this.modOrientation = modOrientation;

        int sqGrid = (int) (Math.sqrt(gridNum));

        this.width = sqGrid;
        this.height = sqGrid;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        modOrientation.rotate(gl);
        gl.glScaled(1, 0.3F, 1);

        gl.glLineWidth(2);
        for (int i = 0; i < gridNum; i++) {
            gl.glPushMatrix();
            float translateX = getGridXAsFloat(i),
                    translateY = getGridYAsFloat(i),
                    translateZ = getGridZAsFloat(i);
            if (i != 0) {
                gl.glBegin(GL_LINES);
                gl.glVertex3d(getGridXAsFloat(i - 1),
                        getGridYAsFloat(i - 1),
                        getGridZAsFloat(i - 1));
                gl.glVertex3d(translateX,
                        translateY,
                        translateZ);
                gl.glEnd();
            }
            gl.glTranslated(translateX, translateY, translateZ);
            float[] color;
            if (i == 0) {
                color = ColorUtility.getColor(0, 0, 1, 1);
            } else if (i == gridNum - 1) {
                color = ColorUtility.getColor(1, 0, 0, 1);
            } else {
                color = ColorUtility.getColor(1, 1, 1, 1);
            }
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, color, 0);
            glDisplay.getGLUT().glutSolidCube(gridWidth);
            gl.glPopMatrix();
        }
        for (Field.GridPair gridPair : field.getLadderAndSnake()) {
            int fromGrid = gridPair.getFrom(), toGrid = gridPair.getTo();
            float[] color;
            if (fromGrid > toGrid) {
                color = ColorUtility.getColor(0, 1, 0, 1);
            } else {
                color = ColorUtility.getColor(1, 1, 0, 1);
            }
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, color, 0);

            gl.glBegin(GL_LINE_STRIP);

            gl.glVertex3d(getGridXAsFloat(fromGrid), 0, getGridZAsFloat(fromGrid));
            gl.glVertex3d(getGridXAsFloat(fromGrid), 20 * gridWidth, getGridZAsFloat(fromGrid));
            gl.glVertex3d(getGridXAsFloat(toGrid), 20 * gridWidth, getGridZAsFloat(toGrid));
            gl.glVertex3d(getGridXAsFloat(toGrid), 0, getGridZAsFloat(toGrid));

            gl.glEnd();
        }
    }

    int getGridX(int grid) {
        int x = grid % width, y = grid / width + 1;
        if (y % 2 == 0) {
            x = width - x - 1;
        }
        return x;
    }

    int getGridY(int grid) {
        return grid / width + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getGridXAsFloat(int gridNum) {
        int intX = getGridX(gridNum);

        return (-width + 1F + intX * 2) * gridWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getGridYAsFloat(int gridNum) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getGridZAsFloat(int gridNum) {
        int intY = getGridY(gridNum);

        return (-height - 1F + intY * 2) * gridWidth;
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
