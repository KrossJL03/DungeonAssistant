package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class NotYourTurnException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public NotYourTurnException() {
        super("Hey! Wait your turn!");
    }
}
