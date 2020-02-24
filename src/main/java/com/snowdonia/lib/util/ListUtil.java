package com.snowdonia.lib.util;

import java.util.List;

public class ListUtil
{
    public static String getFirst(List<String> list)
    {
        if (list == null) return null;
        if (list.isEmpty()) return null;
        return list.get(0);
    }
}
