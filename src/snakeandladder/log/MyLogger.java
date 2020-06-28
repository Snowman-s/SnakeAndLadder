package snakeandladder.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.function.Consumer;
import java.util.logging.*;

public final class MyLogger {
    private MyLogger() {
    }

    private static final Logger logger;

    static {
        Handler fileHandler;
        Logger loggerTmp = null;
        try {
            Files.createDirectory(Path.of("./logs/"));
            fileHandler = new FileHandler("./logs/snakeandladder%g.log", 1_000_000, 10);
            fileHandler.setFormatter(new SimpleFormatter());

            loggerTmp = Logger.getLogger("snakeandladder");
            loggerTmp.addHandler(fileHandler);
            loggerTmp.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("couldn't open log file");
        } finally {
            logger = loggerTmp;
        }
    }

    public static void loggerIfAbsent(Consumer<Logger> loggerConsumer) {
        if (logger != null) {
            loggerConsumer.accept(logger);
        } else {
            loggerConsumer.accept(Logger.getAnonymousLogger());
        }
    }
}
