package luna.task;
import luna.exception.LunaException;

/**
 * Represents a to-do task with a description and task type.
 */
public class ToDoTask extends Task {
    protected String taskType;

    /**
     * Constructs a default ToDoTask with the specified description
     * Throws LunaException if description is empty or blank
     */
    public ToDoTask(String description) throws LunaException {
        super(description);
        if (description == null || description.isBlank()) {
            throw new LunaException("Description cannot be empty");
        }
        this.taskType = "T";
    }

    @Override
    public String toString() {
        return "[" + taskType + "] " + super.toString();
    }
}
