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

    public ArrayList<Reminder> sendReminder(String userId) {

        LOG.info("sendReminder in ReminderService");

        ArrayList<Reminder> reminders =  this.mySqlConnect.readReminders(userId);

        LocalDateTime latestReminderTime = reminders.get(reminders.size() - 1).getReminderTime();

        int isInFuture = latestReminderTime.compareTo(LocalDateTime.now());

        long difference = ChronoUnit.MINUTES.between(LocalDateTime.now(), latestReminderTime);
        LOG.debug("Difference between reminder & now: {}", difference);
        if (isInFuture > 0 && (difference > 20 && difference < 61)){

            return new ArrayList<>(reminders.subList(reminders.size() - 1, reminders.size()));

        } else {

            return new ArrayList<>();
        }
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
