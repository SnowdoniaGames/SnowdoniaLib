package com.snowdonia.lib.file.xml.lib;

import com.snowdonia.lib.file.xml.xpath.XPathNode;
import com.snowdonia.lib.file.xml.xpath.XPathPredicate;

import java.util.ArrayList;
import java.util.List;

public class XmlElement
{
    public XmlElement parent;
    public List<XmlElement> children = new ArrayList<>();
    public List<XmlAttribute> attributes = new ArrayList<>();
    public String name;
    public String value;

    public XmlElement() { }

    public XmlElement(XmlElement parent, List<XmlElement> children, List<XmlAttribute> attributes, String name, String value)
    {
        this.parent = parent;
        this.children = children;
        this.attributes = attributes;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public List<XmlElement> getChildren(String name)
    {
        List<XmlElement> childList = new ArrayList<>();
        for (XmlElement child : children)
        {
            if (child.name.equals(name))
            {
                childList.add(child);
            }
        }
        return childList;
    }

    public String getAttribute(String name)
    {
        for (XmlAttribute attr : attributes)
        {
            if (attr.name.equals(name))
            {
                return attr.value;
            }
        }
        return null;
    }

    public void copyFrom(XmlElement element)
    {
        if (element == null) return;

        for (XmlElement parentChild : element.children)
        {
            XmlElement child = doesChildExist(parentChild);
            if (child == null)
            {
                //create a new copy of the child and change the parent
                XmlElement newElement = new XmlElement(this, parentChild.children, parentChild.attributes, parentChild.name, parentChild.value);
                children.add(newElement);
            }
            else
            {
                child.copyFrom(parentChild);
            }
        }
    }

    public XmlElement doesChildExist(XmlElement parentChild)
    {
        for (XmlElement child : this.children)
        {
            if (child.name.equals(parentChild.name))
            {
                return child;
            }
        }
        return null;
    }

    public List<XmlElement> getXPath(XPathNode node)
    {
        List<XmlElement> matches = new ArrayList<>();
        getXPath(matches, node);
        return matches;
    }

    public void getXPath(List<XmlElement> out, XPathNode node)
    {
        //first check name
        if (this.name == null || !this.name.equals(node.value)) return;

        //next check predicates match if they exist
        if (!node.predicates.isEmpty())
        {
            boolean match = false;
            for (XPathPredicate predicate : node.predicates)
            {
                List<XmlElement> children = getChildren(predicate.type);
                for (XmlElement child : children)
                {
                    if (child.value != null && child.value.equals(predicate.value))
                    {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) return;
        }

        if (node.child == null)
        {
            out.add(this);
        }
        else
        {
            for (XmlElement child : this.children)
            {
                child.getXPath(out, node.child);
            }
        }
    }
}