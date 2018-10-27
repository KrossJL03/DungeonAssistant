package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class PlayerCharacterPresentException extends RuntimeException implements CustomExceptionInterface {

    private PlayerCharacterPresentException(String message) {
        super(message);
    }

    public static PlayerCharacterPresentException createHasAleadyLeft(String mention) {
        return new PlayerCharacterPresentException(
            String.format(
                "%s You have already left. You can't leave again unless you `%srejoin` first",
                mention,
                CommandListener.COMMAND_KEY
            )
        );
    }

    public static PlayerCharacterPresentException createCannotRejoinIfPresent(String mention) {
        return new PlayerCharacterPresentException(
            String.format(
                "%s You are currently active in this encounter. There is not need to `%srejoin`.",
                mention,
                CommandListener.COMMAND_KEY
            )
        );
    }
}