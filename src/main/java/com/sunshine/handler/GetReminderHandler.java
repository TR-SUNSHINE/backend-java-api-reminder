package com.sunshine.handler;

import java.sql.*;
import com.sunshine.database.MySqlConnect;

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

    MySqlConnect mySqlConnect = new MySqlConnect();

    private PreparedStatement preparedStatement = null;
     private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context){

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        List<Reminder> reminders = new ArrayList<>();
        Reminder reminder;

        try {
            // create database object  - done
            // open connection on database - done
            // get data pass in path parameter
            // call database object to get data with parameter as reminder object
            // close connection to database object - done
            // getReminder - build collection - array list

//            Connection connection = mySqlConnect.openConnection();

//            if (connection != null) {

                LOG.debug("calling get reminder with live connection");

//                ResultSet resultSet = mySqlConnect.getReminder(UserId, ReminderId);


            preparedStatement = mySqlConnect.openConnection().prepareStatement("SELECT * FROM reminder " +
                    "WHERE id = ? AND userID = ?");
            preparedStatement.setString(1, ReminderId);
            preparedStatement.setString(2, UserId);
            resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    reminder = new Reminder(resultSet.getString("id"),
                            resultSet.getString("userID"),
                            resultSet.getTimestamp("reminderTime").toLocalDateTime());

                    reminders.add(reminder);
                }

//                mySqlConnect.closeConnection();
//            }
        } catch (SQLException exception){
            LOG.error(String.format("SQL exception: %s", exception.getMessage()),
             exception);
            response.setStatusCode(500);

        } finally {
            mySqlConnect.closeConnection();
        }


        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String responseBody = objectMapper.writeValueAsString(reminders);
            response.setBody(responseBody);

        } catch (JsonProcessingException exception){
            LOG.error("unable to marshal tasks array", exception);
        }

        return response;

    }

}
