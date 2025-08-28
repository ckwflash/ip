import java.util.ArrayList;

/**
 * Application entry point
 */
public class Luna {
    private static final String DATA_FILE_PATH = "./data/duke.txt";
    
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
        Ui ui = new Ui();
        Storage storage = new Storage(DATA_FILE_PATH);

        // Load tasks from file at startup
        tasks = storage.load();

        ui.showWelcome();

        String input = "";
        while (ui.hasMoreInput()) {
            try {
                input = ui.readCommand();
                ParsedCommand parsedCommand = Parser.parse(input);
                
                if (parsedCommand.isExit()) {
                    break;
                }
                
                executeCommand(parsedCommand, tasks, ui, storage);
            } catch (LunaException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Checks if the marking is valid and does so
     */
    private static void markCommand(String indexStr, ArrayList<Task> tasks, boolean markDone, Ui ui) throws LunaException {
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
            ui.showTaskMarked(tasks.get(index));
        } else {
            ui.showTaskUnmarked(tasks.get(index));
        }
    }

    /**
     * Executes the given parsed command
     */
    private static void executeCommand(ParsedCommand parsedCommand, ArrayList<Task> tasks, Ui ui, Storage storage) throws LunaException {
        switch (parsedCommand.getCommandType()) {
        case "list":
            ui.showTaskList(tasks);
            break;
        case "mark":
            markCommand(parsedCommand.getArguments(), tasks, true, ui);
            storage.save(tasks);
            break;
        case "unmark":
            markCommand(parsedCommand.getArguments(), tasks, false, ui);
            storage.save(tasks);
            break;
        case "delete":
            deleteCommand(parsedCommand.getArguments(), tasks, ui);
            storage.save(tasks);
            break;
        case "todo":
            Task todo = new ToDoTask(parsedCommand.getArguments());
            tasks.add(todo);
            ui.showTaskAdded(todo, tasks.size());
            storage.save(tasks);
            break;
        case "deadline":
            Task deadline = new DeadlineTask(parsedCommand.getArguments());
            tasks.add(deadline);
            ui.showTaskAdded(deadline, tasks.size());
            storage.save(tasks);
            break;
        case "event":
            Task event = new EventTask(parsedCommand.getArguments());
            tasks.add(event);
            ui.showTaskAdded(event, tasks.size());
            storage.save(tasks);
            break;
        default:
            throw new LunaException("Unknown command type");
        }
    }

    /**
     * Deletes a task from the task list
     */
    private static void deleteCommand(String indexStr, ArrayList<Task> tasks, Ui ui) throws LunaException {
        int index;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new LunaException("Please give a valid task number to delete");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new LunaException("Task index is out of bounds");
        }
        Task removed = tasks.remove(index);
        ui.showTaskDeleted(removed, tasks.size());
    }
}
