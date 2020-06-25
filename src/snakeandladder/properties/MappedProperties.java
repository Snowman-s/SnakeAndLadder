package snakeandladder.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class MappedProperties {
    Map<IntKey, Integer> integerMap = new HashMap<>();
    Map<LongKey, Long> longMap = new HashMap<>();
    Map<StringKey, String> stringMap = new HashMap<>();

    MappedProperties() {
    }

    public int getInt(IntKey intKey) {
        return integerMap.get(intKey);
    }

    public long getLong(LongKey longKey) {
        return longMap.get(longKey);
    }

    public String getString(StringKey stringKey) {
        return stringMap.get(stringKey);
    }

    public enum IntKey {
        minDice("min_dice", () -> 1),
        maxDice("max_dice", () -> 6),
        gridNumber("num_grid", () -> 49),
        playerNumber("num_player", () -> 4);

        private final String keyString;
        private final IntSupplier defaultData;

        IntKey(String keyString, IntSupplier defaultData) {
            this.keyString = keyString;
            this.defaultData = defaultData;
        }

        String getKeyString() {
            return keyString;
        }

        String getDefaultStringData() {
            return Integer.toString(defaultData.getAsInt());
        }

        int getDefaultIntData() {
            return defaultData.getAsInt();
        }
    }

    public enum LongKey {
        fieldSeed("field_seed", System::currentTimeMillis),
        diceSeed("dice_seed", System::currentTimeMillis);

        private final String keyString;
        private final LongSupplier defaultLongData;

        LongKey(String keyString, LongSupplier defaultData) {
            this.keyString = keyString;
            this.defaultLongData = defaultData;
        }

        String getKeyString() {
            return keyString;
        }

        String getDefaultStringData() {
            return Long.toString(defaultLongData.getAsLong());
        }

        long getDefaultLongData() {
            return defaultLongData.getAsLong();
        }
    }

    public enum StringKey {
        ;
        private final String keyString;
        private final Supplier<String> defaultData;

        StringKey(String keyString, Supplier<String> defaultData) {
            this.keyString = keyString;
            this.defaultData = defaultData;
        }

        String getKeyString() {
            return keyString;
        }

        public String getDefaultData() {
            return defaultData.get();
        }
    }
}
