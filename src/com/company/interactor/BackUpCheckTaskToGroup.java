package com.company.interactor;

import com.company.model.jdbc.ConnectionDB;
import com.company.model.jdbc.TaskToGroupConnector;
import com.company.model.xjc.TaskToGroup;
import com.company.model.xjc.TaskToGroups;
import com.company.model.xml.JavaObjectXmlConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;

/**
 * Class backup {@link com.company.model.xjc.TaskToGroup} table in xml file.
 */
public class BackUpCheckTaskToGroup extends Thread
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(BackUpCheckJournal.class);





    private static final String taskToGroupXmlFileName = "./xml/taskToGroup";

    private TaskToGroupConnector taskToGroupConnector;

    private ConnectionDB connectionDB;



    @Override
    public void run() {



        buildTaskToGroupConnection();







        saveData();

        deleteTables();

        backupTables();




        connectionDB.closeConnection();
    }




    /**
     * Function build connection with data base
     * for {@link com.company.model.jdbc.TaskToGroupConnector}.
     */
    private void buildTaskToGroupConnection()
    {
        connectionDB = new ConnectionDB();
        connectionDB.initConnection();
        Connection connection =  connectionDB.getConnection();

        taskToGroupConnector = new TaskToGroupConnector(connection);
    }



    /**
     * Function save data of {@link TaskToGroup} table in xml files.
     */
    private  void saveData()
    {
        TaskToGroups taskToGroups = taskToGroupConnector.selectAll();
        JavaObjectXmlConverter.writeToXML(taskToGroups, TaskToGroups.class, taskToGroupXmlFileName);
    }


    /**
     * Delete {@link TaskToGroup} table from data base.
     */
    private void deleteTables()
    {
        taskToGroupConnector.delete();
    }


    /**
     * Function backup {@link TaskToGroup} table from xml to data base data.
     */
    private  void backupTables()
    {
        TaskToGroups taskToGroups = JavaObjectXmlConverter
                .readFromXML(TaskToGroups.class, taskToGroupXmlFileName);
        taskToGroupConnector.insert(taskToGroups);
    }


    /**
     * Log current state of some tables in data base.
     */
    public void logDataInDB()
    {

        TaskToGroups taskToGroups = taskToGroupConnector.selectAll();
        for (TaskToGroup taskToGroup:taskToGroups.getTaskToGroups()
                ) {
            logger.info(taskToGroup.toString());
        }
    }





}
