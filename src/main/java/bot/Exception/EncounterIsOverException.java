package bot.Exception;

public class EncounterIsOverException extends RuntimeException implements EncounterException {
    public EncounterIsOverException() {
        super("This encounter is over. If you'd like to start a new one use the `$dm create` command");
    }
}
