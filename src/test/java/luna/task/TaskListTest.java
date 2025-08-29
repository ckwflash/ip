package luna.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import luna.exception.LunaException;

/**
 * Test class for TaskList
 */
public class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;

    @BeforeEach
    public void setUp() throws LunaException {
        taskList = new TaskList();
        todoTask = new ToDoTask("read book");
        deadlineTask = new DeadlineTask("submit assignment /by 2024-12-31");
        eventTask = new EventTask("team meeting /from 2024-09-01 /to 2024-09-02");
    }

    @Test
    public void markTask_validIndex_success() throws LunaException {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        // Initially tasks should be unmarked
        assertFalse(todoTask.isDone());
        assertFalse(deadlineTask.isDone());

        // Mark first task as done
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());
        assertFalse(deadlineTask.isDone());

        // Mark second task as done
        taskList.markTask(1, true);
        assertTrue(todoTask.isDone());
        assertTrue(deadlineTask.isDone());
    }

    @Test
    public void markTask_unmarkTask_success() throws LunaException {
        taskList.add(todoTask);

        // Mark task as done
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        // Unmark task
        taskList.markTask(0, false);
        assertFalse(todoTask.isDone());
    }

    @Test
    public void markTask_toggleTaskMultipleTimes_success() throws LunaException {
        taskList.add(todoTask);

        // Mark and unmark multiple times
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        taskList.markTask(0, false);
        assertFalse(todoTask.isDone());

        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        taskList.markTask(0, false);
        assertFalse(todoTask.isDone());
    }

    @Test
    public void markTask_negativeIndex_throwsException() {
        taskList.add(todoTask);

        LunaException exception = assertThrows(LunaException.class, () -> {
            taskList.markTask(-1, true);
        });
        assertEquals("Task index is out of bounds", exception.getMessage());
    }

    @Test
    public void markTask_indexTooLarge_throwsException() {
        taskList.add(todoTask);

        LunaException exception = assertThrows(LunaException.class, () -> {
            taskList.markTask(1, true);
        });
        assertEquals("Task index is out of bounds", exception.getMessage());
    }

    @Test
    public void markTask_emptyList_throwsException() {
        LunaException exception = assertThrows(LunaException.class, () -> {
            taskList.markTask(0, true);
        });
        assertEquals("Task index is out of bounds", exception.getMessage());
    }

    @Test
    public void markTask_boundaryIndex_success() throws LunaException {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);

        // Test first index (0)
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        // Test last valid index (2)
        taskList.markTask(2, true);
        assertTrue(eventTask.isDone());

        // Test middle index (1)
        taskList.markTask(1, true);
        assertTrue(deadlineTask.isDone());
    }

    @Test
    public void markTask_largeIndex_throwsException() {
        taskList.add(todoTask);

        LunaException exception = assertThrows(LunaException.class, () -> {
            taskList.markTask(100, true);
        });
        assertEquals("Task index is out of bounds", exception.getMessage());
    }

    @Test
    public void markTask_sameTaskMultipleTimes_success() throws LunaException {
        taskList.add(todoTask);

        // Mark as done multiple times
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        taskList.markTask(0, true);
        assertTrue(todoTask.isDone()); // Should still be done

        // Unmark multiple times
        taskList.markTask(0, false);
        assertFalse(todoTask.isDone());

        taskList.markTask(0, false);
        assertFalse(todoTask.isDone()); // Should still be undone
    }

    @Test
    public void markTask_differentTaskTypes_success() throws LunaException {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);

        // Mark all different types of tasks
        taskList.markTask(0, true); // ToDoTask
        taskList.markTask(1, true); // DeadlineTask
        taskList.markTask(2, true); // EventTask

        assertTrue(todoTask.isDone());
        assertTrue(deadlineTask.isDone());
        assertTrue(eventTask.isDone());
    }

    @Test
    public void markTask_withTaskListOperations_success() throws LunaException {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        // Mark first task
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());

        // Add another task
        taskList.add(eventTask);

        // Mark the newly added task
        taskList.markTask(2, true);
        assertTrue(eventTask.isDone());

        // Original marked task should still be marked
        assertTrue(todoTask.isDone());
        assertFalse(deadlineTask.isDone());
    }

    @Test
    public void markTask_afterTaskRemoval_correctIndexing() throws LunaException {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);

        // Remove middle task
        taskList.remove(1); // Remove deadlineTask

        // Now eventTask should be at index 1
        taskList.markTask(1, true);
        assertTrue(eventTask.isDone());

        // todoTask should still be at index 0
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());
    }
}
