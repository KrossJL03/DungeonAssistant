package bot.Exception;

public class WrongPhaseException extends RuntimeException implements EncounterException {
    public WrongPhaseException(String phase, String commandName) {
        super(String.format("You can only `$%s` during the %s turn", commandName, phase));
    }
}
