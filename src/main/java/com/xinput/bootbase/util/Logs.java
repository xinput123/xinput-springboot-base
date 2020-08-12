package com.xinput.bootbase.util;

/**
 * Returns the logger for the calling class. Can be used to determine the
 * Logger, reduces errors when copy&paste.
 * Created by xiaoyong on 18/11/15.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {

    private final static int THREAD_TYPE_DEEP = 2;

    /**
     * A custom security manager that exposes the getClassContext() information
     */
    static class CallerSecurityManager extends SecurityManager {

        public String getCallerClassName(int callStackDepth) {
            return getClassContext()[callStackDepth].getName();
        }
    }

    /**
     * Using a Custom SecurityManager to get the caller classname.
     */
    private static final CallerSecurityManager CSM = new CallerSecurityManager();

    /**
     * Get a logger with current class name
     *
     * @return logger
     */
    public static Logger get() {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        return determineLogger(className);
    }


    /**
     * Determines the class and the appropiate logger of the calling class.
     *
     * @return The (slf4j) logger of the caller
     */
    static Logger determineLogger(String callerClassName) {
        return LoggerFactory.getLogger(callerClassName);
    }

}
