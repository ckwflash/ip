package luna.task;
/**
 * Task with a description and completion status.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Task with the specified description and sets its status to not done
    */
    public Task(String description) {
        assert description != null : "Task description should not be null";
        // Note: Allow empty descriptions to reach business logic for proper exception handling

        this.description = description;
        this.isDone = false;

        assert this.description.equals(description) : "Description should be set correctly";
        assert !this.isDone : "New task should be marked as not done";
    }

    /**
     * Marks the task as done or not done
     */
    public void markDone(boolean status) {
        this.isDone = status;
        assert this.isDone == status : "Task status should be set correctly";
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getStatusIcon() {
        String icon = (isDone ? "X" : " ");
        assert icon != null : "Status icon should never be null";
        assert icon.equals("X") || icon.equals(" ") : "Status icon should be either 'X' or ' '";
        return icon;
    }
<<<<<<< HEAD
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), description);
=======

    /**
     * Returns the string representation of the task for display
     */
    public String taskView() {
        assert description != null : "Description should not be null when generating task view";
        String view = String.format("[%s] %s", this.getStatusIcon(), description);
        assert view != null : "Task view should never be null";
        assert view.contains(description) : "Task view should contain the description";
        return view;
>>>>>>> master
    }
}
