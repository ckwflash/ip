/**
 * Represents a to-do task with a description and task type.
 */
public class ToDoTask extends Task {
    protected String taskType;

    /**
     * Constructs a defualt ToDoTask with the specified description
     */
    public ToDoTask(String description) {
        super(description);
        this.taskType = "T";
    }

    @Override
    public String taskView() {
        return "[" + taskType + "] " + super.taskView();
    }
}
