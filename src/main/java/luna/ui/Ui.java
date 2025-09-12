package luna.ui;
import java.util.ArrayList;
import java.util.Scanner;

import luna.task.Task;

/**
 * Deals with interactions with the user
 */
public class Ui {
    private Scanner scanner;
    private StringBuilder output;
    private boolean captureMode;

    /**
     * Default constructor for console mode
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.output = new StringBuilder();
        this.captureMode = false;
    }

    /**
     * Constructor for string capture mode (used by GUI)
     */
    public Ui(boolean captureMode) {
        this.scanner = null;
        this.output = new StringBuilder();
        this.captureMode = captureMode;

        assert this.output != null : "Output buffer should be initialized";
        assert this.captureMode == captureMode : "Capture mode should be set correctly";

        if (captureMode) {
            assert this.scanner == null : "Scanner should be null in capture mode";
        }
    }

    /**
     * Helper method to either print or capture output
     */
    private void addOutput(String text) {
        assert text != null : "Output text should not be null";
        assert output != null : "Output buffer should not be null";

        if (captureMode) {
            output.append(text);
        } else {
            System.out.print(text);
        }
    }

    /**
     * Gets the captured output and clears the buffer
     */
    public String getOutput() {
        assert captureMode : "getOutput should only be called in capture mode";
        assert output != null : "Output buffer should not be null";

        String result = output.toString().trim();
        assert result != null : "Result should not be null";

        output.setLength(0);
        assert output.length() == 0 : "Output buffer should be cleared";

        return result;
    }

    /**
     * Shows the welcome message when the application starts
     */
    public void showWelcome() {
        String intro =
            "____________________________________________________________\n"
                + " Hello, nice to meet you! I'm Luna\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        addOutput(intro);
    }

    /**
     * Shows the goodbye message when the application exits
     */
    public void showGoodbye() {
        String goodbye =
            "____________________________________________________________\n"
                + " Goodbye! Hope to see you again\n"
                + "____________________________________________________________\n";
        addOutput(goodbye);
    }

    /**
     * Reads the next line of user input
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Checks if there is more input available
     */
    public boolean hasMoreInput() {
        return scanner.hasNextLine();
    }

    /**
     * Shows an error message to the user
     */
    public void showError(String message) {
        addOutput(message + "\n\n");
    }

    /**
     * Shows the list of tasks to the user
     */
    public void showTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            addOutput("No tasks in your list yet!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                addOutput((i + 1) + ". " + tasks.get(i).toString() + "\n");
            }
        }
        if (!captureMode) {
            addOutput("\n");
        }
    }

    /**
     * Shows a message when a task is added
     */
    public void showTaskAdded(Task task, int totalTasks) {
        addOutput(" The following task has been added:\n");
        addOutput("  " + task.toString() + "\n");
        addOutput(" Now you have " + totalTasks + " tasks in the list.");
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows a message when a task is marked as done
     */
    public void showTaskMarked(Task task) {
        addOutput("Nice! This task has been marked as done:\n");
        addOutput("  " + task.toString());
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows a message when a task is unmarked
     */
    public void showTaskUnmarked(Task task) {
        addOutput("OK, This task has been marked as not done yet:\n");
        addOutput("  " + task.toString());
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows a message when a task is deleted
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        addOutput("The following task has been removed:\n");
        addOutput("  " + task.toString() + "\n");
        addOutput("Now you have " + totalTasks + " tasks in the list.");
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows the search results to the user
     */
    public void showSearchResults(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            addOutput("No matching tasks found.");
        } else {
            addOutput("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                addOutput(" " + (i + 1) + "." + matchingTasks.get(i).toString() + "\n");
            }
        }
        if (!captureMode) {
            addOutput("\n");
        }
    }

    /**
     * Shows undo success message
     */
    public void showUndoSuccess() {
        addOutput("Undo successful! Reverted the last command.\n");
        if (!captureMode) {
            addOutput("\n");
        }
    }

    /**
     * Closes the scanner
     */
    public void close() {
        scanner.close();
    }
}
