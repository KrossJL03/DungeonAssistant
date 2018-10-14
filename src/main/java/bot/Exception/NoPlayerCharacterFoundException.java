package bot.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class NoPlayerCharacterFoundException extends RuntimeException implements CustomExceptionInterface {
    private NoPlayerCharacterFoundException(String message) {
        super(message);
    }

    public static NoPlayerCharacterFoundException create(String name) {
        return new NoPlayerCharacterFoundException(
            String.format(
                "I couldn't find '%s'... maybe try adding them with the `%screate` command?",
                name,
                CommandListener.COMMAND_KEY
            )
        );
    }
}
