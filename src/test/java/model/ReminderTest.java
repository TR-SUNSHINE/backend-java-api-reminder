package model;

import com.sunshine.model.Reminder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.Month;

public class ReminderTest {

    @Test
    @DisplayName("Test reminder time: constructor with reminderTime")
    public void testReminderTime(){
        LocalDateTime christmas = LocalDateTime.of(2021, Month.DECEMBER, 25,7, 30);
        Reminder reminder = new Reminder(christmas);
        assertEquals("2021-12-25T07:30", reminder.getReminderTime().toString(), "Reminder was " +
                "incorrect");
        assertEquals("DECEMBER", reminder.getReminderTime().getMonth().toString(), "Month was incorrect");
        assertEquals(7, reminder.getReminderTime().getHour(), "Hour was incorrect");
        assertEquals(25, reminder.getReminderTime().getDayOfMonth(), "Day of month was incorrect");
        assertEquals(30, reminder.getReminderTime().getMinute(), "Minute was incorrect");
    }

    @Test
    @DisplayName("Test reminderId: constructor with userId, reminderId, reminderTime")
    public void testReminderId(){
        LocalDateTime christmas = LocalDateTime.of(2021, Month.DECEMBER, 25,7, 30);
        String reminderId = "0f6d04c5-2014-4a9b-9ec3-287c05a47ca5";
        String userId = "b06418f8-be80-4da5-911e-fe1b1413d413";
        Reminder reminder = new Reminder(reminderId, userId, christmas);
        assertEquals("0f6d04c5-2014-4a9b-9ec3-287c05a47ca5", reminder.getReminderId(),
                "reminderId was incorrect");
    }

    @Test
    @DisplayName("Test userId: constructor with userId, reminderId, reminderTime")
    public void testUserId(){
        LocalDateTime christmas = LocalDateTime.of(2021, Month.DECEMBER, 25,7, 30);
        String reminderId = "0f6d04c5-2014-4a9b-9ec3-287c05a47ca5";
        String userId = "b06418f8-be80-4da5-911e-fe1b1413d413";
        Reminder reminder = new Reminder(reminderId, userId, christmas);
        assertEquals("b06418f8-be80-4da5-911e-fe1b1413d413", reminder.getUserId(),
                "userId was incorrect");
    }
}
