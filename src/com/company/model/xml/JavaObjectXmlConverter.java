package com.company.model.xml;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 *  Class marshalling (to xml) and unmarshalling (from xml)
 *  java objects
 */
public class JavaObjectXmlConverter
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(JavaObjectXmlConverter.class);


    /**
     * Function marshalling list of objects in specified xml file.
     * @param objects - list of objects of some type.
     * @param xmlFileName - name of xml file where object
     * @param <T> - type of java object.
     */
    public static <T> void writeToXML(T objects, Class<?> classObj, String xmlFileName)
    {
        File file = new File(xmlFileName + ".xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classObj);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(objects, file);

        }
        catch (JAXBException ex)
        {
            logger.error(ex.getMessage());
        }
    }







    public static <T> T readFromXML(Class<?> classObj, String xmlFileName)
    {
        File file = new File(xmlFileName + ".xml");

        T objects = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classObj);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


            objects = (T) jaxbUnmarshaller.unmarshal(file);


        }
        catch (JAXBException ex)
        {
            logger.error(ex.getMessage());
        }

        return objects;

    }








}
