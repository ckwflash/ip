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
                    if (parts.length > 1) {
                        markCommand(parts[1], tasks, count, true);
                    }
                    System.out.println();
                    break;
                case "unmark":
                    if (parts.length > 1) {
                        markCommand(parts[1], tasks, count, false);
                    }
                    System.out.println();
                    break;
                case "todo":
                    Task todo = new ToDoTask(input);
                    tasks[count] = todo;
                    count++;
                    printAddTaskMsg(todo, count);
                    break;
                case "deadline":
                    String[] deadlineParts = parts[1].split(" /by ", 2);
                    tasks[count] = new DeadlineTask(deadlineParts[0], deadlineParts[1]);
                    count++;
                    printAddTaskMsg(tasks[count - 1], count);
                    break;
                case "event":
                    String[] eventParts = parts[1].split(" /from | /to ");
                    tasks[count] = new EventTask(
                        eventParts[0],
                        eventParts[1],
                        eventParts[2]
                    );
                    count++;
                    printAddTaskMsg(tasks[count - 1], count);
                    break;
                default:
                    tasks[count] = new Task(input);
                    count++;
                    System.out.println("added: " + input);
                    System.out.println();
                    break;
                }
            }
        }

        System.out.println(goodbye);
        scanner.close();
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String indexStr, Task[] tasks, int count, boolean markDone) {
        int index = Integer.parseInt(indexStr) - 1;
        if (index >= 0 && index < count) {
            tasks[index].markDone(markDone);
            if (markDone) {
                System.out.println("Nice! This task has been marked as done:");
                System.out.println("  " + tasks[index].taskView());
            } else {
                System.out.println("OK, This task has been marked as not done yet:");
                System.out.println("  " + tasks[index].taskView());
            }
        }
    }

    private static void printAddTaskMsg(Task task, int count) {
        System.out.println(" Got it. The following task has been added:");
        System.out.println("  " + task.taskView());
        System.out.println(" Now you have " + count + " tasks in the list.");
        System.out.println();
    }
}
