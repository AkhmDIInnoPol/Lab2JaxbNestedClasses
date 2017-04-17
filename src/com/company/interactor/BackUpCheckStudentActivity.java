package com.company.interactor;

import com.company.model.jdbc.ConnectionDB;
import com.company.model.jdbc.StudentActivityConnector;
import com.company.model.xjc.StudentActivities;
import com.company.model.xjc.StudentActivity;
import com.company.model.xml.JavaObjectXmlConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;

/**
 * Class backup {@link com.company.model.xjc.StudentActivity} table in xml file.
 */
public class BackUpCheckStudentActivity extends Thread
{



    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(BackUpCheckJournal.class);







    private static final String studentActivitiesXmlFileName = "./xml/studentActivity";

    private StudentActivityConnector studentActivityConnector;

    private ConnectionDB connectionDB;

    @Override
    public void run() {



        buildStudentActivityConnection();






        saveData();

        deleteTables();

        startAndJoinOtherThread();

        backupTables();





        connectionDB.closeConnection();
    }


    /**
     * Start other thread and wait until it ended.
     * Wait until parent table thread ended their work.
     */
    public void startAndJoinOtherThread()
    {
        BackUpCheckLesson backUpCheckLessonThread = new BackUpCheckLesson();
        backUpCheckLessonThread.start();

        try {
            backUpCheckLessonThread.join();
        }
        catch (InterruptedException ex)
        {
            logger.error(ex.getMessage());
        }
    }


    /**
     * Function build connection with data base
     * for {@link com.company.model.jdbc.StudentActivityConnector}.
     */
    private void buildStudentActivityConnection()
    {
        connectionDB = new ConnectionDB();
        connectionDB.initConnection();
        Connection connection =  connectionDB.getConnection();

        studentActivityConnector = new StudentActivityConnector(connection);
    }



    /**
     * Function save data of {@link StudentActivity} table in xml files.
     */
    private  void saveData()
    {
        StudentActivities studentActivities = studentActivityConnector.selectAll();
        JavaObjectXmlConverter.writeToXML(studentActivities, StudentActivities.class,
                studentActivitiesXmlFileName);
    }


    /**
     * Delete {@link StudentActivity} table from data base.
     */
    private void deleteTables()
    {
        studentActivityConnector.delete();
    }


    /**
     * Function backup {@link StudentActivity} table from xml to data base data.
     */
    private  void backupTables()
    {
        StudentActivities studentActivities = JavaObjectXmlConverter.readFromXML(StudentActivities.class,
                                                                                studentActivitiesXmlFileName);
        studentActivityConnector.insert(studentActivities);
    }


    /**
     * Log current state of some tables in data base.
     */
    public void logDataInDB()
    {
        StudentActivities studentActivities =  studentActivityConnector.selectAll();
        for (StudentActivity studentActivity:studentActivities.getStudentActivities()
                ) {
            logger.info(studentActivity.toString());
        }

    }



}
