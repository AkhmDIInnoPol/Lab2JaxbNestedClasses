package com.company.interactor;

import com.company.model.jdbc.ConnectionDB;
import com.company.model.jdbc.MarkConnector;
import com.company.model.xjc.Mark;
import com.company.model.xjc.Marks;
import com.company.model.xml.JavaObjectXmlConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;

/**
 * Class backup {@link com.company.model.xjc.Mark} table in xml file.
 */
public class BackUpCheckMark extends Thread
{



    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(BackUpCheckJournal.class);







    private static final String marksXmlFileName = "./xml/mark";

    private MarkConnector markConnector;

    private ConnectionDB connectionDB;




    @Override
    public void run() {


        buildMarkConnection();






        saveData();

        deleteTables();

        backupTables();





        connectionDB.closeConnection();
    }



    /**
     * Function build connection with data base
     * for {@link com.company.model.jdbc.MarkConnector}.
     */
    private void buildMarkConnection()
    {
        connectionDB = new ConnectionDB();
        connectionDB.initConnection();
        Connection connection =  connectionDB.getConnection();

        markConnector = new MarkConnector(connection);
    }



    /**
     * Function save data of {@link Mark} table in xml files.
     */
    private  void saveData()
    {
        Marks marks = markConnector.selectAll();
        JavaObjectXmlConverter.writeToXML(marks, Marks.class, marksXmlFileName);
    }


    /**
     * Delete {@link Mark} table from data base.
     */
    private void deleteTables()
    {
        markConnector.delete();
    }


    /**
     * Function backup {@link Mark} table from xml to data base data.
     */
    private  void backupTables()
    {
        Marks marks = JavaObjectXmlConverter.readFromXML(Marks.class, marksXmlFileName);
        markConnector.insert(marks);
    }


    /**
     * Log current state of some tables in data base.
     */
    public void logDataInDB()
    {
        Marks marks =  markConnector.selectAll();
        for (Mark mark:marks.getMarks()
                ) {
            logger.info(mark.toString());
        }

    }
}
