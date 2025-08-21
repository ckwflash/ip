/**
 * Represents a Deadline task with a description, end date and task type.
 */
public class DeadlineTask extends ToDoTask {
    protected String endTime;

    /**
     * Constructs a DeadlineTask from a single input string, splitting using ' /by '
     * Throws LunaException if description or endTime is missing
     */
    public DeadlineTask(String input) throws LunaException {
        super(parseDescription(input));
        this.taskType = "D";
        this.endTime = parseEndTime(input);
        if (endTime.isBlank()) {
            throw new LunaException("Please provide end time for deadline with /by");
        }
    }

    private static String parseDescription(String input) {
        int idx = input.indexOf(" /by ");
        return idx == -1 ? input : input.substring(0, idx);
    }

    private static String parseEndTime(String input) {
        int idx = input.indexOf(" /by ");
        return idx == -1 ? "" : input.substring(idx + 5);
    }

    @Override
    public String taskView() {
        return super.taskView() + " (by: " + endTime + ")";
    }
}
