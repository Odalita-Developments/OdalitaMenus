package nl.odalitadevelopments.menus.nms.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class OdalitaLogger {

    private OdalitaLogger() {
    }

    private static final Logger LOGGER = Logger.getLogger("OdalitaMenus");

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    public static void error(Throwable throwable) {
        LOGGER.log(Level.SEVERE, null, throwable);
    }

    public static void debug(String message) {
        info("[DEBUG] " + message);
    }
}