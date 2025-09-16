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
        
        assert description != null : "Description should not be null after super constructor";
        
        if (description == null || description.isBlank()) {
            throw new LunaException("Description cannot be empty");
        }
        this.taskType = "T";
        
        assert this.taskType.equals("T") : "ToDoTask should have task type 'T'";
        assert !description.isBlank() : "Description should not be blank after validation";
    }

    @Override
    public String toString() {
        return "[" + taskType + "] " + super.toString();
    }

    /**
     * Creates a copy of this ToDoTask
     */
    @Override
    public Task copy() {
        try {
            ToDoTask copy = new ToDoTask(this.description);
            copy.markDone(this.isDone);
            copy.taskType = this.taskType;
            return copy;
        } catch (LunaException e) {
            // This should not happen for valid existing tasks
            throw new RuntimeException("Failed to copy ToDoTask", e);
        }
    }
}
