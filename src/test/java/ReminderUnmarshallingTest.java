import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunshine.model.Reminder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ReminderUnmarshallingTest {

    @Test
    void testUnmarshallReminder() throws IOException {
        String requestBody = "{" +
                "\"reminderTime\": \"2020-09-30T07:27:39Z\"" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Reminder reminder = objectMapper.readValue(requestBody, Reminder.class);
        Assertions.assertNotNull(reminder.getReminderTime());
        System.out.println(reminder.getReminderTime().toString());
    }

}
