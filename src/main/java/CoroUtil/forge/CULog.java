package CoroUtil.forge;

import CoroUtil.config.ConfigCoroUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CULog {

    private static final Logger LOGGER = LogManager.getLogger(CoroUtil.modID);

    /**
     * For seldom used but important things to print out in production
     *
     * @param string
     */
    public static void log(String string) {
        if (ConfigCoroUtil.useLoggingLog) {
            LOGGER.info(string);
        }
    }

    /**
     * For logging warnings/errors
     *
     * @param string
     */
    public static void err(String string) {
        if (ConfigCoroUtil.useLoggingError) {
            LOGGER.error(string);
        }
    }

    /**
     * For debugging things
     *
     * @param string
     */
    public static void dbg(String string) {
        if (ConfigCoroUtil.useLoggingDebug) {
            LOGGER.info(string);
        }
    }

}
