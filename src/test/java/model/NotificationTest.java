package model;

import com.sunshine.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NotificationTest {

    LocalDateTime christmas = LocalDateTime.of(2021, Month.DECEMBER, 25,7, 30);
    String reminderId = "0f6d04c5-2014-4a9b-9ec3-287c05a47ca5";
    String userId = "b06418f8-be80-4da5-911e-fe1b1413d413";
    String email = "123@gmail.com";
    String userName = "bobby";

    @Test
    @DisplayName("Test reminder time")
    public void testReminderTime(){
        Notification notification = new Notification(reminderId, christmas, userId, email, userName );
        assertEquals("2021-12-25T07:30", notification.getReminderTime().toString(), "Reminder was " +
                "incorrect");
        assertEquals("DECEMBER", notification.getReminderTime().getMonth().toString(), "Month was " +
                "incorrect");
        assertEquals(7, notification.getReminderTime().getHour(), "Hour was incorrect");
        assertEquals(25, notification.getReminderTime().getDayOfMonth(), "Day of month was incorrect");
        assertEquals(30, notification.getReminderTime().getMinute(), "Minute was incorrect");
    }

    @Test
    @DisplayName("Test reminderId")
    public void testReminderId(){
        Notification notification = new Notification(reminderId, christmas, userId, email,
                userName );
        assertEquals("0f6d04c5-2014-4a9b-9ec3-287c05a47ca5", notification.getReminderId(),
                "reminderId was incorrect");
    }

    @Test
    @DisplayName("Test userId")
    public void testUserId(){
        Notification notification = new Notification(reminderId, christmas, userId, email,
                userName );
        assertEquals("b06418f8-be80-4da5-911e-fe1b1413d413", notification.getUserId(),
                "userId was incorrect");
    }

    @Test
    @DisplayName("Test email")
    public void testEmail(){
        Notification notification = new Notification(reminderId, christmas, userId, email,
                userName );
        assertEquals("123@gmail.com", notification.getEmail(),
                "email was incorrect");
    }

    @Test
    @DisplayName("Test userName")
    public void testUserName(){
        Notification notification = new Notification(reminderId, christmas, userId, email,
                userName );
        assertEquals("bobby", notification.getUserName(),
                "email was incorrect");
    }


}
