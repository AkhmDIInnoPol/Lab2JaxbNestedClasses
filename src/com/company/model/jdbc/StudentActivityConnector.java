package com.company.model.jdbc;

import com.company.model.xjc.Lesson;
import com.company.model.xjc.StudentActivities;
import com.company.model.xjc.StudentActivity;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.math.BigInteger;
import java.sql.*;
import java.util.List;

/**
 * Connector to data base for {@link com.company.model.xjc.StudentActivity} object.
 */
public class StudentActivityConnector
{
    static {
        PropertyConfigurator.configure("./src/com/company/logger/log4j.properties");
    }
    private static final Logger logger = Logger.getLogger(StudentActivity.class);



    private Connection connection;


    public StudentActivityConnector(Connection connection) {
        this.connection = connection;
    }




    /**
     * Get all tuple from {@link StudentActivity} table.
     * @return list of all {@link StudentActivity} objects that contain in table.
     */
    public StudentActivities selectAll()
    {

        StudentActivities studentActivities = new StudentActivities();

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from public.student_activity");


            while (result.next())
            {
                StudentActivity studentActivity = new StudentActivity();

                studentActivity.setId(BigInteger.valueOf(result.getInt("id")));
                studentActivity.setStudentId(BigInteger.valueOf(result.getInt("student_id")));
                int lessonId = result.getInt("lesson_id");
                studentActivity.setLesson(getLessonById(lessonId));
                studentActivity.setActivityGrade(BigInteger.valueOf(result.getInt("activity_grade")));
                studentActivity.setActivityComment(result.getString("activity_comment"));
                studentActivity.setIsDeleted(result.getBoolean("is_deleted"));



                studentActivities.getStudentActivities().add(studentActivity);
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex.getMessage());
        }

        return studentActivities;
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
     * Function insert new tuple of {@link StudentActivity} object in table.
     * @param studentActivities - list of objects of type {@link StudentActivity}.
     */
    public void insert(StudentActivities studentActivities)
    {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO student_activity(id, student_id, lesson_id, " +
                            "activity_grade, activity_comment, is_deleted) \n"
                            + "VALUES (?,?,?,?,?,?)"
            );

            List<StudentActivity> studentActivityList = studentActivities
                                                            .getStudentActivities();

            for (StudentActivity studentActivity:studentActivityList
                    ) {
                preparedStatement.setInt(1, studentActivity.getId().intValue());
                preparedStatement.setInt(2, studentActivity.getStudentId().intValue());
                int lessonId = studentActivity.getLesson().getId().intValue();
                insertLesson(studentActivity.getLesson(), lessonId);
                preparedStatement.setInt(3, studentActivity.getLesson().getId().intValue());
                preparedStatement.setInt(4, studentActivity.getActivityGrade().intValue());
                preparedStatement.setString(5, studentActivity.getActivityComment());
                preparedStatement.setBoolean(6, studentActivity.isIsDeleted());

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
     * Delete table {@link StudentActivity} in data base.
     */
    public void delete()
    {
        try {
            Statement statement = connection.createStatement();

            String query = "DELETE FROM student_activity ";
            statement.executeUpdate(query);
        }
        catch (SQLException ex)
        {
            logger.error(ex);
        }

    }




}
