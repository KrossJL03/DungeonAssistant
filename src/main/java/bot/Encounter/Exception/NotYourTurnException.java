package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class NotYourTurnException extends RuntimeException implements CustomExceptionInterface {

    public NotYourTurnException() {
        super("Hey! Wait your turn!");
    }

}
