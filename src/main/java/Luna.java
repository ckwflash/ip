import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Application entry point
 */
public class Luna {
    private static final String DATA_FILE_PATH = "./data/duke.txt";
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        // Load tasks from file at startup
        loadTasks(tasks);

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
        while (scanner.hasNextLine()) {
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
                            saveTasks(tasks);
                        } else {
                            throw new LunaException("Please provide a task number to mark");
                        }
                        System.out.println();
                        break;
                    case "unmark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, false);
                            saveTasks(tasks);
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
                            saveTasks(tasks);
                            System.out.println();
                        } else {
                            throw new LunaException("Please give a task number to delete");
                        }
                        break;
                    case "todo":
                        Task todo = new ToDoTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(todo);
                        printAddTaskMsg(todo, tasks.size());
                        saveTasks(tasks);
                        break;
                    case "deadline":
                        Task deadline = new DeadlineTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(deadline);
                        printAddTaskMsg(deadline, tasks.size());
                        saveTasks(tasks);
                        break;
                    case "event":
                        Task event = new EventTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(event);
                        printAddTaskMsg(event, tasks.size());
                        saveTasks(tasks);
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
     * Saves tasks to the data file
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            FileWriter writer = new FileWriter(DATA_FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.taskView() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file
     */
    private static void loadTasks(ArrayList<Task> tasks) {
        try {
            if (!Files.exists(Paths.get(DATA_FILE_PATH))) {
                return; // No file to load from
            }

            Scanner fileScanner = new Scanner(new File(DATA_FILE_PATH));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Task task = parseTaskFromFile(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Parses a task from taskView format
     */
    private static Task parseTaskFromFile(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {

            if (!line.startsWith("[") || line.length() < 8) {
                return null;
            }

            // Extract the content from "[T] [X] ..."
            char taskType = line.charAt(1);
            boolean isDone = line.charAt(5) == 'X';
            String content = line.substring(8);

            Task task = null;
            switch (taskType) {
            case 'T':
                task = new ToDoTask(content);
                break;
            case 'D':
                // Format: description (by: date)
                int byIndex = content.lastIndexOf(" (by: ");
                if (byIndex != -1 && content.endsWith(")")) {
                    String description = content.substring(0, byIndex);
                    String date = content.substring(byIndex + 6, content.length() - 1);
                    task = new DeadlineTask(description + " /by " + date);
                }
                break;
            case 'E':
                // Format: description (from: start to: end)
                int fromIndex = content.lastIndexOf(" (from: ");
                if (fromIndex != -1 && content.endsWith(")")) {
                    String description = content.substring(0, fromIndex);
                    String timeInfo = content.substring(fromIndex + 8, content.length() - 1);
                    int toIndex = timeInfo.lastIndexOf(" to: ");
                    if (toIndex != -1) {
                        String startTime = timeInfo.substring(0, toIndex);
                        String endTime = timeInfo.substring(toIndex + 5);
                        task = new EventTask(description + " /from " + startTime + " /to " + endTime);
                    }
                }
                break;
            default:
                return null;
            }

            if (task != null && isDone) {
                task.markDone(true);
            }
            return task;
        } catch (LunaException e) {
            // Skip invalid tasks
            return null;
        }
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
