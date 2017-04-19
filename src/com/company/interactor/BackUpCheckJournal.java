package com.company.interactor;

import com.company.model.jdbc.ConnectionDB;
import com.company.model.jdbc.JournalConnector;
import com.company.model.xjc.Journal;
import com.company.model.xjc.Journals;
import com.company.model.xml.JavaObjectXmlConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;


/**
 * Class backup {@link Journal} table in xml file.
 */
public class BackUpCheckJournal extends Thread
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(BackUpCheckJournal.class);







    private static final String journalsXmlFileName = "./xml/journal";

    private JournalConnector journalConnector;

    private ConnectionDB connectionDB;









    @Override
    public void run() {



        buildJournalConnection();

        saveData();

        deleteTables();

        startAndJoinOtherThread();

        backupTables();





        connectionDB.closeConnection();
    }


    /**
     * Start other thread and wait until it ended. Wait until other thread table deleted.
     */
    public void startAndJoinOtherThread()
    {
        BackUpCheckStudentActivity backUpCheckStudentActivityThread = new BackUpCheckStudentActivity();
        backUpCheckStudentActivityThread.start();
        try {
            backUpCheckStudentActivityThread.join();
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.getMessage());
        }

    }




    /**
     * Function build connection with data base
     * for {@link JournalConnector}.
     */
    private void buildJournalConnection()
    {
        connectionDB = new ConnectionDB();
        connectionDB.initConnection();
        Connection connection =  connectionDB.getConnection();

        journalConnector = new JournalConnector(connection);
    }



    /**
     * Function save data of {@link Journal} table in xml files.
     */
    private  void saveData()
    {
        Journals journals = journalConnector.selectAll();
        JavaObjectXmlConverter.writeToXML(journals, Journals.class, journalsXmlFileName);
    }


    /**
     * Delete {@link Journal} table from data base.
     */
    private void deleteTables()
    {
        journalConnector.delete();
    }


    /**
     * Function backup {@link Journal} table from xml to data base data.
     */
    private  void backupTables()
    {
        Journals journals = JavaObjectXmlConverter.readFromXML(Journals.class, journalsXmlFileName);
        journalConnector.insert(journals);
    }


    /**
     * Log current state of some tables in data base.
     */
    public void logDataInDB()
    {
        Journals journals = journalConnector.selectAll();
        for (Journal journal:journals.getJournals()
             ) {
            logger.info(journal.toString());
        }
    }



}
