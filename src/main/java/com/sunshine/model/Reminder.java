package com.sunshine.model;

import java.time.LocalDateTime;

public class Reminder {
    private String reminderId;
    private String userId;
    private String walkId;
    private LocalDateTime reminderTime;

    public Reminder() {}

    public Reminder(String reminderId, String userId, LocalDateTime reminderTime){
        this.reminderId = reminderId;
        this.userId = userId;
        this.reminderTime = reminderTime;
    }

    public Reminder(String reminderId, String userId, String walkId, LocalDateTime reminderTime){
        this.reminderId = reminderId;
        this.userId = userId;
        this.walkId = walkId;
        this.reminderTime = reminderTime;
    }

    public String getReminderId(){ return reminderId;}

    public String getUserId(){ return userId;}

    public String getWalkId(){ return walkId;}

    public LocalDateTime getReminderTime(){ return reminderTime;}
}
