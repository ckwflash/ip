import java.util.ArrayList;
import java.util.Scanner;
/**
 * Application entry point
 */
public class Luna {
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
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

        String input = "";
        while (true) {
            try {
                input = scanner.nextLine();
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i).taskView());
                    }
                    System.out.println();
                } else {
                    String[] parts = input.split(" ", 2);
                    String command = parts[0];
                    switch (command) {
                    case "mark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, true);
                        } else {
                            throw new LunaException("Please provide a task number to mark");
                        }
                        System.out.println();
                        break;
                    case "unmark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, false);
                        } else {
                            throw new LunaException("Please provide a task number to unmark");
                        }
                        System.out.println();
                        break;
                    case "delete":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            int index;
                            try {
                                index = Integer.parseInt(parts[1]) - 1;
                            } catch (NumberFormatException e) {
                                throw new LunaException("Please give a valid task number to delete");
                            }
                            if (index < 0 || index >= tasks.size()) {
                                throw new LunaException("Task index is out of bounds");
                            }
                            Task removed = tasks.remove(index);
                            System.out.println("The following task has been removed:");
                            System.out.println("  " + removed.taskView());
                            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            System.out.println();
                        } else {
                            throw new LunaException("Please give a task number to delete");
                        }
                        break;
                    case "todo":
                        Task todo = new ToDoTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(todo);
                        printAddTaskMsg(todo, tasks.size());
                        break;
                    case "deadline":
                        Task deadline = new DeadlineTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(deadline);
                        printAddTaskMsg(deadline, tasks.size());
                        break;
                    case "event":
                        Task event = new EventTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(event);
                        printAddTaskMsg(event, tasks.size());
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
    private static void markCommand(String indexStr, ArrayList<Task> tasks, boolean markDone) throws LunaException {
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        tasks.get(index).markDone(markDone);
        if (markDone) {
            System.out.println("Nice! This task has been marked as done:");
            System.out.println("  " + tasks.get(index).taskView());
        } else {
            System.out.println("OK, This task has been marked as not done yet:");
            System.out.println("  " + tasks.get(index).taskView());
        }
    }

    private static void printAddTaskMsg(Task task, int count) {
        System.out.println(" The following task has been added:");
        System.out.println("  " + task.taskView());
        System.out.println(" Now you have " + count + " tasks in the list.");
        System.out.println();
    }
}
