package com.snowdonia.lib.file.xml.xpath;

import java.util.ArrayList;
import java.util.List;

public class XPathNode
{
    public String value;
    public XPathNode child;
    public List<XPathPredicate> predicates = new ArrayList<>();
}
