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
    
    // Undo functionality - simple single-level undo
    private TaskList previousTaskState;
    private String lastCommand;

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
     * Constructor for testing with custom file path
     */
    public Luna(String dataFilePath) {
        this.storage = new Storage(dataFilePath);
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

            executeCommand(parsedCommand, ui);
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
    private void markCommand(String indexStr, boolean markDone, Ui ui) throws LunaException {
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
    private void executeCommand(ParsedCommand parsedCommand, Ui ui) throws LunaException {
        assert parsedCommand != null : "ParsedCommand should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";
        assert parsedCommand.getCommandType() != null : "Command type should not be null";

        String commandType = parsedCommand.getCommandType();

        // Save state before modifying commands (but not for undo itself)
        if (!commandType.equals("undo")) {
            saveStateForUndo(commandType);
        }

        int initialTaskCount = tasks.size();

        switch (commandType) {
        case "list":
            ui.showTaskList(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "List command should not change task count";
            break;
        case "undo":
            undoLastCommand(ui);
            storage.save(tasks.getTasks());
            break;
        case "mark":
            markCommand(parsedCommand.getArguments(), true, ui);
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "Mark command should not change task count";
            break;
        case "unmark":
            markCommand(parsedCommand.getArguments(), false, ui);
            storage.save(tasks.getTasks());
            assert tasks.size() == initialTaskCount : "Unmark command should not change task count";
            break;
        case "delete":
            deleteCommand(parsedCommand.getArguments(), ui);
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
            findCommand(parsedCommand.getArguments(), ui);
            assert tasks.size() == initialTaskCount : "Find command should not change task count";
            break;
        default:
            throw new LunaException("Unknown command type");
        }
    }

    /**
     * Deletes a task from the task list
     */
    private void deleteCommand(String indexStr, Ui ui) throws LunaException {
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
    private void findCommand(String keyword, Ui ui) {
        assert keyword != null : "Search keyword should not be null";
        assert tasks != null : "TaskList should not be null";
        assert ui != null : "Ui should not be null";

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        assert matchingTasks != null : "findTasks should never return null, even for empty results";

        ui.showSearchResults(matchingTasks);
    }

    /**
     * Saves the current state for undo functionality
     */
    private void saveStateForUndo(String commandType) {
        assert tasks != null : "Tasks should not be null when saving state";
        assert commandType != null : "Command type should not be null";

        // Only save state for commands that modify the task list
        if (isModifyingCommand(commandType)) {
            this.previousTaskState = tasks.copy();
            this.lastCommand = commandType;
            assert previousTaskState != null : "Previous task state should be saved";
            assert lastCommand != null : "Last command should be saved";
        }
    }

    /**
     * Checks if a command type modifies the task list
     */
    private boolean isModifyingCommand(String commandType) {
        assert commandType != null : "Command type should not be null";

        return commandType.equals("mark") || commandType.equals("unmark") ||
               commandType.equals("delete") || commandType.equals("todo") ||
               commandType.equals("deadline") || commandType.equals("event");
    }

    /**
     * Undoes the last command if possible
     */
    private void undoLastCommand(Ui ui) throws LunaException {
        assert ui != null : "Ui should not be null";

        if (previousTaskState == null) {
            throw new LunaException("No previous command to undo");
        }

        assert lastCommand != null : "Last command should not be null if previous state exists";

        // Restore the previous state
        this.tasks = previousTaskState.copy();
        assert tasks != null : "Tasks should be restored after undo";

        // Clear undo state to prevent double undo
        this.previousTaskState = null;
        this.lastCommand = null;

        ui.showUndoSuccess();
    }
}
