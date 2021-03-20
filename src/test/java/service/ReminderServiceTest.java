package service;

import com.sunshine.database.MySqlConnect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sunshine.model.Reminder;
import com.sunshine.service.ReminderService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;

public class ReminderServiceTest {

    @Test
    @DisplayName("Test saveReminder in ReminderService returns 1 when database successfully " +
            "updated")
    public void testSaveReminderHappyPath(){

        // Arrange
        LocalDateTime summerSolstice = LocalDateTime.of(2021, Month.JUNE, 21, 21, 30);
        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";
        Reminder reminder = new Reminder(reminderId, userId,summerSolstice);

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.createReminder(reminder)).thenReturn(1);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        int response = reminderService.saveReminder(reminder);

        // Assert
        assertEquals(1, response, "saveReminder does not return 1 when database successfully " +
                "updated");

    }

    @Test
    @DisplayName("Test saveReminder in ReminderService returns 0 when database not " +
            "updated")
    public void testSaveReminderUnHappyPath(){

        // Arrange
        LocalDateTime summerSolstice = LocalDateTime.of(2021, Month.JUNE, 21, 21, 30);
        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";
        Reminder reminder = new Reminder(reminderId, userId,summerSolstice);

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.createReminder(reminder)).thenReturn(0);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        int response = reminderService.saveReminder(reminder);

        // Assert
        assertEquals(0, response, "saveReminder does not return 0 when database has not updated");

    }

    @Test
    @DisplayName("Test getReminder in ReminderService returns an array with 1 Reminder objects " +
            "when " +
            "updated")
    public void testGetReminderHappyPath(){

        // Arrange
        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";

        LocalDateTime summerSolstice = LocalDateTime.of(2021, Month.JUNE, 21, 21, 30);
        Reminder reminder = new Reminder(reminderId, userId,summerSolstice);
        ArrayList<Reminder> reminderList = new ArrayList<>(Collections.singletonList(reminder));

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.readReminder(userId, reminderId)).thenReturn(reminderList);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

       ArrayList<Reminder> response = reminderService.getReminder(userId, reminderId);

        // Assert
        assertEquals("ff4fe5e6-f112-423b-9196-c1f9dd998851", response.get(0).getReminderId(),
                "readReminder " +
                "does not " +
                "return an array with a single Reminder object");
        assertEquals("f622d2f8-78c7-4419-ac0f-287409980f20", response.get(0).getUserId(),
                "readReminder " +
                        "does not " +
                        "return an array with a single Reminder object");
        assertEquals(LocalDateTime.of(2021, Month.JUNE, 21, 21, 30), response.get(0).getReminderTime(),
                "readReminder " +
                        "does not " +
                        "return an array with a single Reminder object");

    }

    @Test
    @DisplayName("Test changeReminder in ReminderService returns 1 when the database has " +
            "been updated")
    public void testChangeReminderHappyPath(){
        // Arrange
        LocalDateTime winterSolstice = LocalDateTime.of(2021, Month.DECEMBER, 21, 16, 0);
        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";
        Reminder reminder = new Reminder(reminderId, userId,winterSolstice);

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.updateReminder(reminder)).thenReturn(1);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        int response = reminderService.changeReminder(reminder);

        // Assert
        assertEquals(1, response, "changeReminder does not return 1 when database successfully " +
                "updated");
    }

    @Test
    @DisplayName("Test changeReminder in ReminderService returns 0 when the database has not" +
            "been updated")
    public void testChangeReminderUnHappyPath(){

        // Arrange
        LocalDateTime winterSolstice = LocalDateTime.of(2021, Month.DECEMBER, 21, 16, 0);
        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";
        Reminder reminder = new Reminder(reminderId, userId,winterSolstice);

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.updateReminder(reminder)).thenReturn(0);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        int response = reminderService.changeReminder(reminder);

        // Assert
        assertEquals(0, response, "changeReminder does not return 0 when database has not been " +
                "updated");
    }

    @Test
    @DisplayName("Test deleteReminder in ReminderService calls deleteReminder in mySqlConnect")

        public void testDeleteReminder() {

        String reminderId = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        reminderService.deleteReminder(userId, reminderId);

        verify(mockMySqlConnect).deleteReminder(userId,reminderId);
        }

}
