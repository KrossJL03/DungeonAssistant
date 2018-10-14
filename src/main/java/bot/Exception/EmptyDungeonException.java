package bot.Exception;

import bot.CustomExceptionInterface;

public class EmptyDungeonException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public EmptyDungeonException() {
        super("Well uh... this is awkward. Is seems we don't have any players...");
    }
}
