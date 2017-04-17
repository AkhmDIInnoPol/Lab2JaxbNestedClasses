package com.company.model.jdbc;

import com.company.model.xjc.Lesson;
import com.company.model.xjc.Lessons;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;

/**
 * Connector to data base for {@link com.company.model.xjc.Lesson} object.
 * It only delete {@link Lesson} tables.
 */
public class LessonConnector
{


    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(LessonConnector.class);


    private Connection connection;

    public LessonConnector(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get specified tuple by ID from {@link com.company.model.xjc.Lesson} table.
     * @return list of all {@link com.company.model.xjc.Lesson} objects that contain in table.
     */
    public Lesson selectByID(int lessonId)
    {

        Lesson lesson = new Lesson();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from public.lesson " +
                                                    "WHERE id = " + lessonId);


                result.next();


                lesson.setId(BigInteger.valueOf(result.getInt("id")));
                lesson.setStudyGroupId(BigInteger.valueOf(result.getInt("study_group_id")));
                lesson.setLessonDate(GregXMLAndTimestampSQLConverter
                                        .convTimestampSqlToGregXml(result.getTimestamp("lesson_date")));
                lesson.setRoom(BigInteger.valueOf(result.getInt("room")));
                lesson.setTopic(result.getString("topic"));
                lesson.setDescription(result.getString("description"));
                lesson.setLessonComment(result.getString("lesson_comment"));
                lesson.setToken(result.getString("token"));
                lesson.setTokenExpiration(GregXMLAndTimestampSQLConverter
                        .convTimestampSqlToGregXml(result.getTimestamp("token_expiration")));
                lesson.setIsDeleted(result.getBoolean("is_deleted"));



        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }

        return lesson;
    }


    /**
     * Function insert new tuple of {@link Lesson} object in table.
     * @param lesson - object of type {@link Lesson}.
     * @param lessonId - id of {@link Lesson} object.
     */
    public void insert(Lesson lesson, int lessonId)
    {

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from public.lesson " +
                    "WHERE id = " + lessonId);
            boolean isContainRow = result.next();

            if (!isContainRow)
            {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO lesson(id, study_group_id, lesson_date, " +
                                "room, topic, description, lesson_comment, token, token_expiration, is_deleted) \n"
                                + "VALUES (?,?,?,?,?,?,?,?,?,?)"

                );




                preparedStatement.setInt(1, lesson.getId().intValue());
                preparedStatement.setInt(2, lesson.getStudyGroupId().intValue());
                preparedStatement.setTimestamp(3, GregXMLAndTimestampSQLConverter
                        .convGregXmlToTimestampSql(lesson.getLessonDate()));
                preparedStatement.setInt(4, lesson.getRoom().intValue());
                preparedStatement.setString(5, lesson.getTopic());
                preparedStatement.setString(6, lesson.getDescription());
                preparedStatement.setString(7, lesson.getLessonComment());
                preparedStatement.setString(8, lesson.getToken());
                preparedStatement.setTimestamp(9, GregXMLAndTimestampSQLConverter
                        .convGregXmlToTimestampSql(lesson.getTokenExpiration()));
                preparedStatement.setBoolean(10, lesson.isIsDeleted());

                preparedStatement.executeUpdate();
            }

        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }
    }


    /**
     * Delete table {@link Lesson} in data base.
     */
    public void delete()
    {
        try {
            Statement statement = connection.createStatement();

            String query = "DELETE FROM lesson ";
            statement.executeUpdate(query);
        }
        catch (SQLException ex)
        {
            logger.error(ex);
        }

    }



}
