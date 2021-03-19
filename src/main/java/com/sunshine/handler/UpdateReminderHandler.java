package com.sunshine.handler;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UpdateReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(SaveReminderHandler.class);

    MySqlConnect mySqlConnect = new MySqlConnect();
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");
        LOG.info(String.format("ReminderId %s",
                ReminderId));
        String requestBody = request.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
//        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        try {

            Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);
            reminder.setUserId(UserId);
            reminder.setReminderId(ReminderId);

            if  (mySqlConnect.updateReminder(reminder) == 1) {
                LOG.debug("updated");
                response.setStatusCode(200);
            } else {
                LOG.debug("update failed");
                response.setStatusCode(500);
            }

            // send reminderId to frontend

        } catch (IOException exception) {
            LOG.error(String.format("Unable to unmarshall JSON for adding a reminder %s",
                    exception.getMessage()));
            response.setStatusCode(500);
        }

        finally {
            mySqlConnect.closeConnection();
        }

        return response;

    }
}

