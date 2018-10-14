package bot.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class CharacterUnableToProtectException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public CharacterUnableToProtectException() {
        super(String.format("You've already used your `%sprotect` for this encounter", CommandListener.COMMAND_KEY));
    }
}
