package com.sunshine.service;

import com.sunshine.model.Reminder;
import com.sunshine.database.MySqlConnect;

import java.sql.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class ReminderService {

    private final MySqlConnect mySqlConnect;

    private static final Logger LOG = LogManager.getLogger(ReminderService.class);

    public ReminderService(MySqlConnect mySqlConnect){
        this.mySqlConnect = mySqlConnect;
    }

    public int saveReminder(Reminder reminder) {

        LOG.info("saveReminder in ReminderService");

       return this.mySqlConnect.createReminder(reminder);

    }

    public ArrayList<Reminder> getReminder(String userId, String reminderId) {

        LOG.info("getReminder in ReminderService");

        return this.mySqlConnect.readReminder(userId, reminderId);

    }

    public ArrayList<Reminder> getReminders(String userId) {

        LOG.info("getReminder in ReminderService");

        return this.mySqlConnect.readReminders(userId);

    }

    public int changeReminder(Reminder reminder){

        LOG.info("updateReminder in ReminderService");

        return this.mySqlConnect.updateReminder(reminder);
    }

    public void deleteReminder(String userId, String reminderId){

        LOG.info("deleteReminder in ReminderService");

        this.mySqlConnect.deleteReminder(userId, reminderId);
    }

}
