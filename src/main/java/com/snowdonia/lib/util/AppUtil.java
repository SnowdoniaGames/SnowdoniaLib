package com.snowdonia.lib.util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class AppUtil
{
    public static String getFocusedApplication()
    {
        User32 user32 = User32.INSTANCE;
        WinDef.HWND hWnd = user32.GetForegroundWindow();

        char[] path = new char[512];
        user32.GetWindowModuleFileName(hWnd, path, 512);

        char[] path2 = new char[512];
        user32.GetWindowText(hWnd, path2, 512);

        return new String(path2).trim();
    }

    public static boolean isFocusedApplication(String appName)
    {
        return getFocusedApplication().equals(appName);
    }
}
