package model;

import com.sunshine.model.Reminder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.Month;

public class ReminderTest {


    @Test
    @DisplayName("Reminder Test: constructor with reminderTime")
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




}
