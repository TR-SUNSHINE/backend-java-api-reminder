package com.sunshine;

import java.sql.*;


import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public class GetReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(GetReminderHandler.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context){

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");

        List<Reminder> reminders = new ArrayList<>();
        Reminder reminder;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            LOG.debug(String.format("Connecting to DB on %s", System.getenv("DB_HOST")));

            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s" +
                    "&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));

            preparedStatement = connection.prepareStatement("SELECT * FROM reminder WHERE id = ?");
            preparedStatement.setString(1, ReminderId);
            resultSet = preparedStatement.executeQuery();



            while (resultSet.next()){

                reminder = new Reminder(    resultSet.getString("id"),
                                            resultSet.getString("userID"),
                                            resultSet.getTimestamp("reminderTime").toLocalDateTime());

                reminders.add(reminder);
            }

        } catch (Exception exception){
            LOG.error(String.format("Unable to query database for reminder %s for user %s",
                    ReminderId,
                    UserId),
             exception);
        } finally {
            closeConnection();
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String responseBody = objectMapper.writeValueAsString(reminders);
            response.setBody(responseBody);

        } catch (JsonProcessingException exception){
            LOG.error("unable to marshal tasks array", exception);
        }

        return response;

    }

    public void closeConnection(){

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
        } catch (SQLException exception){
            LOG.error("Unable to close connection to MySQL - {}", exception.getMessage());
        }
    }

}
