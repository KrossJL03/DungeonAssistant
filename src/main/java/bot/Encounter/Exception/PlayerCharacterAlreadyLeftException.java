package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Encounter.Exception.EncounterException;

public class PlayerCharacterAlreadyLeftException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public PlayerCharacterAlreadyLeftException() {
        super(
            String.format(
                "You have already left. You can't leave again unless you `%sreturn` first",
                CommandListener.COMMAND_KEY
            )
        );
    }
}
