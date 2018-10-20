package bot.PlayerCharacter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class PlayerCharacterNotFoundException extends RuntimeException implements CustomExceptionInterface {
    private PlayerCharacterNotFoundException(String message) {
        super(message);
    }

    public static PlayerCharacterNotFoundException createNotInDatabase(String name) {
        return new PlayerCharacterNotFoundException(
            String.format(
                "I couldn't find '%s'... maybe try adding them with the `%screate` command?",
                name,
                CommandListener.COMMAND_KEY
            )
        );
    }
}
