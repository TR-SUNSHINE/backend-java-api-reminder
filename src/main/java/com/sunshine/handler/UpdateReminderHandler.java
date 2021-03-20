package com.sunshine.handler;

import com.sunshine.database.MySqlConnect;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunshine.service.ReminderService;
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

    private final MySqlConnect mySqlConnect = new MySqlConnect();

    private final ReminderService reminderService = new ReminderService(mySqlConnect);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");
        String requestBody = request.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        Reminder reminder = null;

        if (UserId.length() != 36 || ReminderId.length() != 36) {

            response.setStatusCode(400);

        } else {

            try {

                reminder = objectMapper.readValue(requestBody, Reminder.class);
                reminder.setUserId(UserId);
                reminder.setReminderId(ReminderId);

            } catch (IOException exception) {
                LOG.error(String.format("Unable to unmarshall JSON for updating a reminder %s",
                        exception.getMessage()));
                response.setStatusCode(500);
            }

            if (reminderService.changeReminder(reminder) == 1) {

                LOG.info("updated");

            } else {
                LOG.info("update failed");
                response.setStatusCode(500);
            }
        }

        return response;

    }
}

