package bot.Exception;

public class EmptyDungeonException extends RuntimeException implements EncounterException {
    public EmptyDungeonException() {
        super("Well uh... this is awkward. Is seems we don't have any players...");
    }
}
