package com.company.model.jdbc;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * Converter between {@link XMLGregorianCalendar} and {@link Timestamp}.
 */
public class GregXMLAndTimestampSQLConverter
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(GregXMLAndTimestampSQLConverter.class);





    /**
     * Function convert {@link XMLGregorianCalendar} to {@link  Timestamp}
     * @param gregXML - date and time in {@link XMLGregorianCalendar} format.
     * @return - time and date in {@link  Timestamp} format.
     */
    public static Timestamp convGregXmlToTimestampSql(XMLGregorianCalendar gregXML)
    {
        long time = gregXML.toGregorianCalendar().getTime().getTime();
        return new Timestamp(time);
    }

    /**
     * Function convert {@link  Timestamp} to {@link XMLGregorianCalendar}.
     * @param timestamp - date and time in {@link  Timestamp}.
     * @return time and date in {@link XMLGregorianCalendar}.
     */
    public static XMLGregorianCalendar convTimestampSqlToGregXml(Timestamp timestamp)
    {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(timestamp);

        XMLGregorianCalendar gregXml = null;

        try {
            gregXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        }
        catch (DatatypeConfigurationException ex)
        {
            logger.error(ex.getMessage());
        }

        return gregXml;
    }









}
