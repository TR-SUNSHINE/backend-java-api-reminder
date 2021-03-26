package com.sunshine.service;

import com.sunshine.model.Reminder;
import com.sunshine.database.MySqlConnect;

import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public ArrayList<Reminder> sendNotifications() {

        LOG.info("sendNotifications in ReminderService");

        ArrayList<Reminder> notifications =  this.mySqlConnect.readNotifications();

        return notifications;

//        LocalDateTime latestReminderTime = notifications.get(notifications.size() - 1).getReminderTime();

//        int isInFuture = latestReminderTime.compareTo(LocalDateTime.now());
//
//        long difference = ChronoUnit.MINUTES.between(LocalDateTime.now(), latestReminderTime);
//        LOG.debug("Difference between reminder & now: {}", difference);
//        if (isInFuture > 0 && (difference > 55 && difference < 65)){

//            return new ArrayList<>(reminders.subList(reminders.size() - 1, reminders.size()));

//        } else {
//
//            return new ArrayList<>();
//        }
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
