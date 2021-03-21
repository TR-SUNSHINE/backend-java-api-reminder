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
import java.util.Arrays;
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
    @DisplayName("Test getReminder in ReminderService returns an array with 1 Reminder object")
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
    @DisplayName("Test getReminder in ReminderService returns an empty array when the reminder " +
            "requested is" +
            "not present in the database")
    public void testGetReminderUnHappyPath(){

        // Arrange
        String reminderId = "abc";
        String userId = "123";

        ArrayList<Reminder> reminderList = new ArrayList<>();

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.readReminder(userId, reminderId)).thenReturn(reminderList);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        ArrayList<Reminder> response = reminderService.getReminder(userId, reminderId);

        // Assert
        assertEquals(0, response.size(),
                "readReminder " +
                        "does not " +
                        "return an empty array when the reminder requested is not present in the " +
                        "database");
    }

    @Test
    @DisplayName("Test getReminders in ReminderService returns an array with one or more Reminder objects")
    public void testGetRemindersHappyPath(){

        // Arrange
        String userId = "f622d2f8-78c7-4419-ac0f-287409980f20";
        String reminderId1 = "ff4fe5e6-f112-423b-9196-c1f9dd998851";
        String reminderId2 = "7fc3423a-d367-4b6d-aa92-9252c4761774";

        LocalDateTime summerSolstice = LocalDateTime.of(2021, Month.JUNE, 21, 21, 30);
        LocalDateTime winterSolstice = LocalDateTime.of(2021, Month.DECEMBER, 21, 15, 30);
        Reminder reminder1 = new Reminder(reminderId1, userId,summerSolstice);
        Reminder reminder2 = new Reminder(reminderId2, userId,winterSolstice);
        ArrayList<Reminder> reminderList = new ArrayList<>(Arrays.asList(reminder1,
                reminder2));

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.readReminders(userId)).thenReturn(reminderList);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        ArrayList<Reminder> response = reminderService.getReminders(userId);

        // Assert
        assertEquals("ff4fe5e6-f112-423b-9196-c1f9dd998851", response.get(0).getReminderId(),
                "readReminder " +
                        "does not " +
                        "return an array correct Reminder objects");
        assertEquals("f622d2f8-78c7-4419-ac0f-287409980f20", response.get(0).getUserId(),
                "readReminder " +
                        "does not " +
                        "return an array with correct Reminder objects");
        assertEquals(LocalDateTime.of(2021, Month.JUNE, 21, 21, 30), response.get(0).getReminderTime(),
                "readReminder " +
                        "does not " +
                        "return an array with correct Reminder objects");
        assertEquals("7fc3423a-d367-4b6d-aa92-9252c4761774", response.get(1).getReminderId(),
                "readReminder " +
                        "does not " +
                        "return an array correct Reminder objects");
        assertEquals("f622d2f8-78c7-4419-ac0f-287409980f20", response.get(1).getUserId(),
                "readReminder " +
                        "does not " +
                        "return an array with correct Reminder objects");
        assertEquals(LocalDateTime.of(2021, Month.DECEMBER, 21, 15,
                30),
                response.get(1).getReminderTime(),
                "readReminder " +
                        "does not " +
                        "return an array with correct Reminder objects");
    }

    @Test
    @DisplayName("Test getReminders in ReminderService returns an empty array when the userId is " +
            "not present in the database")
    public void testGetRemindersUnhappyPath(){

        // Arrange
        String userId = "abc";

        ArrayList<Reminder> reminderList = new ArrayList<>();

        MySqlConnect mockMySqlConnect = mock(com.sunshine.database.MySqlConnect.class);
        when(mockMySqlConnect.readReminders(userId)).thenReturn(reminderList);

        // Act
        ReminderService reminderService = new ReminderService(mockMySqlConnect);

        ArrayList<Reminder> response = reminderService.getReminders(userId);

        // Assert
        assertEquals(0, response.size(),
                "readReminders does not return an empty array when the userId is not present in the " +
                        "database");
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
