package com.sunshine.database;

import com.sunshine.model.Reminder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
public class MySqlConnect {

    private static final Logger LOG = LogManager.getLogger(MySqlConnect.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    public Connection openConnection() {
        if (connection == null) {

            try {
                Class.forName("com.mysql.jdbc.Driver");

                LOG.debug(String.format("Connecting to DB on %s", System.getenv("DB_HOST")));

                connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s" +
                                "&password=%s&useSSL=false",
                        System.getenv("DB_HOST"),
                        System.getenv("DB_NAME"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD")));

            } catch (ClassNotFoundException exception) {

                LOG.error(String.format("ClassNotFoundException: %s", exception.getMessage()), exception);

            } catch (SQLException exception) {

                LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
            }
        }

        return connection;
    }

    public void closeConnection() {

        try {

//            if (resultSet != null){
//                resultSet.close();
//            }
//
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            LOG.error("Unable to close connection to MySQL - {}", exception.getMessage());
        }
    }

    public void insertReminder(Reminder reminder){

        try{

            LOG.debug("Inserting reminder");

            preparedStatement = connection.prepareStatement("INSERT INTO reminder (id, " +
                    "userID," +
                    "reminderTime) VALUES(?, ?, ?)");
            preparedStatement.setString(1, reminder.getReminderId());
            preparedStatement.setString(2, reminder.getUserId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reminder.getReminderTime()));
            preparedStatement.execute();

        } catch (SQLException exception){

            LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
            // response.setStatusCode(500);
        }

    }

}