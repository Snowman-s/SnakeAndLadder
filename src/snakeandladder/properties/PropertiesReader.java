package snakeandladder.properties;

import snakeandladder.log.MyLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PropertiesReader {
    static final String propertiesFileName = "snakeandladder.properties";
    private static Properties properties;

    private PropertiesReader() {
    }

    public static MappedProperties load() {
        MappedProperties mappedProperties = new MappedProperties();

        if (properties == null) {
            properties = new Properties();
            try (FileInputStream inputStream = new FileInputStream(propertiesFileName)) {
                properties.load(inputStream);
            } catch (IOException ioException) {
                MyLogger.loggerIfAbsent(l -> l.warning(propertiesFileName));
            }
        }

        //int
        for (MappedProperties.IntKey intKey : MappedProperties.IntKey.values()) {
            String key = intKey.getKeyString();
            String property = properties.getProperty(key, intKey.getDefaultStringData());
            int intProperty;

            try {
                intProperty = Integer.parseInt(property);
            } catch (NumberFormatException e) {
                MyLogger.loggerIfAbsent(l -> l.info("property " + key + "(" + property + ") cannot convert to int"));
                intProperty = intKey.getDefaultIntData();
            }
            mappedProperties.integerMap.put(intKey, intProperty);
        }

        //long
        for (MappedProperties.LongKey longKey : MappedProperties.LongKey.values()) {
            String key = longKey.getKeyString();
            String property = properties.getProperty(longKey.getKeyString(), longKey.getDefaultStringData());
            long longProperty;

            try {
                longProperty = Long.parseLong(property);
            } catch (NumberFormatException e) {
                MyLogger.loggerIfAbsent(l -> l.info("property " + key + "(" + property + ") cannot convert to long"));
                longProperty = longKey.getDefaultLongData();
            }
            mappedProperties.longMap.put(longKey, longProperty);
        }

        //String
        for (MappedProperties.StringKey stringKey : MappedProperties.StringKey.values()) {
            String property = properties.getProperty(stringKey.getKeyString(), stringKey.getDefaultData());
            mappedProperties.stringMap.put(stringKey, property);
        }

        validDataTestOrFix.forEach(
                mappedPropertiesConsumer -> mappedPropertiesConsumer.accept(mappedProperties)
        );

        return mappedProperties;
    }

    final static List<Consumer<MappedProperties>> validDataTestOrFix = List.of(
            //maxDice >= minDice
            p -> {
                int maxDice = p.integerMap.get(MappedProperties.IntKey.maxDice);
                int minDice = p.integerMap.get(MappedProperties.IntKey.minDice);
                String maxDiceKey = MappedProperties.IntKey.maxDice.getKeyString();
                String minDiceKey = MappedProperties.IntKey.minDice.getKeyString();
                if (maxDice < minDice) {
                    p.integerMap.put(MappedProperties.IntKey.maxDice, minDice);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            "property " + maxDiceKey + " < " + minDiceKey +
                                    "; fixed to:" + maxDiceKey + " = " + minDiceKey);
                }
            },
            //gridNumber >= 2
            p -> {
                int gridNum = p.integerMap.get(MappedProperties.IntKey.gridNumber);
                String gridNumKey = MappedProperties.IntKey.gridNumber.getKeyString();
                if (gridNum < 2) {
                    p.integerMap.put(MappedProperties.IntKey.gridNumber, 2);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            "property " + gridNumKey + " < 2" +
                                    "; fixed to:" + gridNumKey + " = 2");
                }
            }
    );
}
