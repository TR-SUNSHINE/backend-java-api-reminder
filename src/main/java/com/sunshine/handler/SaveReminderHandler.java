package com.sunshine.handler;

import java.sql.*;
import com.sunshine.database.MySqlConnect;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.io.IOException;
import java.util.*;

public class SaveReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(SaveReminderHandler.class);

    MySqlConnect mySqlConnect = new MySqlConnect();

//    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context){

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String requestBody = request.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(201);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        try {
            // create database object  - already started
            // open connection on database - done
            // build reminder object - in Handler - further separate out after
            // call database object to insert data with parameter as reminder object
            // close connection to database object - done

            Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);
            reminder.setUserId(UserId);
            reminder.setReminderId(UUID.randomUUID().toString());

            mySqlConnect.openConnection();

            mySqlConnect.insertReminder(reminder);

//            preparedStatement = mySqlConnect.connect().prepareStatement("INSERT INTO reminder (id, " +
//                    "userID," +
//                    "reminderTime) VALUES(?, ?, ?)");
//            preparedStatement.setString(1, UUID.randomUUID().toString());
//            preparedStatement.setString(2, UserId);
//            preparedStatement.setTimestamp(3, Timestamp.valueOf(reminder.getReminderTime()));
//            preparedStatement.execute();

            mySqlConnect.closeConnection();

            // return reminderId to frontend

        } catch (IOException exception){
            LOG.error(String.format("Unable to unmarshall JSON for adding a reminder %s",
                    exception.getMessage()));
            response.setStatusCode(500);

        }
//        catch (SQLException exception){
//            LOG.error(String.format("SQL exception: %s",exception.getMessage()), exception);
//            response.setStatusCode(500);
//        }
        finally {
            mySqlConnect.closeConnection();
        }

        return response;

    }
}
