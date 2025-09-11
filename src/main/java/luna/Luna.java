package luna;
import java.util.ArrayList;

import luna.exception.LunaException;
import luna.parser.ParsedCommand;
import luna.parser.Parser;
import luna.storage.Storage;
import luna.task.DeadlineTask;
import luna.task.EventTask;
import luna.task.Task;
import luna.task.TaskList;
import luna.task.ToDoTask;
import luna.ui.Ui;

/**
 * Application entry point and main logic handler
 */
public class Luna {
    private static final String DATA_FILE_PATH = "./data/luna.txt";
    private TaskList tasks;
    private Storage storage;

    /**
     * Constructor for GUI and instance usage
     */
    public Luna() {
        this.storage = new Storage(DATA_FILE_PATH);
        assert storage != null : "Storage should be successfully initialized";
        
        ArrayList<Task> loadedTasks = storage.load();
        assert loadedTasks != null : "Storage.load() should never return null, even for empty lists";
        
        this.tasks = new TaskList(loadedTasks);
        assert tasks != null : "TaskList should be successfully initialized";
    }

    /**
     * Processes a command and returns the response as a string
     * Used for GUI integration
     */
    public String getResponse(String input) {
        assert input != null : "Input command should not be null";

        try {
            Ui ui = new Ui(true); // true for capture mode
            assert ui != null : "Ui should be successfully created in capture mode";

            ParsedCommand parsedCommand = Parser.parse(input);
            assert parsedCommand != null : "Parser should never return null ParsedCommand";

            if (parsedCommand.isExit()) {
                ui.showGoodbye();
                String output = ui.getOutput();
                assert output != null : "UI output should never be null";
                return output;
            }

            executeCommand(parsedCommand, tasks, ui, storage);
            String output = ui.getOutput();
            assert output != null : "UI output should never be null after command execution";
            return output;

        } catch (LunaException e) {
            assert e.getMessage() != null : "LunaException should have a non-null message";
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String indexStr, TaskList tasks, boolean markDone, Ui ui) throws LunaException {
        assert indexStr != null : "Index string should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number");
        }
        
        assert index >= -1 : "Parsed index should be at least -1 (which will fail bounds check)";
        
        tasks.markTask(index, markDone);
        Task task = tasks.get(index);
        assert task != null : "Task should exist after successful marking";
        
        if (markDone) {
            assert task.isDone() : "Task should be marked as done after marking";
            ui.showTaskMarked(task);
        } else {
            assert !task.isDone() : "Task should be marked as not done after unmarking";
            ui.showTaskUnmarked(task);
        }
    }

    /**
     * Executes the given parsed command
     */
    private static void executeCommand(ParsedCommand parsedCommand, TaskList tasks,
        Ui ui, Storage storage) throws LunaException {
        assert parsedCommand != null : "ParsedCommand should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";
        assert parsedCommand.getCommandType() != null : "Command type should not be null";
        
        int initialTaskCount = tasks.size();
        
        switch (parsedCommand.getCommandType()) {
        case "list":
            ui.showTaskList(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "List command should not change task count";
            break;
        case "mark":
            markCommand(parsedCommand.getArguments(), tasks, true, ui);
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "Mark command should not change task count";
            break;
        case "unmark":
            markCommand(parsedCommand.getArguments(), tasks, false, ui);
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "Unmark command should not change task count";
            break;
        case "delete":
            deleteCommand(parsedCommand.getArguments(), tasks, ui);
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount - 1 : "Delete command should reduce task count by 1";
            break;
        case "todo":
            Task todo = new ToDoTask(parsedCommand.getArguments());
            assert todo != null : "ToDoTask should be successfully created";
            tasks.add(todo);
            ui.showTaskAdded(todo, tasks.size());
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount + 1 : "Todo command should increase task count by 1";
            break;
        case "deadline":
            Task deadline = new DeadlineTask(parsedCommand.getArguments());
            assert deadline != null : "DeadlineTask should be successfully created";
            tasks.add(deadline);
            ui.showTaskAdded(deadline, tasks.size());
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount + 1 : "Deadline command should increase task count by 1";
            break;
        case "event":
            Task event = new EventTask(parsedCommand.getArguments());
            assert event != null : "EventTask should be successfully created";
            tasks.add(event);
            ui.showTaskAdded(event, tasks.size());
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount + 1 : "Event command should increase task count by 1";
            break;
        case "find":
            findCommand(parsedCommand.getArguments(), tasks, ui);
            assert tasks.size() == initialTaskCount : "Find command should not change task count";
            break;
        default:
            throw new LunaException("Unknown command type");
        }
    }

    /**
     * Deletes a task from the task list
     */
    private static void deleteCommand(String indexStr, TaskList tasks, Ui ui) throws LunaException {
        assert indexStr != null : "Index string should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number to delete");
        }
        
        assert index >= -1 : "Parsed index should be at least -1 (which will fail bounds check)";
        
        Task removed = tasks.deleteTask(index);
        assert removed != null : "Deleted task should not be null";
        
        ui.showTaskDeleted(removed, tasks.size());
    }

    /**
     * Finds tasks that contain the given keyword
     */
    private static void findCommand(String keyword, TaskList tasks, Ui ui) {
        assert keyword != null : "Search keyword should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        assert matchingTasks != null : "findTasks should never return null, even for empty results";
        
        ui.showSearchResults(matchingTasks);
    }
}
