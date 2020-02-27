package com.walle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    public static void debug(Object... msg) {
        debug(log, msg);
    }

    public static void debug(Logger log, Object... msg) {
        log.debug(StrUtil.joinObj(msg, ", "));
    }

    public static void info(Object... msg) {
        info(log, msg);
    }

    public static void info(Logger log, Object... msg) {
        log.info(StrUtil.joinObj(msg, ", "));
    }

    public static void warn(Object... msg) {
        warn(log, msg);
    }

    public static void warn(Logger log, Object... msg) {
        log.warn(StrUtil.joinObj(msg, ", "));
    }

    public static void error(Object... msg) {
        error(log, msg);
    }

    public static void error(Logger log, Object... msg) {
        log.error(StrUtil.joinObj(msg, ", "));
    }
}
