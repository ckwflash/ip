package luna.task;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import luna.exception.LunaException;

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

        assert input != null : "Input should not be null";

        this.taskType = "D";
        this.originalEndTime = parseEndTime(input);

        assert originalEndTime != null : "Parsed end time should not be null";
        assert this.taskType.equals("D") : "DeadlineTask should have task type 'D'";

        if (originalEndTime.isBlank()) {
            throw new LunaException("Please provide end time for deadline with /by");
        }

        assert !originalEndTime.isBlank() : "End time should not be blank after validation";

        parseDateTime(originalEndTime);

        // At least one of endDateTime or endDate should be set, or we fall back to original
        assert endDateTime != null || endDate != null || originalEndTime != null
            : "At least original end time should be preserved";
    }

    private static String parseDescription(String input) {
        assert input != null : "Input should not be null when parsing description";

        int idx = input.indexOf(" /by ");
        String description = idx == -1 ? input : input.substring(0, idx);

        assert description != null : "Parsed description should not be null";

        // Remove only trailing whitespace, preserve leading spaces as per test expectations
        return description.replaceAll("\\s+$", "");
    }

    private static String parseEndTime(String input) {
        assert input != null : "Input should not be null when parsing end time";

        int idx = input.indexOf(" /by ");
        String endTime = idx == -1 ? "" : input.substring(idx + 5);

        assert endTime != null : "Parsed end time should not be null";

        return endTime;
    }

    /**
     * Parses date and time from string.
     * Sets either endDateTime (with time) or endDate (date only).
     */
    private void parseDateTime(String dateTimeStr) {
        assert dateTimeStr != null : "DateTime string should not be null";
        
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            this.hasTime = false;
            return;
        }

        String trimmed = dateTimeStr.trim();
        assert trimmed != null : "Trimmed string should not be null";
        assert !trimmed.isEmpty() : "Trimmed string should not be empty";
        
        try {
            // Try to parse already formatted dates (for loading from file)
            // Format: "MMM dd yyyy, h:mma" (e.g., "Dec 02 2019, 6:00PM")
            if (trimmed.matches("\\w{3} \\d{2} \\d{4}, \\d{1,2}:\\d{2}[AP]M")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
                this.hasTime = true;
                assert endDateTime != null : "Parsed endDateTime should not be null";
                assert hasTime : "hasTime should be true when endDateTime is set";
                return;
            }
            // Format: "MMM dd yyyy" (e.g., "Dec 02 2019")
            if (trimmed.matches("\\w{3} \\d{2} \\d{4}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                this.hasTime = false;
                assert endDate != null : "Parsed endDate should not be null";
                assert !hasTime : "hasTime should be false when only endDate is set";
                return;
            }
            // Try yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                this.hasTime = true;
                assert endDateTime != null : "Parsed endDateTime should not be null";
                assert hasTime : "hasTime should be true when endDateTime is set";
                return;
            }
            // Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                this.endDateTime = LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
                this.hasTime = true;
                assert endDateTime != null : "Parsed endDateTime should not be null";
                assert hasTime : "hasTime should be true when endDateTime is set";
                return;
            }
            // Try yyyy-mm-dd (date only)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                this.hasTime = false;
                assert endDate != null : "Parsed endDate should not be null";
                assert !hasTime : "hasTime should be false when only endDate is set";
                return;
            }
            // Try d/M/yyyy (date only)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                this.endDate = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("d/M/yyyy"));
                this.hasTime = false;
                assert endDate != null : "Parsed endDate should not be null";
                assert !hasTime : "hasTime should be false when only endDate is set";
                return;
            }
        } catch (DateTimeParseException e) {
            // Fall back to treating as plain text
        }
        // Could not parse as date/time, keep as original text
        this.hasTime = false;
        assert !hasTime : "hasTime should be false when parsing fails";
    }

    @Override
    public String taskView() {
        assert originalEndTime != null : "Original end time should not be null";
        
        String superView = super.taskView();
        assert superView != null : "Super class task view should not be null";
        
        if (hasTime && endDateTime != null) {
            // Format with time: "MMM dd yyyy, h:mma" (e.g., "Dec 02 2019, 6:00PM")
            String formattedDate = endDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
            assert formattedDate != null : "Formatted date should not be null";
            
            String result = superView + " (by: " + formattedDate + ")";
            assert result.contains("(by: ") : "Task view should contain deadline indicator";
            return result;
        } else if (!hasTime && endDate != null) {
            // Format date only: "MMM dd yyyy" (e.g., "Dec 02 2019")
            String formattedDate = endDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
            assert formattedDate != null : "Formatted date should not be null";
            
            String result = superView + " (by: " + formattedDate + ")";
            assert result.contains("(by: ") : "Task view should contain deadline indicator";
            return result;
        } else {
            // Fall back to original string if parsing failed
            String result = superView + " (by: " + originalEndTime + ")";
            assert result.contains("(by: ") : "Task view should contain deadline indicator";
            assert result.contains(originalEndTime) : "Task view should contain original end time";
            return result;
        }
    }

    /**
     * Creates a copy of this DeadlineTask
     */
    @Override
    public Task copy() {
        try {
            // Reconstruct the original input string
            String originalInput = this.description + " /by " + this.originalEndTime;
            DeadlineTask copy = new DeadlineTask(originalInput);
            copy.markDone(this.isDone);
            return copy;
        } catch (LunaException e) {
            // This should not happen for valid existing tasks
            throw new RuntimeException("Failed to copy DeadlineTask", e);
        }
    }
}
