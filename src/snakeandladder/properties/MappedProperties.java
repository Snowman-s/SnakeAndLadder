package snakeandladder.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MappedProperties {
    Map<IntKey, Integer> integerMap = new HashMap<>();
    Map<StringKey, String> stringMap = new HashMap<>();

    protected MappedProperties(){}

    public int getInt(IntKey intKey) {
        return integerMap.get(intKey);
    }

    public String getString(StringKey stringKey) {
        return stringMap.get(stringKey);
    }

    public enum IntKey {
        minDice("min_dice", 1),
        maxDice("max_dice", 6);

        final String keyString;
        final String defaultStringData;
        final int defaultIntData;

        IntKey(String keyString, int defaultData) {
            this.keyString = keyString;
            this.defaultIntData = defaultData;
            this.defaultStringData = Integer.toString(defaultData);
        }
    }

    public enum StringKey {
        ;
        final String keyString;
        final String defaultData;

        StringKey(String keyString, String defaultData) {
            this.keyString = keyString;
            this.defaultData = defaultData;
        }
    }
}
