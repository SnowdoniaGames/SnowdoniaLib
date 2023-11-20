package com.snowdonia.lib.logging;

import org.apache.logging.log4j.*;

import java.io.File;

@SuppressWarnings({"rawtypes", "ResultOfMethodCallIgnored", "unused"})
public class SnowdoniaLogger
{
    public static void init(String logDir)
    {
        // create log directory if needed
        File logDirFile = new File(logDir);
        if (!logDirFile.exists())
        {
            logDirFile.mkdirs();
        }

        // set log file name
        System.setProperty("logfile.name", logDir + File.separator + "log.txt");
    }

    public static void trace(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.trace(log);
    }

    public static void debug(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.debug(log);
    }

    public static void info(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.info(log);
    }

    public static void warn(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.warn(log);
    }

    public static void error(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.error(log);
    }

    public static void fatal(Class clazz, String log)
    {
        Logger logger = LogManager.getLogger(clazz);
        logger.fatal(log);

        //force the program to close
        System.exit(0);
    }
}
