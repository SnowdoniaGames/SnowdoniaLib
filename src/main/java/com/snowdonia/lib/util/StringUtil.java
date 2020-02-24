package com.snowdonia.lib.util;

public class StringUtil
{
    public static boolean exists(String s)
    {
        if (s == null) return false;
        return !s.isEmpty();
    }
}
