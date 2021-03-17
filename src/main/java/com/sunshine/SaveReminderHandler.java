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

import java.io.IOException;
import java.util.*;

public class SaveReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(SaveReminderHandler.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context){

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String requestBody = request.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(201);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        try {

            Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);

            Class.forName("com.mysql.jdbc.Driver");

            LOG.debug(String.format("Connecting to DB on %s", System.getenv("DB_HOST")));

            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s" +
                            "&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));

            preparedStatement = connection.prepareStatement("INSERT INTO reminder (id, userID," +
                    "reminderTime) VALUES = ?, ?, " +
                    "?");
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, UserId);
//            preparedStatement.setTimestamp(3, Timestamp.valueOf(reminder.getReminderTime()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reminder.getReminderTime()));
            preparedStatement.execute();

            connection.close();

        } catch (IOException exception){
            LOG.error(String.format("Unable to unmarshall JSON for adding a reminder %s",
                    exception.getMessage()));

            // what status code for unmarshalling??

        } catch (ClassNotFoundException exception){
            LOG.error("ClassNotFoundException", exception);
        } catch (SQLException exception){
            LOG.error("SQL exception", exception);
        }
        finally {
            closeConnection();
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
