package com.sunshine.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunshine.GetReminderHandler;
import com.sunshine.model.Reminder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

public class handleReminderRequests {

    private APIGatewayProxyRequestEvent request;

    private static final Logger LOG = LogManager.getLogger(GetReminderHandler.class);

    public handleReminderRequests(APIGatewayProxyRequestEvent request){
        this.request = request;
    }

    public Reminder marshallRequest (){

        String UserId = this.request.getPathParameters().get("userId");
        String ReminderId = this.request.getPathParameters().get("reminderId");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {

            Reminder reminder = objectMapper.readValue(this.request.getBody(), Reminder.class);
            reminder.setUserId(UserId);
            reminder.setReminderId(ReminderId);

            return reminder;

        } catch (IOException exception){
            LOG.error(String.format("Unable to unmarshall JSON to reminder: %s",
                    exception.getMessage()));
//            response.setStatusCode(500);

            return new Reminder();
        }
    }

    public APIGatewayProxyRequestEvent getRequest(){return this.request;}
}
