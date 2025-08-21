/**
 * Application entry point
 */

import java.util.Scanner; 

public class Luna {
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int count = 0;

        String intro =
            "____________________________________________________________\n"
                + " Hello, nice to meet you! I'm Luna\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        String goodbye =
            "____________________________________________________________\n"
                + " Goodbye! Hope to see you again\n"
                + "____________________________________________________________\n";
        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);
        // scanner needs to be used as because System.console() will always return null when input is redirected from a file
        String input = "";
        while (true) {
            try {
                input = scanner.nextLine();
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    for (int i = 0; i < count; i++) {
                        System.out.println((i + 1) + ". " + tasks[i].taskView());
                    }
                    System.out.println();
                } else {
                    String[] parts = input.split(" ", 2);
                    String command = parts[0];
                    switch (command) {
                    case "mark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, count, true);
                        } else {
                            throw new LunaException("Please provide a task number to mark.");
                        }
                        System.out.println();
                        break;
                    case "unmark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, count, false);
                        } else {
                            throw new LunaException("Please provide a task number to unmark.");
                        }
                        System.out.println();
                        break;
                    case "todo":
                        Task todo = new ToDoTask(parts.length > 1 ? parts[1] : "");
                        tasks[count] = todo;
                        count++;
                        printAddTaskMsg(todo, count);
                        break;
                    case "deadline":
                        Task deadline = new DeadlineTask(parts.length > 1 ? parts[1] : "");
                        tasks[count] = deadline;
                        count++;
                        printAddTaskMsg(deadline, count);
                        break;
                    case "event":
                        Task event = new EventTask(parts.length > 1 ? parts[1] : "");
                        tasks[count] = event;
                        count++;
                        printAddTaskMsg(event, count);
                        break;
                    default:
                        throw new LunaException("Sorry! I dont gets");
                    }
                }
            } catch (LunaException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }

        System.out.println(goodbye);
        scanner.close();
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String indexStr, Task[] tasks, int count, boolean markDone) throws LunaException {
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number");
        }
        if (index < 0 || index >= count) {
            throw new LunaException("Task index is out of bounds");
        }
        tasks[index].markDone(markDone);
        if (markDone) {
            System.out.println("Nice! This task has been marked as done:");
            System.out.println("  " + tasks[index].taskView());
        } else {
            System.out.println("OK, This task has been marked as not done yet:");
            System.out.println("  " + tasks[index].taskView());
        }
    }

    private static void printAddTaskMsg(Task task, int count) {
        System.out.println(" Got it. The following task has been added:");
        System.out.println("  " + task.taskView());
        System.out.println(" Now you have " + count + " tasks in the list.");
        System.out.println();
    }
}
