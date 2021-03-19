package com.sunshine.database;

import com.sunshine.model.Reminder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
public class MySqlConnect {

    private static final Logger LOG = LogManager.getLogger(MySqlConnect.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public Connection openConnection() {

            LOG.debug("opening connection");

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

        return connection;
    }

    public void closeConnection() {

        LOG.debug("closing connection");

        try {

            if (resultSet != null){
                resultSet.close();
            }

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

    public void createReminder(Reminder reminder){

        LOG.debug("in createReminder");

        try{

            preparedStatement = this.openConnection().prepareStatement("INSERT INTO reminder (id, " +
                    "userID," +
                    "reminderTime) VALUES(?, ?, ?)");
            preparedStatement.setString(1, reminder.getReminderId());
            preparedStatement.setString(2, reminder.getUserId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reminder.getReminderTime()));
            LOG.debug("Creating in database - connection closed: {}",connection.isClosed() );
            preparedStatement.execute();

        } catch (SQLException exception){

            LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
        }

    }

    public ResultSet readReminder(String UserId, String ReminderId){

            LOG.debug("in readReminder");

            try {

                preparedStatement = this.openConnection().prepareStatement("SELECT * FROM " +
                        "reminder " +
                        "WHERE id = ? AND userID = ?");
                preparedStatement.setString(1, ReminderId);
                preparedStatement.setString(2, UserId);
                LOG.debug("Reading database - connection closed: {}",connection.isClosed() );
                resultSet = preparedStatement.executeQuery();

            } catch (SQLException exception) {

                LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
            }

        return resultSet;

    }

    public int updateReminder (Reminder reminder){

        LOG.debug("in updateReminder");
        int updated = 0;

        try{

            preparedStatement = this.openConnection().prepareStatement("UPDATE reminder SET " +
                    "reminderTime = ? WHERE id = " +
                    "? AND userID = ?");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(reminder.getReminderTime()));
            preparedStatement.setString(2, reminder.getReminderId());
            preparedStatement.setString(3, reminder.getUserId());
            LOG.debug("Updating database - connection closed: {}",connection.isClosed() );
            updated = preparedStatement.executeUpdate();

        } catch (SQLException exception){

            LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
        }

        return updated;

    }

    public void deleteReminder (String UserId, String ReminderId){

        LOG.debug("in deleteReminder");

        try {

            preparedStatement = this.openConnection().prepareStatement("DELETE FROM reminder " +
                    "WHERE id = ? AND userID = ?");
            preparedStatement.setString(1, ReminderId);
            preparedStatement.setString(2, UserId);
            LOG.debug("Deleting from database - connection closed: {}",connection.isClosed() );
            preparedStatement.execute();

        } catch (SQLException exception){

            LOG.error(String.format("SQL exception: %s", exception.getMessage()), exception);
        }

    }

}