package snakeandladder.log;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.*;

public final class MyLogger {
    private MyLogger() {
    }

    private static final Logger logger;
    private static final Handler fileHandler;

    static {
        Handler fileHandlerTmp;
        logger = Logger.getLogger("snakeandladder");
        try {
            fileHandlerTmp = new FileHandler("./logs/snakeandladder%g.log", 1_000_000, 10);
        } catch (IOException e) {
            System.err.println("couldn't open log file");
            fileHandlerTmp = null;
        }

        fileHandler = fileHandlerTmp;

        fileHandler.setFormatter(new SimpleFormatter());

        logger.addHandler(fileHandler);

        logger.setLevel(Level.ALL);
    }

    public static void loggerIfAbsent(Consumer<Logger> loggerConsumer) {
        loggerConsumer.accept(Objects.requireNonNullElseGet(logger, Logger::getAnonymousLogger));
    }
}
