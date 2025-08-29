package luna.task;
/**
 * Task with a description and completion status.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Task with the specified description and sets its status to not done.
    */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone(boolean status) {
        this.isDone = status;
    }
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
    public String taskView() {
        return String.format("[%s] %s", this.getStatusIcon(), description);
    }
}
