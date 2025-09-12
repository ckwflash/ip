package luna.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import luna.exception.LunaException;

/**
 * Test class for DeadlineTask
 */
public class DeadlineTaskTest {

    @Test
    public void constructor_validInputWithDateTime_success() throws LunaException {
        DeadlineTask task = new DeadlineTask("submit assignment /by 2024-12-31 1800");
        assertEquals("submit assignment", task.description);
        assertTrue(task.toString().contains("(by: Dec 31 2024, 6:00PM)"));
    }

    @Test
    public void constructor_validInputWithDateOnly_success() throws LunaException {
        DeadlineTask task = new DeadlineTask("finish project /by 2024-12-25");
        assertEquals("finish project", task.description);
        assertTrue(task.toString().contains("(by: Dec 25 2024)"));
    }

    @Test
    public void constructor_validInputWithSlashFormat_success() throws LunaException {
        DeadlineTask task = new DeadlineTask("complete homework /by 25/12/2024");
        assertEquals("complete homework", task.description);
        assertTrue(task.toString().contains("(by: Dec 25 2024)"));
    }

    @Test
    public void constructor_validInputWithSlashFormatAndTime_success() throws LunaException {
        DeadlineTask task = new DeadlineTask("attend meeting /by 25/12/2024 1400");
        assertEquals("attend meeting", task.description);
        assertTrue(task.toString().contains("(by: Dec 25 2024, 2:00PM)"));
    }

    @Test
    public void constructor_invalidDateFormat_fallbackToOriginal() throws LunaException {
        DeadlineTask task = new DeadlineTask("submit report /by next Monday");
        assertEquals("submit report", task.description);
        assertTrue(task.toString().contains("(by: next Monday)"));
    }

    @Test
    public void constructor_missingEndTime_throwsException() {
        LunaException exception = assertThrows(LunaException.class, () -> {
            new DeadlineTask("submit assignment /by ");
        });
        assertEquals("Please provide end time for deadline with /by", exception.getMessage());
    }

    @Test
    public void constructor_noByKeyword_throwsException() {
        LunaException exception = assertThrows(LunaException.class, () -> {
            new DeadlineTask("submit assignment");
        });
        assertEquals("Please provide end time for deadline with /by", exception.getMessage());
    }

    @Test
    public void constructor_emptyDescription_throwsException() {
        LunaException exception = assertThrows(LunaException.class, () -> {
            new DeadlineTask(" /by 2024-12-31");
        });
        assertEquals("Description cannot be empty", exception.getMessage());
    }

    @Test
    public void constructor_multipleByKeywords_usesFirst() throws LunaException {
        DeadlineTask task = new DeadlineTask("task /by 2024-12-31 /by 2025-01-01");
        assertEquals("task", task.description);
        assertTrue(task.toString().contains("2024-12-31 /by 2025-01-01"));
    }

    @Test
    public void toString_formatsCorrectly() throws LunaException {
        DeadlineTask task = new DeadlineTask("read book /by 2024-06-15");
        String view = task.toString();
        assertTrue(view.startsWith("[D] [ ] read book"));
        assertTrue(view.contains("(by: Jun 15 2024)"));
    }

    @Test
    public void toString_markedTaskFormatsCorrectly() throws LunaException {
        DeadlineTask task = new DeadlineTask("read book /by 2024-06-15");
        task.markDone(true);
        String view = task.toString();
        assertTrue(view.startsWith("[D] [X] read book"));
        assertTrue(view.contains("(by: Jun 15 2024)"));
    }

    @Test
    public void constructor_validFormattedDate_parsesCorrectly() throws LunaException {
        DeadlineTask task = new DeadlineTask("task /by Dec 31 2024, 11:59PM");
        assertEquals("task", task.description);
        assertTrue(task.toString().contains("(by: Dec 31 2024, 11:59PM)"));
    }

    @Test
    public void constructor_validFormattedDateOnly_parsesCorrectly() throws LunaException {
        DeadlineTask task = new DeadlineTask("task /by Dec 31 2024");
        assertEquals("task", task.description);
        assertTrue(task.toString().contains("(by: Dec 31 2024)"));
    }

    @Test
    public void constructor_edgeCaseDates_handlesCorrectly() throws LunaException {
        // Test leap year
        DeadlineTask task1 = new DeadlineTask("task1 /by 29/2/2024");
        assertTrue(task1.toString().contains("Feb 29 2024"));

        // Test single digit day/month
        DeadlineTask task2 = new DeadlineTask("task2 /by 1/1/2024");
        assertTrue(task2.toString().contains("Jan 01 2024"));
    }

    @Test
    public void constructor_whitespaceHandling_success() throws LunaException {
        DeadlineTask task = new DeadlineTask("  task with spaces  /by  2024-12-31  ");
        assertEquals("  task with spaces", task.description); // Exact parsing result
        assertTrue(task.toString().contains("(by: Dec 31 2024)"));
    }
}
