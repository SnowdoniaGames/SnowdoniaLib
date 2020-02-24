package com.snowdonia.lib.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JaxbHandler
{
	public static Object open(Class clazz, File file)
	{
		try
		{
			//try  to load file
			if (!file.exists())
			{
				//if one doesn't exist, create a new instance
				return clazz.newInstance();
			}

			//create instance
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

			//create unmarshaller
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(file);
		}
		catch (JAXBException | IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void save(Object data, File file)
	{
		try
		{
			//create instance
			JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());

			//create marshaller
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//write xml
			marshaller.marshal(data, file);
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}
}
