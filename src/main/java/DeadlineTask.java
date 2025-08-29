import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Deadline task with a description, end date and task type.
 */
public class DeadlineTask extends ToDoTask {
    protected LocalDateTime endDateTime;
    protected LocalDate endDate;
    protected String originalEndTime;
    protected boolean hasTime;

    /**
     * Constructs a DeadlineTask from a single input string, splitting using ' /by '
     * Throws LunaException if description or endTime is missing
     */
    public DeadlineTask(String input) throws LunaException {
        super(parseDescription(input));
        this.taskType = "D";
        this.originalEndTime = parseEndTime(input);
        if (originalEndTime.isBlank()) {
            throw new LunaException("Please provide end time for deadline with /by");
        }
        parseDateTime(originalEndTime);
    }

    private static String parseDescription(String input) {
        int idx = input.indexOf(" /by ");
        return idx == -1 ? input : input.substring(0, idx);
    }

    private static String parseEndTime(String input) {
        int idx = input.indexOf(" /by ");
        return idx == -1 ? "" : input.substring(idx + 5);
    }

    /**
     * Parses date and time from string.
     * Sets either endDateTime (with time) or endDate (date only).
     */
    private void parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            this.hasTime = false;
            return;
        }

        String trimmed = dateTimeStr.trim();
        try {
            // Try to parse already formatted dates (for loading from file)
            // Format: "MMM dd yyyy, h:mma" (e.g., "Dec 02 2019, 6:00PM")
            if (trimmed.matches("\\w{3} \\d{2} \\d{4}, \\d{1,2}:\\d{2}[AP]M")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
                this.hasTime = true;
                return;
            }
            // Format: "MMM dd yyyy" (e.g., "Dec 02 2019")
            if (trimmed.matches("\\w{3} \\d{2} \\d{4}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                this.hasTime = false;
                return;
            }
            // Try yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                this.hasTime = true;
                return;
            }
            // Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
                this.hasTime = true;
                return;
            }
            // Try yyyy-mm-dd (date only)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                this.hasTime = false;
                return;
            }
            // Try d/M/yyyy (date only)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("d/M/yyyy"));
                this.hasTime = false;
                return;
            }
        } catch (DateTimeParseException e) {
            // Fall back to treating as plain text
        }
        // Could not parse as date/time, keep as original text
        this.hasTime = false;
    }

    @Override
    public String taskView() {
        if (hasTime && endDateTime != null) {
            // Format with time: "MMM dd yyyy, h:mma" (e.g., "Dec 02 2019, 6:00PM")
            String formattedDate = endDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
            return super.taskView() + " (by: " + formattedDate + ")";
        } else if (!hasTime && endDate != null) {
            // Format date only: "MMM dd yyyy" (e.g., "Dec 02 2019")
            String formattedDate = endDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
            return super.taskView() + " (by: " + formattedDate + ")";
        } else {
            // Fall back to original string if parsing failed
            return super.taskView() + " (by: " + originalEndTime + ")";
        }
    }
}
