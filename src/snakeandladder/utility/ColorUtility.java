package snakeandladder.utility;

import java.util.HashSet;
import java.util.Set;

public class ColorUtility {
    private ColorUtility() {
    }

    private static final Set<float[]> colorSet;

    static {
        colorSet = new HashSet<>();
    }

    /**
     * 返ってきた配列は、決して変更しないでください。
     *
     * @return {@code {r, g, b, a}} である配列;<b>決して変更しないでください</b>
    */
     public static float[] getColor(float r, float g, float b, float a) {
        for (float[] color : colorSet) {
            if (color[0] == r &&
                    color[1] == g &&
                    color[2] == b &&
                    color[3] == a) {
                return color;
            }
        }
        float[] returnColor = new float[]{r, g, b, a};
        colorSet.add(returnColor);
        return returnColor;
    }
}
