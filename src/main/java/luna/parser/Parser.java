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
        assert fullCommand != null : "Full command should not be null";

        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new LunaException("Empty command");
        }

        String trimmedCommand = fullCommand.trim();
        assert trimmedCommand != null : "Trimmed command should not be null";
        assert !trimmedCommand.isEmpty() : "Trimmed command should not be empty after validation";

        if (trimmedCommand.equals("bye")) {
            ParsedCommand result = new ParsedCommand("bye", "");
            assert result != null : "ParsedCommand should be created successfully";
            assert result.getCommandType().equals("bye") : "Command type should be 'bye'";
            return result;
        }

        if (trimmedCommand.equals("list")) {
            ParsedCommand result = new ParsedCommand("list", "");
            assert result != null : "ParsedCommand should be created successfully";
            assert result.getCommandType().equals("list") : "Command type should be 'list'";
            return result;
        }

        String[] parts = trimmedCommand.split(" ", 2);
        assert parts != null : "Split result should not be null";
        assert parts.length >= 1 : "Split should produce at least one part";

        String commandWord = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        assert commandWord != null : "Command word should not be null";
        assert arguments != null : "Arguments should not be null";

        switch (commandWord) {
        case "mark":
            if (arguments.isBlank()) {
                throw new LunaException("Please provide a task number to mark");
            }
            assert !arguments.isBlank() : "Arguments should not be blank after validation";
            return new ParsedCommand("mark", arguments);

        case "unmark":
            if (arguments.isBlank()) {
                throw new LunaException("Please provide a task number to unmark");
            }
            assert !arguments.isBlank() : "Arguments should not be blank after validation";
            return new ParsedCommand("unmark", arguments);

        case "delete":
            if (arguments.isBlank()) {
                throw new LunaException("Please give a task number to delete");
            }
            assert !arguments.isBlank() : "Arguments should not be blank after validation";
            return new ParsedCommand("delete", arguments);

        case "todo":
            return new ParsedCommand("todo", arguments);

        case "deadline":
            return new ParsedCommand("deadline", arguments);

        case "event":
            return new ParsedCommand("event", arguments);

        case "find":
            if (arguments.isBlank()) {
                throw new LunaException("Please provide a keyword to search for");
            }
            assert !arguments.isBlank() : "Arguments should not be blank after validation";
            return new ParsedCommand("find", arguments);

        case "undo":
            return new ParsedCommand("undo", arguments);

        default:
            throw new LunaException("Sorry! I dont gets");
        }
    }
}
