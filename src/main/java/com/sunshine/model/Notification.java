package com.sunshine.model;

import java.time.LocalDateTime;

public class Notification {

    private String reminderId;
    private LocalDateTime reminderTime;
    private String userId;
    private String email;
    private String userName;

    public Notification() {}

    public Notification(String reminderId, LocalDateTime reminderTime, String userId,
                        String email, String userName){
        this.reminderId = reminderId;
        this.reminderTime = reminderTime;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }

    public String getReminderId(){ return this.reminderId;}

    public void setReminderId(String reminderId){this.reminderId = reminderId;}

    public LocalDateTime getReminderTime(){ return this.reminderTime;}

    public void setReminderTime(LocalDateTime reminderTime){ this.reminderTime =
            reminderTime; }

    public String getUserId(){ return this.userId;}

    public void setUserId(String userId){this.userId = userId;}

    public String getEmail(){ return this.email;}

    public void setEmail(String email){this.email = email;}

    public String getUserName(){return this.userName;}

    public void setUserName(String userName){this.userId = userName;}
}

