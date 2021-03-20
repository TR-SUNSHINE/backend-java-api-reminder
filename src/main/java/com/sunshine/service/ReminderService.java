package com.sunshine.service;

import com.sunshine.model.Reminder;
import com.sunshine.database.MySqlConnect;

import java.sql.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class ReminderService {

    private MySqlConnect mySqlConnect = new MySqlConnect();

    private static final Logger LOG = LogManager.getLogger(ReminderService.class);

    public int saveReminder(Reminder reminder) {

        LOG.info("saveReminder in ReminderService");

       return mySqlConnect.createReminder(reminder);

    }

    public ArrayList<Reminder> getReminder(String userId, String reminderId) {

        LOG.info("getReminder in ReminderService");

        ArrayList<Reminder> reminders = mySqlConnect.readReminder(userId, reminderId);

        return reminders;

    }

    public int changeReminder(Reminder reminder){

        LOG.info("updateReminder in ReminderService");

        return mySqlConnect.updateReminder(reminder);
    }

    public void deleteReminder(String userId, String reminderId){

        LOG.info("deleteReminder in ReminderService");

        mySqlConnect.deleteReminder(userId, reminderId);
    }

}
