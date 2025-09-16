package luna.task;
import luna.exception.LunaException;

/**
 * Represents an event task with a start and end time.
 */
public class EventTask extends ToDoTask {
    protected String startTime;
    protected String endTime;

    /**
     * Constructs EventTask from a single input string, splitting using ' /from ' and ' /to '
     * Throws LunaException if description, startTime, or endTime is missing
     */
    public EventTask(String input) throws LunaException {
        super(parseDescription(input));

        assert input != null : "Input should not be null";

        this.taskType = "E";
        this.startTime = parseStartTime(input);

        assert startTime != null : "Parsed start time should not be null";
        assert this.taskType.equals("E") : "EventTask should have task type 'E'";

        if (startTime.isBlank()) {
            throw new LunaException("Please provide start time and /from for event");
        }

        this.endTime = parseEndTime(input);
        assert endTime != null : "Parsed end time should not be null";

        if (endTime.isBlank()) {
            throw new LunaException("Please provide end time and /to for event");
        }

        assert !startTime.isBlank() : "Start time should not be blank after validation";
        assert !endTime.isBlank() : "End time should not be blank after validation";
    }

    private static String parseDescription(String input) {
        assert input != null : "Input should not be null when parsing description";

        int fromIdx = input.indexOf(" /from ");
        String description = fromIdx == -1 ? input : input.substring(0, fromIdx);

        assert description != null : "Parsed description should not be null";

        return description;
    }

    private static String parseStartTime(String input) {
        assert input != null : "Input should not be null when parsing start time";

        int fromIdx = input.indexOf(" /from ");
        int toIdx = input.indexOf(" /to ");
        if (fromIdx == -1) {
            return "";
        }
        // If no " /to " found, check if it ends with " /to"
        if (toIdx == -1) {
            if (input.endsWith(" /to")) {
                toIdx = input.length() - 4; // Position of " /to"
            } else {
                return "";
            }
        }
        if (toIdx < fromIdx || toIdx < fromIdx + 7) {
            return "";
        }
        String candidate = input.substring(fromIdx + 7, toIdx);
        String result = candidate.trim();

        assert result != null : "Parsed start time should not be null";

        return result;
    }

    private static String parseEndTime(String input) {
        assert input != null : "Input should not be null when parsing end time";

        int toIdx = input.indexOf(" /to ");
        if (toIdx != -1) {
            String result = input.substring(toIdx + 5).trim();
            assert result != null : "Parsed end time should not be null";
            return result;
        }
        // Check if input ends with " /to" (without trailing space after trimming)
        if (input.endsWith(" /to")) {
            return "";
        }
        return "";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + startTime + " to: " + endTime + ")";
    }

    /**
     * Creates a copy of this EventTask
     */
    @Override
    public Task copy() {
        try {
            // Reconstruct the original input string
            String originalInput = this.description + " /from " + this.startTime + " /to " + this.endTime;
            EventTask copy = new EventTask(originalInput);
            copy.markDone(this.isDone);
            return copy;
        } catch (LunaException e) {
            // This should not happen for valid existing tasks
            throw new RuntimeException("Failed to copy EventTask", e);
        }
    }
}
