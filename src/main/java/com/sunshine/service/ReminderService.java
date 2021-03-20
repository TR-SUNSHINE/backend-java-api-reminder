package com.sunshine.service;

import com.sunshine.model.Reminder;
import com.sunshine.database.MySqlConnect;

import java.sql.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReminderService {

    private MySqlConnect mySqlConnect = new MySqlConnect();

    private static final Logger LOG = LogManager.getLogger(ReminderService.class);

    public APIGatewayProxyResponseEvent saveReminder(Reminder reminder) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        response.setHeaders(headers);

        if (mySqlConnect.createReminder(reminder) == 1) {

            response.setStatusCode(201);

        } else {
            response.setStatusCode(500);
        }

        mySqlConnect.closeConnection();

        return response;

    }


    public ArrayList<Reminder> getReminder(String userId, String reminderId) {

        LOG.info("getReminder in ReminderService");

        ArrayList<Reminder> reminders = mySqlConnect.readReminder(userId, reminderId);



            mySqlConnect.closeConnection();


        return reminders;

    }


}
