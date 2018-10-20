package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class PlayerCharacterUnableToProtectException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {

    private PlayerCharacterUnableToProtectException(String message) {
        super(message);
    }

    public static PlayerCharacterUnableToProtectException createProtectAlreadyUsed() {
        return new PlayerCharacterUnableToProtectException(
            String.format(
                "You've already used your `%sprotect` for this encounter",
                CommandListener.COMMAND_KEY
            )
        );
    }
}
