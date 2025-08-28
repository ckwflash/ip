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
        this.taskType = "E";
        this.startTime = parseStartTime(input);
        if (startTime.isBlank()) {
            throw new LunaException("Please provide start time and /from for event");
        }
        this.endTime = parseEndTime(input);
        if (endTime.isBlank()) {
            throw new LunaException("Please provide end time and /to for event");
        }
    }

    private static String parseDescription(String input) {
        int fromIdx = input.indexOf(" /from ");
        return fromIdx == -1 ? input : input.substring(0, fromIdx);
    }

    private static String parseStartTime(String input) {
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
        return candidate.trim();
    }

    private static String parseEndTime(String input) {
        int toIdx = input.indexOf(" /to ");
        if (toIdx != -1) {
            return input.substring(toIdx + 5).trim();
        }
        // Check if input ends with " /to" (without trailing space after trimming)
        if (input.endsWith(" /to")) {
            return "";
        }
        return "";
    }

    @Override
    public String taskView() {
        return super.taskView() + " (from: " + startTime + " to: " + endTime + ")";
    }
}
