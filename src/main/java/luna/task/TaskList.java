package luna.task;
import java.util.ArrayList;

import luna.exception.LunaException;

/**
 * Encapsulates task list operations
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Default constructor that creates an empty task list
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert tasks != null : "Tasks list should be initialized";
        assert tasks.isEmpty() : "New task list should be empty";
    }

    /**
     * Constructor that creates a task list with existing tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Input tasks list should not be null";
        this.tasks = tasks;
        assert this.tasks == tasks : "Tasks list should be set correctly";
    }    /**
     * Copy constructor that creates a new TaskList with copies of all tasks
     */
    public TaskList(TaskList other) {
        assert other != null : "Other TaskList should not be null";

        this.tasks = new ArrayList<>();
        for (Task task : other.tasks) {
            this.tasks.add(task.copy()); // Create deep copy of each task
        }

        assert this.tasks.size() == other.tasks.size() : "Copied TaskList should have same size";
    }

    /**
     * Returns the underlying task list
     */
    public ArrayList<Task> getTasks() {
        assert tasks != null : "Tasks list should never be null";
        return tasks;
    }

    /**
     * Adds a task to the list
     */
    public void add(Task task) {
        assert task != null : "Task to add should not be null";

        int oldSize = tasks.size();
        tasks.add(task);

        assert tasks.size() == oldSize + 1 : "Task list size should increase by 1 after adding";
        assert tasks.contains(task) : "Task list should contain the added task";
    }

    /**
     * Removes a task at the given index
     */
    public Task remove(int index) {
        assert index >= 0 : "Index should be non-negative";
        assert index < tasks.size() : "Index should be within bounds";

        int oldSize = tasks.size();
        Task removed = tasks.remove(index);

        assert removed != null : "Removed task should not be null";
        assert tasks.size() == oldSize - 1 : "Task list size should decrease by 1 after removal";

        return removed;
    }

    /**
     * Gets a task at the given index
     */
    public Task get(int index) {
        assert index >= 0 : "Index should be non-negative";
        assert index < tasks.size() : "Index should be within bounds";

        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";

        return task;
    }

    /**
     * Returns the number of tasks
     */
    public int size() {
        int size = tasks.size();
        assert size >= 0 : "Task list size should never be negative";
        return size;
    }

    /**
     * Marks a task as done or undone
     */
    public void markTask(int index, boolean isDone) throws LunaException {
        assert tasks != null : "Tasks list should not be null";
        
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        
        Task task = tasks.get(index);
        assert task != null : "Task at valid index should not be null";
        
        boolean oldStatus = task.isDone();
        task.markDone(isDone);
        
        assert task.isDone() == isDone : "Task status should be updated correctly";
        assert task.isDone() != oldStatus || oldStatus == isDone : "Task status should change unless already set";
    }

    /**
     * Delete a task at the given index
     */
    public Task deleteTask(int index) throws LunaException {
        assert tasks != null : "Tasks list should not be null";
        
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        
        int oldSize = tasks.size();
        Task removed = tasks.remove(index);
        
        assert removed != null : "Removed task should not be null";
        assert tasks.size() == oldSize - 1 : "Task list size should decrease by 1";
        
        return removed;
    }

    /**
     * Finds tasks that contain the given keyword in their description
     */
    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Search keyword should not be null";
        assert tasks != null : "Tasks list should not be null";
        
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "Task in list should not be null";
            assert task.description != null : "Task description should not be null";
            
            if (task.description.toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        
        assert matchingTasks != null : "Matching tasks list should not be null";
        assert matchingTasks.size() <= tasks.size() : "Matching tasks should not exceed total tasks";
        
        return matchingTasks;
    }

    /**
     * Creates a deep copy of this TaskList for undo functionality
     */
    public TaskList copy() {
        return new TaskList(this);
    }
}
