/**
 * Represents an event task with a start and end time.
 */
public class EventTask extends DeadlineTask {
    protected String startTime;


    /**
     * Constructs EventTask with the specified description, start time, and end time.
     */
    public EventTask(String description, String startTime, String endTime) {
        super(description, endTime);
        this.taskType = "E";
        this.startTime = startTime;
    }

    @Override
    public String taskView() {
        return "[" + taskType + "] "
            + String.format("[%s] %s", this.getStatusIcon(), description)
            + " (from: " + startTime + " to: " + endTime + ")";
    }
}
