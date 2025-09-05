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
    }

    /**
     * Helper method to either print or capture output
     */
    private void addOutput(String text) {
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
        String result = output.toString().trim();
        output.setLength(0);
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
                addOutput((i + 1) + ". " + tasks.get(i).taskView() + "\n");
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
        addOutput("  " + task.taskView() + "\n");
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
        addOutput("  " + task.taskView());
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows a message when a task is unmarked
     */
    public void showTaskUnmarked(Task task) {
        addOutput("OK, This task has been marked as not done yet:\n");
        addOutput("  " + task.taskView());
        if (!captureMode) {
            addOutput("\n\n");
        }
    }

    /**
     * Shows a message when a task is deleted
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        addOutput("The following task has been removed:\n");
        addOutput("  " + task.taskView() + "\n");
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
                addOutput(" " + (i + 1) + "." + matchingTasks.get(i).taskView() + "\n");
            }
        }
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
