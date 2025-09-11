package luna.parser;

/**
 * Simple data structure to hold parsed command information
 */
public class ParsedCommand {
    private String commandType;
    private String arguments;

    /**
     * Constructs a ParsedCommand with the specified command type and arguments
     */
    public ParsedCommand(String commandType, String arguments) {
        assert commandType != null : "Command type should not be null";
        assert arguments != null : "Arguments should not be null (use empty string if no args)";

        this.commandType = commandType;
        this.arguments = arguments;

        assert this.commandType.equals(commandType) : "Command type should be set correctly";
        assert this.arguments.equals(arguments) : "Arguments should be set correctly";
    }

    public String getCommandType() {
        assert commandType != null : "Command type should never be null";
        return commandType;
    }

    public String getArguments() {
        assert arguments != null : "Arguments should never be null";
        return arguments;
    }

    /**
     * Checks if this command is an exit command
     */
    public boolean isExit() {
        assert commandType != null : "Command type should not be null when checking exit";
        boolean isExit = commandType.equals("bye");
        return isExit;
    }
}
