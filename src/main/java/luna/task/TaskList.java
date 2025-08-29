package luna.task;
import java.util.ArrayList;

import luna.exception.LunaException;

/**
 * Encapsulates task list operations
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the underlying task list
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds a task to the list
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task at the given index
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Gets a task at the given index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Marks a task as done or undone
     */
    public void markTask(int index, boolean isDone) throws LunaException {
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        tasks.get(index).markDone(isDone);
    }

    /**
     * Delete a task at the given index
     */
    public Task deleteTask(int index) throws LunaException {
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        return tasks.remove(index);
    }

    /**
     * Finds tasks that contain the given keyword in their description
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description.toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
