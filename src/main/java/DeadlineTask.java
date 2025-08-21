/**
 * Represents a Deadline task with a description, end date and task type.
 */
public class DeadlineTask extends ToDoTask {
    protected String endTime;

    /**
     * Constructs a ToDoTask with the specified description
     */
    public DeadlineTask(String description, String endTime) {
        super(description);
        this.taskType = "D";
        this.endTime = endTime;
    }

    @Override
    public String taskView() {
        return super.taskView() + " (by: " + endTime + ")";
    }
}
