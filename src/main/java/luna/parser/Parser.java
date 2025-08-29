package luna.parser;

import luna.exception.LunaException;

/**
 * Deals with making sense of the user command
 */
public class Parser {

    /**
     * Parses the user input and returns command type and arguments
     */
    public static ParsedCommand parse(String fullCommand) throws LunaException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new LunaException("Empty command");
        }

        String trimmedCommand = fullCommand.trim();

        if (trimmedCommand.equals("bye")) {
            return new ParsedCommand("bye", "");
        }

        if (trimmedCommand.equals("list")) {
            return new ParsedCommand("list", "");
        }

        String[] parts = trimmedCommand.split(" ", 2);
        String commandWord = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
        case "mark":
            if (arguments.isBlank()) {
                throw new LunaException("Please provide a task number to mark");
            }
            return new ParsedCommand("mark", arguments);

        case "unmark":
            if (arguments.isBlank()) {
                throw new LunaException("Please provide a task number to unmark");
            }
            return new ParsedCommand("unmark", arguments);

        case "delete":
            if (arguments.isBlank()) {
                throw new LunaException("Please give a task number to delete");
            }
            return new ParsedCommand("delete", arguments);

        case "todo":
            return new ParsedCommand("todo", arguments);

        case "deadline":
            return new ParsedCommand("deadline", arguments);

        case "event":
            return new ParsedCommand("event", arguments);

        default:
            throw new LunaException("Sorry! I dont gets");
        }
    }
}
