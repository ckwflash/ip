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
        ArrayList<Task> loadedTasks = storage.load();
        this.tasks = new TaskList(loadedTasks);
    }

    /**
     * Processes a command and returns the response as a string
     * Used for GUI integration
     */
    public String getResponse(String input) {
        try {
            Ui ui = new Ui(true); // true for capture mode
            
            ParsedCommand parsedCommand = Parser.parse(input);
            if (parsedCommand.isExit()) {
                ui.showGoodbye();
                return ui.getOutput();
            }

            executeCommand(parsedCommand, tasks, ui, storage);
            return ui.getOutput();

        } catch (LunaException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String indexStr, TaskList tasks, boolean markDone, Ui ui) throws LunaException {
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number");
        }
        tasks.markTask(index, markDone);
        if (markDone) {
            ui.showTaskMarked(tasks.get(index));
        } else {
            ui.showTaskUnmarked(tasks.get(index));
        }
    }

    /**
     * Executes the given parsed command
     */
    private static void executeCommand(ParsedCommand parsedCommand, TaskList tasks,
        Ui ui, Storage storage) throws LunaException {
        switch (parsedCommand.getCommandType()) {
        case "list":
            ui.showTaskList(tasks.getTasks());
            break;
        case "mark":
            markCommand(parsedCommand.getArguments(), tasks, true, ui);
            storage.save(tasks.getTasks());
            break;
        case "unmark":
            markCommand(parsedCommand.getArguments(), tasks, false, ui);
            storage.save(tasks.getTasks());
            break;
        case "delete":
            deleteCommand(parsedCommand.getArguments(), tasks, ui);
            storage.save(tasks.getTasks());
            break;
        case "todo":
            Task todo = new ToDoTask(parsedCommand.getArguments());
            tasks.add(todo);
            ui.showTaskAdded(todo, tasks.size());
            storage.save(tasks.getTasks());
            break;
        case "deadline":
            Task deadline = new DeadlineTask(parsedCommand.getArguments());
            tasks.add(deadline);
            ui.showTaskAdded(deadline, tasks.size());
            storage.save(tasks.getTasks());
            break;
        case "event":
            Task event = new EventTask(parsedCommand.getArguments());
            tasks.add(event);
            ui.showTaskAdded(event, tasks.size());
            storage.save(tasks.getTasks());
            break;
        case "find":
            findCommand(parsedCommand.getArguments(), tasks, ui);
            break;
        default:
            throw new LunaException("Unknown command type");
        }
    }

    /**
     * Deletes a task from the task list
     */
    private static void deleteCommand(String indexStr, TaskList tasks, Ui ui) throws LunaException {
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number to delete");
        }
        Task removed = tasks.deleteTask(index);
        ui.showTaskDeleted(removed, tasks.size());
    }

    /**
     * Finds tasks that contain the given keyword
     */
    private static void findCommand(String keyword, TaskList tasks, Ui ui) {
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showSearchResults(matchingTasks);
    }
}
