package com.snowdonia.lib.util;

import com.snowdonia.lib.file.xml.lib.XmlElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmlUtil
{
    public static List<XmlElement> getElements(XmlElement root, String path, String value)
    {
        List<XmlElement> out = new ArrayList<>();

        String[] pathSplit;
        if (path == null)
        {
            pathSplit = new String[0];
        }
        else
        {
            pathSplit = path.split("/");
        }

        recurseElements(root, out, pathSplit, value);

        return out;
    }

    private static void recurseElements(XmlElement element, List<XmlElement> out, String[] pathSplit, String value)
    {
        if (pathSplit.length > 0)
        {
            List<XmlElement> children = element.getChildren(pathSplit[0]);
            pathSplit = Arrays.copyOfRange(pathSplit, 1, pathSplit.length);

            for (XmlElement child : children)
            {
                recurseElements(child, out, pathSplit, value);
            }
        }
        else
        {
            List<XmlElement> children = element.getChildren(value);
            out.addAll(children);
        }
    }

    public static List<String> getValues(XmlElement root, String path, String value)
    {
        List<String> out = new ArrayList<>();

        String[] pathSplit;
        if (path == null)
        {
            pathSplit = new String[0];
        }
        else
        {
            pathSplit = path.split("/");
        }

        recurseValues(root, out, pathSplit, value);

        return out;
    }

    private static void recurseValues(XmlElement element, List<String> out, String[] pathSplit, String value)
    {
        if (pathSplit.length > 0)
        {
            List<XmlElement> children = element.getChildren(pathSplit[0]);
            pathSplit = Arrays.copyOfRange(pathSplit, 1, pathSplit.length);

            for (XmlElement child : children)
            {
                recurseValues(child, out, pathSplit, value);
            }
        }
        else
        {
            List<XmlElement> children = element.getChildren(value);

            for (XmlElement child : children)
            {
                out.add(child.value);
            }
        }
    }

    public static List<String> getAttributes(XmlElement root, String path, String value)
    {
        List<String> out = new ArrayList<>();

        String[] pathSplit = path.split("/");
        recurseAttributes(root, out, pathSplit, value);

        return out;
    }

    private static void recurseAttributes(XmlElement element, List<String> out, String[] pathSplit, String value)
    {
        if (pathSplit.length > 0)
        {
            List<XmlElement> children = element.getChildren(pathSplit[0]);
            pathSplit = Arrays.copyOfRange(pathSplit, 1, pathSplit.length);

            for (XmlElement child : children)
            {
                recurseAttributes(child, out, pathSplit, value);
            }
        }
        else
        {
            String attr = element.getAttribute(value);
            if (attr != null) out.add(attr);
        }
    }
}