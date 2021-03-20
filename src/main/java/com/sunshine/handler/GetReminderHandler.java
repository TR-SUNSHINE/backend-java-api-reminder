package com.sunshine.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunshine.service.ReminderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sunshine.model.Reminder;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class GetReminderHandler implements RequestHandler<APIGatewayProxyRequestEvent,
        APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(GetReminderHandler.class);

    private final ReminderService reminderService = new ReminderService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {

        LOG.info("request received");

        String UserId = request.getPathParameters().get("userId");
        String ReminderId = request.getPathParameters().get("reminderId");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        if (UserId.length() != 36 || ReminderId.length() != 36) {

            response.setStatusCode(400);

        } else {

            ArrayList<Reminder> reminders = reminderService.getReminder(UserId, ReminderId);

            ObjectMapper objectMapper = new ObjectMapper();

            try {

                String responseBody = objectMapper.writeValueAsString(reminders);
                response.setBody(responseBody);

            } catch (JsonProcessingException exception) {
                LOG.error("unable to marshal tasks array", exception);
                response.setStatusCode(500);
            }
        }

        return response;

    }

}
