package bot.Exception;

public class NoHostileInEncounterException extends RuntimeException implements EncounterException {
    public NoHostileInEncounterException(String name) {
        super(String.format("I don't see any hostiles named %s in this encounter", name));
    }
}
