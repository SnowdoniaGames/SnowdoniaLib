package com.snowdonia.lib.util;

import java.io.File;

@SuppressWarnings("UnusedReturnValue")
public class FileUtil
{
	public enum OperatingSystem
	{
		WINDOWS,
		UNIX,
		MAC,
	}

	public static OperatingSystem getOperatingSystem()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("mac os x"))
		{
			return OperatingSystem.MAC;
		}
		else if (os.startsWith("windows"))
		{
			return OperatingSystem.WINDOWS;
		}
		else
		{
			return OperatingSystem.UNIX;
		}
	}

	public static File getSaveDir(String appName)
	{
		File file = null;

		OperatingSystem os = getOperatingSystem();
		if (os.equals(OperatingSystem.WINDOWS))
		{
			String appData = System.getenv("APPDATA");

			if (appData != null)
			{
				file = new File(appData + File.separator + appName);
			}
			else
			{
				file = new File(System.getProperty("user.home") + File.separator + appName);
			}
		}
		else if (os.equals(OperatingSystem.MAC))
		{
			file = new File("/Library/Application Support" + File.separator + appName);
		}
		else if (os.equals(OperatingSystem.UNIX))
		{
			file = new File(System.getProperty("user.home") + File.separator + appName);
		}

		//create directory if it doesn't exist
		if (file != null && !file.exists())
		{
			if (!file.mkdirs())
			{
				System.out.println("Error Creating Save Directory");
			}
		}

		return file;
	}

	public static boolean deleteFile(File file)
	{
		return file.delete();
	}

	public static boolean deleteDirectory(File dir)
	{
		File[] allContents = dir.listFiles();
		if (allContents != null)
		{
			for (File file : allContents)
			{
				deleteDirectory(file);
			}
		}
		return dir.delete();
	}
}
