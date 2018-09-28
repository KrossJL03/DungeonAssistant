package bot.Exception;

public class WrongPhaseException extends RuntimeException {

    private String commandName;
    private String phase;

    public WrongPhaseException(String phase, String commandName) {
        this.commandName = commandName;
        this.phase = phase;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getPhase() {
        return phase;
    }
}
