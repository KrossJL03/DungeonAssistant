package bot.Explorer.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class ExplorerNotFoundException extends RuntimeException implements CustomExceptionInterface {
    private ExplorerNotFoundException(String message) {
        super(message);
    }

    public static ExplorerNotFoundException createNotInDatabase(String name) {
        return new ExplorerNotFoundException(
            String.format(
                "I couldn't find '%s'... maybe try adding them with the `%screate` command?",
                name,
                CommandListener.COMMAND_KEY
            )
        );
    }
}
