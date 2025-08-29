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
        this.commandType = commandType;
        this.arguments = arguments;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getArguments() {
        return arguments;
    }

    public boolean isExit() {
        return commandType.equals("bye");
    }
}
