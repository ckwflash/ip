package luna.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import luna.exception.LunaException;
import luna.task.DeadlineTask;
import luna.task.EventTask;
import luna.task.Task;
import luna.task.ToDoTask;

/**
 * Deals with loading tasks from the file and saving tasks in the file
 */
public class Storage {
    private String filePath;

    /**
     * Constructor that creates a Storage instance with the specified file path
     */
    public Storage(String filePath) {
        assert filePath != null : "File path should not be null";
        assert !filePath.trim().isEmpty() : "File path should not be empty";

        this.filePath = filePath;

        assert this.filePath.equals(filePath) : "File path should be set correctly";
    }

    /**
     * Loads tasks from the storage file
     * @return ArrayList of tasks loaded from file
     */
    public ArrayList<Task> load() {
        assert filePath != null : "File path should not be null when loading";

        ArrayList<Task> tasks = new ArrayList<>();
        assert tasks != null : "Tasks list should be initialized";

        try {
            if (!Files.exists(Paths.get(filePath))) {
                assert tasks.isEmpty() : "Empty task list should be returned for non-existent file";
                return tasks; // Return empty list if no file exists
            }

            Scanner fileScanner = new Scanner(new File(filePath));
            assert fileScanner != null : "File scanner should be created successfully";

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                assert line != null : "Read line should not be null";

                Task task = parseTaskFromFile(line);
                // task can be null if parsing fails - this is expected behavior
                if (task != null) {
                    int oldSize = tasks.size();
                    tasks.add(task);
                    assert tasks.size() == oldSize + 1 : "Task should be added to list";
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        assert tasks != null : "Returned tasks list should never be null";
        return tasks;
    }

    /**
     * Saves tasks to the storage file
     * @param tasks ArrayList of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list should not be null when saving";
        assert filePath != null : "File path should not be null when saving";

        try {
            // Create data directory if it doesn't exist
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                assert created || parentDir.exists() : "Parent directory should exist after creation";
            }

            FileWriter writer = new FileWriter(filePath);
            assert writer != null : "File writer should be created successfully";

            for (Task task : tasks) {
                assert task != null : "Task in list should not be null";
                String toString = task.toString();
                assert toString != null : "Task view should not be null";
                writer.write(toString + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parses a task from toString format stored in file
     */
    private static Task parseTaskFromFile(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            assert line != null : "Line should not be null at this point";

            if (!line.startsWith("[") || line.length() < 8) {
                return null;
            }

            // Extract the content from "[T] [X] ..."
            char taskType = line.charAt(1);
            boolean isDone = line.charAt(5) == 'X';
            String content = line.substring(8);

            assert content != null : "Extracted content should not be null";

            Task task = null;
            switch (taskType) {
            case 'T':
                task = new ToDoTask(content);
                assert task != null : "ToDoTask should be created successfully";
                break;
            case 'D':
                task = parseDeadlineTask(content);
                assert task != null || task == null : "Task parsing result should be valid";
                break;
            case 'E':
                task = parseEventTask(content);
                assert task != null || task == null : "Task parsing result should be valid";
                break;
            default:
                return null;
            }

            if (task != null && isDone) {
                boolean wasNotDone = !task.isDone();
                task.markDone(true);
                assert task.isDone() : "Task should be marked as done";
                assert wasNotDone || task.isDone() : "Task status should be updated correctly";
            }

            // task can be null if parsing fails - this is expected
            return task;
        } catch (LunaException e) {
            // Skip invalid tasks
            return null;
        }
    }

    // AI-assisted refactoring: GitHub Copilot was used to help extract the parsing logic
    // for deadline and event tasks into separate helper methods to improve code readability
    // and follow the Single Level of Abstraction Principle (SLAP). This reduced the length
    // of the parseTaskFromFile method and made the code more maintainable.

    /**
     * Parses a deadline task from file content
     */
    private static Task parseDeadlineTask(String content) throws LunaException {
        int byIndex = content.lastIndexOf(" (by: ");
        if (byIndex != -1 && content.endsWith(")")) {
            String description = content.substring(0, byIndex);
            String date = content.substring(byIndex + 6, content.length() - 1);
            assert description != null : "Parsed description should not be null";
            assert date != null : "Parsed date should not be null";
            return new DeadlineTask(description + " /by " + date);
        }
        return null;
    }

    /**
     * Parses an event task from file content
     */
    private static Task parseEventTask(String content) throws LunaException {
        int fromIndex = content.lastIndexOf(" (from: ");
        if (fromIndex != -1 && content.endsWith(")")) {
            String description = content.substring(0, fromIndex);
            String timeInfo = content.substring(fromIndex + 8, content.length() - 1);
            int toIndex = timeInfo.lastIndexOf(" to: ");
            if (toIndex != -1) {
                String startTime = timeInfo.substring(0, toIndex);
                String endTime = timeInfo.substring(toIndex + 5);
                assert description != null : "Parsed description should not be null";
                assert startTime != null : "Parsed start time should not be null";
                assert endTime != null : "Parsed end time should not be null";
                return new EventTask(description + " /from " + startTime + " /to " + endTime);
            }
        }
        return null;
    }
}
