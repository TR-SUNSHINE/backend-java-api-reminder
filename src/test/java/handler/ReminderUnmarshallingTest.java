package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunshine.model.Reminder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReminderUnmarshallingTest {

    @Test
    void testUnmarshallReminderWithZ() throws IOException {
        String requestBody = "{" +
                "\"reminderTime\": \"2020-09-30T07:27:39Z\"" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);
        Assertions.assertNotNull(reminder.getReminderTime());
        assertEquals("SEPTEMBER", reminder.getReminderTime().getMonth().toString(), "Month was " +
                "incorrect");
        assertEquals(7, reminder.getReminderTime().getHour(), "Hour was incorrect");
        assertEquals(30, reminder.getReminderTime().getDayOfMonth(), "Day of month was incorrect");
        assertEquals(27, reminder.getReminderTime().getMinute(), "Minute was incorrect");
        System.out.println(reminder.getReminderTime().toString());
    }

    @Test
    void testUnmarshallReminderWithOutZ() throws IOException {
        String requestBody = "{" +
                "\"reminderTime\": \"2020-09-30T07:27:39\"" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);
        assertEquals("SEPTEMBER", reminder.getReminderTime().getMonth().toString(), "Month was " +
                "incorrect");
        assertEquals(7, reminder.getReminderTime().getHour(), "Hour was incorrect");
        assertEquals(30, reminder.getReminderTime().getDayOfMonth(), "Day of month was incorrect");
        assertEquals(27, reminder.getReminderTime().getMinute(), "Minute was incorrect");
        Assertions.assertNotNull(reminder.getReminderTime());
        System.out.println(reminder.getReminderTime().toString());
    }

}
