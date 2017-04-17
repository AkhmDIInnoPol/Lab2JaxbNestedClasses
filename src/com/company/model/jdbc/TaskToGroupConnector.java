package com.company.model.jdbc;


import com.company.model.xjc.TaskToGroup;
import com.company.model.xjc.TaskToGroups;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;

/**
 * Connector to data base for {@link com.company.model.xjc.TaskToGroup} object.
 */
public class TaskToGroupConnector
{

    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(JournalConnector.class);



    Connection connection;


    public TaskToGroupConnector(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get all tuple from {@link TaskToGroup} table.
     * @return list of all taskToGroup objects that contain in table.
     */
    public TaskToGroups selectAll()
    {

        TaskToGroups taskToGroups = new TaskToGroups();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from public.task_to_group");


            while (result.next())
            {
                TaskToGroup taskToGroup = new TaskToGroup();

                taskToGroup.setId(BigInteger.valueOf(result.getInt("id")));
                taskToGroup.setTaskId(BigInteger.valueOf(result.getInt("task_id")));
                taskToGroup.setStudyGroupId(BigInteger.valueOf(result.getInt("study_group_id")));
                taskToGroup.setIsActive(result.getBoolean("is_active"));
                taskToGroup.setEndDate(GregXMLAndTimestampSQLConverter
                        .convTimestampSqlToGregXml(result.getTimestamp("end_date")));

                taskToGroups.getTaskToGroups().add(taskToGroup);
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }

        return taskToGroups;
    }


    /**
     * Function insert new tuple of {@link TaskToGroup} object in table.
     * @param taskToGroups - object of {@link TaskToGroup} type.
     */
    public void insert(TaskToGroups taskToGroups)
    {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO task_to_group(id, task_id, study_group_id, is_active, end_date) \n"
                            + "VALUES (?,?,?,?,?)"
            );

            List<TaskToGroup> taskToGroupList = taskToGroups.getTaskToGroups();

            for (TaskToGroup taskToGroup:taskToGroupList
                 ) {
                preparedStatement.setInt(1, taskToGroup.getId().intValue());
                preparedStatement.setInt(2, taskToGroup.getTaskId().intValue());
                preparedStatement.setInt(3, taskToGroup.getStudyGroupId().intValue());
                preparedStatement.setBoolean(4, taskToGroup.isIsActive());
                preparedStatement.setTimestamp(5,
                        GregXMLAndTimestampSQLConverter.convGregXmlToTimestampSql(taskToGroup.getEndDate()));

                preparedStatement.executeUpdate();
            }


        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }
    }



    /**
     * Delete table {@link TaskToGroup} in data base.
     */
    public void delete()
    {
        try {
            Statement statement = connection.createStatement();

            String query = "DELETE FROM task_to_group ";
            statement.executeUpdate(query);
        }
        catch (SQLException ex)
        {
            logger.error(ex);
        }

    }


}
