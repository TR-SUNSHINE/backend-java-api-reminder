package com.sunshine;

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

        List<Reminder> reminders = new ArrayList<>();
        Reminder reminder;

        try {

            preparedStatement = mySqlConnect.connect().prepareStatement("SELECT * FROM reminder WHERE id =" +
                    " ?");
            preparedStatement.setString(1, ReminderId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                reminder = new Reminder(    resultSet.getString("id"),
                                            resultSet.getString("userID"),
                                            resultSet.getTimestamp("reminderTime").toLocalDateTime());

                reminders.add(reminder);
            }

        } catch (Exception exception){
            LOG.error(String.format("Exception message %s", exception.getMessage()),
             exception);
        } finally {
            mySqlConnect.closeConnection();
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

//    public void closeConnection(){
//
//        try {
//            if (resultSet != null){
//                resultSet.close();
//            }
//
//            if (preparedStatement != null) {
//                preparedStatement.close();
//            }
//
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException exception){
//            LOG.error("Unable to close connection to MySQL - {}", exception.getMessage());
//        }
//    }

}
