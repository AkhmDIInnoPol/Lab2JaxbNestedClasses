package com.company.test.model.jdbc;


import com.company.model.jdbc.GregXMLAndTimestampSQLConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Di on 15.04.2017.
 */
class GregXMLAndTimestampSQLConverterTest
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(GregXMLAndTimestampSQLConverterTest.class);




    @Test
    void convGregXmlToDateSqlTest() {

        int year = 2017;
        int month = 4;
        int dayOfMonth = 21;
        int hourOfDay = 8;
        int minute = 7;
        int second = 6;



        GregorianCalendar calendar = new GregorianCalendar(year, month,
                                            dayOfMonth,hourOfDay,minute, second);
        try {
            XMLGregorianCalendar xmlGregorianCalendar=
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

            Timestamp dateTime = GregXMLAndTimestampSQLConverter.convGregXmlToTimestampSql(xmlGregorianCalendar);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime);

            assertTrue(year == cal.get(Calendar.YEAR));
            assertTrue(month == cal.get(Calendar.MONTH));
            assertTrue(dayOfMonth == cal.get(Calendar.DAY_OF_MONTH));
            assertTrue(hourOfDay == cal.get(Calendar.HOUR));
            assertTrue(minute == cal.get(Calendar.MINUTE));
            assertTrue(second == cal.get(Calendar.SECOND));
        }
        catch (DatatypeConfigurationException ex)
        {
            logger.error(ex.getMessage());
        }
    }








    @org.junit.jupiter.api.Test
    void convDateSqlToGregXmlTest() {

        int year = 2017;
        int month = 4;
        int dayOfMonth = 21;
        int hourOfDay = 8;
        int minute = 7;
        int second = 6;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, dayOfMonth, hourOfDay, minute, second);
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());

        XMLGregorianCalendar xmlGregorianCalendar =
                GregXMLAndTimestampSQLConverter.convTimestampSqlToGregXml(timestamp);

        System.out.println(xmlGregorianCalendar.getMonth());

        assertTrue(xmlGregorianCalendar.getYear() == year);
        assertTrue(xmlGregorianCalendar.getMonth() == month);
        assertTrue(xmlGregorianCalendar.getDay() == dayOfMonth);
        assertTrue(xmlGregorianCalendar.getHour() == hourOfDay);
        assertTrue(xmlGregorianCalendar.getMinute() == minute);
        assertTrue(xmlGregorianCalendar.getSecond() == second);




    }

}