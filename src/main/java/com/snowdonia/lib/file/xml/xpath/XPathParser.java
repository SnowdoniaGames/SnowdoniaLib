package com.snowdonia.lib.file.xml.xpath;

public class XPathParser
{
    public static String inA = "/ResearchProjectDef[defName=\"VFES_SentryGuns\"]";
    public static String inB = "/ResearchProjectDef[defName=\"Firefoam\"]/prerequisites/li";
    public static String inC = "/ResearchProjectDef[defName=\"Machining\" or defName=\"SG_Manufacturing\"]/prerequisites/li";

    public static void parseAll()
    {
        parse(inA);
        parse(inB);
        parse(inC);
    }

    public static XPathNode parse(String in)
    {
        //remove leading /
        if (in.startsWith("/")) in = in.substring(1);

        //create root node
        boolean isRoot = true;
        XPathNode root = new XPathNode();
        XPathNode current = root;

        //split into parts
        String[] parts = in.split("/");
        for (String part : parts)
        {
            if (!isRoot)
            {
                XPathNode newNode = new XPathNode();
                current.child = newNode;
                current = newNode;
            }

            if (part.contains("[") && part.contains("]"))
            {
                //node has a predicate
                String predicate = part.substring(part.indexOf("[") + 1, part.indexOf("]"));

                String[] orSplit = predicate.split(" or ");
                for (String split : orSplit)
                {
                    if (split.contains("="))
                    {
                        String type = split.substring(0, split.indexOf("=")).trim();
                        String val = split.substring(split.indexOf("=") + 1).trim();
                        if (val.startsWith("\"") && val.endsWith("\""))
                        {
                            current.predicates.add(new XPathPredicate(type, val.substring(1, val.length() - 1)));
                        }
                    }
                }

                current.value = part.substring(0, part.indexOf("["));
            }
            else
            {
                current.value = part;
            }
            isRoot = false;
        }
        return root;
    }
}
