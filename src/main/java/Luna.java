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
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ui.showTaskList(tasks);
                } else {
                    String[] parts = input.split(" ", 2);
                    String command = parts[0];
                    switch (command) {
                    case "mark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, true, ui);
                            storage.save(tasks);
                        } else {
                            throw new LunaException("Please provide a task number to mark");
                        }
                        break;
                    case "unmark":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            markCommand(parts[1], tasks, false, ui);
                            storage.save(tasks);
                        } else {
                            throw new LunaException("Please provide a task number to unmark");
                        }
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
                            ui.showTaskDeleted(removed, tasks.size());
                            storage.save(tasks);
                        } else {
                            throw new LunaException("Please give a task number to delete");
                        }
                        break;
                    case "todo":
                        Task todo = new ToDoTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(todo);
                        ui.showTaskAdded(todo, tasks.size());
                        storage.save(tasks);
                        break;
                    case "deadline":
                        Task deadline = new DeadlineTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(deadline);
                        ui.showTaskAdded(deadline, tasks.size());
                        storage.save(tasks);
                        break;
                    case "event":
                        Task event = new EventTask(parts.length > 1 ? parts[1] : "");
                        tasks.add(event);
                        ui.showTaskAdded(event, tasks.size());
                        storage.save(tasks);
                        break;
                    default:
                        throw new LunaException("Sorry! I dont gets");
                    }
                }
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
}
