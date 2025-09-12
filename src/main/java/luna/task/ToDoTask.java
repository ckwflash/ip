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
    public String taskView() {
        assert taskType != null : "Task type should not be null";
        assert taskType.length() == 1 : "Task type should be a single character";
        assert taskType.equals("T") || taskType.equals("D") || taskType.equals("E")
            : "Task type should be one of T, D, or E";

        String superView = super.taskView();
        assert superView != null : "Super class task view should not be null";

        String view = "[" + taskType + "] " + superView;
        assert view != null : "Task view should never be null";
        assert view.startsWith("[" + taskType + "]") : "Task view should start with task type in brackets";

        return view;
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
