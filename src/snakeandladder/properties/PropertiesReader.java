package snakeandladder.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PropertiesReader {
    static final String propertiesFileName = "snakeandladder.properties";

    private PropertiesReader() {
    }

    public static MappedProperties load() {
        MappedProperties mappedProperties = new MappedProperties();
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertiesFileName)) {
            properties.load(inputStream);
        } catch (IOException ioException) {
            Logger.getAnonymousLogger().log(Level.WARNING, propertiesFileName);
        }

        //int
        for (MappedProperties.IntKey intKey : MappedProperties.IntKey.values()) {
            String property = properties.getProperty(intKey.keyString, intKey.defaultStringData);
            int intProperty;

            try {
                intProperty = Integer.parseInt(property);
            } catch (NumberFormatException e) {
                intProperty = intKey.defaultIntData;
            }
            mappedProperties.integerMap.put(intKey, intProperty);
        }

        //String
        for (MappedProperties.StringKey stringKey : MappedProperties.StringKey.values()) {
            String property = properties.getProperty(stringKey.keyString, stringKey.defaultData);
            mappedProperties.stringMap.put(stringKey, property);
        }

        validDataTestOrFix.forEach(
                mappedPropertiesConsumer -> mappedPropertiesConsumer.accept(mappedProperties)
        );

        return mappedProperties;
    }

    final static List<Consumer<MappedProperties>> validDataTestOrFix = List.of(
            p -> {
                int maxDice = p.integerMap.get(MappedProperties.IntKey.maxDice);
                int minDice = p.integerMap.get(MappedProperties.IntKey.minDice);
                String maxDiceKey = MappedProperties.IntKey.maxDice.keyString;
                String minDiceKey = MappedProperties.IntKey.minDice.keyString;
                if (maxDice < minDice) {
                    p.integerMap.put(MappedProperties.IntKey.maxDice, minDice);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            "property " + maxDiceKey + " < " + minDiceKey +
                                    "; fixed to:" + maxDiceKey + " = " + minDiceKey);
                }
            }
    );
}
