package main.de.jezlynn.medialist.helper;

import main.de.jezlynn.medialist.logger.FMLLog;
import main.de.jezlynn.medialist.reference.Reference;
import org.apache.logging.log4j.Level;

/**
 * Created by Michael on 24.08.2014.
 */
public class LogHelper {

    public static void log(Level logLevel, Object object) {
        FMLLog.log(Reference.NAME, logLevel, String.valueOf(object));
    }

    public static void all(Object object) {
        log(Level.ALL, object);
    }

    public static void debug(Object object) {
        log(Level.DEBUG, object);
    }

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }
}
