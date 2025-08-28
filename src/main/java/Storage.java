import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with loading tasks from the file and saving tasks in the file
 */
public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file
     * @return ArrayList of tasks loaded from file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return tasks; // Return empty list if no file exists
            }

            Scanner fileScanner = new Scanner(new File(filePath));
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
        return tasks;
    }

    /**
     * Saves tasks to the storage file
     * @param tasks ArrayList of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
        try {
            // Create data directory if it doesn't exist
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                writer.write(task.taskView() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parses a task from taskView format stored in file
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
}
