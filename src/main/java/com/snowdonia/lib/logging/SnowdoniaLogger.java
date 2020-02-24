package com.snowdonia.lib.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.net.URL;

@SuppressWarnings("unused")
public class SnowdoniaLogger
{
	public static SnowdoniaLogger logger;

	public SnowdoniaLogger(File logDir)
	{
		if (!logDir.exists())
		{
			if (!logDir.mkdirs())
			{
				SnowdoniaLogger.preLog(SnowdoniaLogger.class, "Unable to initialize log directory");
			}
		}

		System.setProperty("logfile.name", logDir + File.separator + "log.txt");

		//attempt to load release logging, if that fails load standard, if that fails use default
		URL urlRelease = getClass().getResource("/log4j-release.properties");
		if (urlRelease != null)
		{
			PropertyConfigurator.configure(urlRelease);
		}
		else
		{
			URL urlBase = getClass().getResource("/log4j.properties");
			if (urlBase != null)
			{
				PropertyConfigurator.configure(urlBase);
			}
		}

		//set logger for use with library
		logger = this;
	}

	/**
	 * Logging for fatal exceptions that can occur before the logger has been initialized
	 * Should only be used for rare occasions
	 * @param clazz The class to log for
	 * @param log The log information
	 */
	public static void preLog(Class clazz, String log)
	{
		//save a crash report to the location of the executable
		System.setProperty("logfile.name", "crash-report.txt");

		//load whatever the default log4j is
		PropertyConfigurator.configure(clazz.getResource("/log4j.properties"));

		//log our error
		Logger logger = Logger.getLogger(clazz);
		logger.debug(log);

		//exit the application
		System.exit(100);
	}

	public void trace(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.trace(log);
	}

	public void debug(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.debug(log);
	}

	public void info(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.info(log);
	}

	public void warn(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.warn(log);
	}

	public void error(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.error(log);
	}

	public void fatal(Class clazz, String log)
	{
		Logger logger = Logger.getLogger(clazz);
		logger.fatal(log);

		//force the program to close
		System.exit(0);
	}
}
