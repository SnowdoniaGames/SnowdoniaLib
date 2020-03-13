package com.snowdonia.lib.file.xml;

import com.snowdonia.lib.file.xml.lib.XmlAttribute;
import com.snowdonia.lib.file.xml.lib.XmlElement;
import com.snowdonia.lib.util.StringUtil;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler
{
    public XmlElement root = null;
    public XmlElement current = null;

    public XmlElement getRoot()
    {
        return root;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        XmlElement element = new XmlElement();
        element.name = qName;

        for (int i = 0; i < attributes.getLength(); i++)
        {
            element.attributes.add(new XmlAttribute(attributes.getQName(i), attributes.getValue(i)));
        }

        if (root == null) root = element;

        if (current != null)
        {
            element.parent = current;
            current.children.add(element);
        }
        current = element;

        //System.out.println("Start -> " + qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if (current != null)
        {
            current = current.parent;
        }

        //System.out.println("\t\tEnd -> " + qName);
    }

    @Override
    public void characters(char ch[], int start, int length)
    {
        String out = new String(ch, start, length);
        if (!out.trim().isEmpty())
        {
            if (current != null)
            {
                if (StringUtil.exists(current.value))
                {
                    current.value += out;
                }
                else
                {
                    current.value = out;
                }
            }

            //System.out.println("\tOut -> " + out);
        }
    }
}