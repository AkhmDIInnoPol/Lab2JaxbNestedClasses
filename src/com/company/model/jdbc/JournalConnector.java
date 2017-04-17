package com.company.model.jdbc;

import com.company.model.xjc.Journal;
import com.company.model.xjc.Journals;
import com.company.model.xjc.Lesson;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;

/**
 * Connector to data base for {@link Journal} object.
 */
public class JournalConnector
{


    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(JournalConnector.class);


    private Connection connection;

    public JournalConnector(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get all tuple from {@link Journal} table
     * @return list of all {@link Journal} objects that contain in table.
     */
    public Journals selectAll()
    {

        Journals journals = new Journals();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from public.journal");


            while (result.next())
            {
                Journal journal = new Journal();

                journal.setId(BigInteger.valueOf(result.getInt("id")));
                int lessonId = result.getInt("lesson_id");
                journal.setLesson(getLessonById(lessonId));
                journal.setStudentId(BigInteger.valueOf(result.getInt("student_id")));

                journal.setTimeCheck(GregXMLAndTimestampSQLConverter
                        .convTimestampSqlToGregXml(  result.getTimestamp("time_check")));

                journal.setIsDeleted(result.getBoolean("is_deleted"));

                journals.getJournals().add(journal);
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }

        return journals;
    }


    /**
     * Get object {@link Lesson} by ID.
     * @param lessonId - id of {@link Lesson} tht need to acquire.
     * @return - {@link Lesson} object with specified Id.
     */
    private Lesson getLessonById(int lessonId)
    {
        ConnectionDB connectionDB = new ConnectionDB();
        connectionDB.initConnection();
        Lesson lesson = new LessonConnector(connectionDB
                                    .getConnection()).selectByID(lessonId);

        return lesson;
    }




    /**
     * Function insert new tuple of {@link Journal} object in table.
     * @param journals - list of objects of type {@link Journal}.
     */
    public void insert(Journals journals)
    {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO journal(id, lesson_id, student_id, time_check, is_deleted) \n"
                    + "VALUES (?,?,?,?,?)"
            );

            List<Journal> journalList = journals.getJournals();

            for (Journal journal:journalList
                 ) {
                preparedStatement.setInt(1, journal.getId().intValue());
                int lessonId = journal.getLesson().getId().intValue();
                insertLesson(journal.getLesson(), lessonId);
                preparedStatement.setInt(2, journal.getLesson().getId().intValue());
                preparedStatement.setInt(3, journal.getStudentId().intValue());
                preparedStatement.setTimestamp(4, GregXMLAndTimestampSQLConverter
                        .convGregXmlToTimestampSql(journal.getTimeCheck()));
                preparedStatement.setBoolean(5, journal.isIsDeleted());

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }
    }


    /**
     * Insert this {@link Lesson} object in data base.
     * @param lesson - {@link Lesson} object to insert.
     * @param lessonId - id need for data base. Program don't insert
     *                 {@link Lesson} object if data base already contained
     *                 object with this id.
     */
    private void insertLesson(Lesson lesson, int lessonId)
    {
        LessonConnector lessonConnector = new LessonConnector(connection);
        lessonConnector.insert(lesson, lessonId);
    }


    /**
     * Delete table {@link Journal} in data base.
     */
    public void delete()
    {
        try {
            Statement statement = connection.createStatement();

            String query = "DELETE FROM journal";
            statement.executeUpdate(query);
        }
        catch (SQLException ex)
        {
            logger.error(ex);
        }

    }



}
