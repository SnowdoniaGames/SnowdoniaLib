package com.snowdonia.lib.file.xml;

import com.snowdonia.lib.file.xml.lib.XmlElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XmlGenerator
{
    public static XmlElement generate(File f)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            XmlHandler handler = new XmlHandler();
            saxParser.parse(f.getPath(), handler);

            XmlElement root = handler.getRoot();
            return root;
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            return null;
        }
    }
}
