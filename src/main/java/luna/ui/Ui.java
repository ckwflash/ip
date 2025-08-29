package luna.ui;
import java.util.ArrayList;
import java.util.Scanner;

import luna.task.Task;

/**
 * Deals with interactions with the user
 */
public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
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
        System.out.println(intro);
    }

    /**
     * Shows the goodbye message when the application exits
     */
    public void showGoodbye() {
        String goodbye =
            "____________________________________________________________\n"
                + " Goodbye! Hope to see you again\n"
                + "____________________________________________________________\n";
        System.out.println(goodbye);
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
        System.out.println(message);
        System.out.println();
    }

    /**
     * Shows the list of tasks to the user
     */
    public void showTaskList(ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).taskView());
        }
        System.out.println();
    }

    /**
     * Shows a message when a task is added
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" The following task has been added:");
        System.out.println("  " + task.taskView());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        System.out.println();
    }

    /**
     * Shows a message when a task is marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! This task has been marked as done:");
        System.out.println("  " + task.taskView());
        System.out.println();
    }

    /**
     * Shows a message when a task is unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, This task has been marked as not done yet:");
        System.out.println("  " + task.taskView());
        System.out.println();
    }

    /**
     * Shows a message when a task is deleted
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("The following task has been removed:");
        System.out.println("  " + task.taskView());
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println();
    }

    /**
     * Shows the search results to the user
     */
    public void showSearchResults(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i).taskView());
            }
        }
        System.out.println();
    }

    /**
     * Closes the scanner
     */
    public void close() {
        scanner.close();
    }
}
