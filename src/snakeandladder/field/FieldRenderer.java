package snakeandladder.field;

import snakeandladder.glrenderer.AbstractGLRenderer;

public abstract class FieldRenderer extends AbstractGLRenderer {
    protected final Field field;
    protected final int gridNum;

    protected float gridWidth;

    public FieldRenderer(Field field, float gridWidth) {
        this.field = field;
        this.gridNum = field.getGridNum();
        this.gridWidth = gridWidth;
    }

    /**
     * これを実装するクラスは、マスの描画にこのメソッドを用いることが<b>強制されます</b>。
     *
     * @param gridNum マスの番号
     * @return そのマスのx座標
     */
    public abstract float getGridXAsFloat(int gridNum);

    /**
     * これを実装するクラスは、マスの描画にこのメソッドを用いることが<b>強制されます</b>。
     *
     * @param gridNum マスの番号
     * @return そのマスのy座標
     */
    public abstract float getGridYAsFloat(int gridNum);

    /**
     * これを実装するクラスは、マスの描画にこのメソッドを用いることが<b>強制されます</b>。
     *
     * @param gridNum マスの番号
     * @return そのマスのx座標
     */
    public abstract float getGridZAsFloat(int gridNum);


    public float getGridWidth() {
        return gridWidth;
    }
}
