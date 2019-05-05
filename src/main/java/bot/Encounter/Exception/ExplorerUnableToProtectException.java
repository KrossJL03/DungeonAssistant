package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class ExplorerUnableToProtectException extends RuntimeException implements CustomExceptionInterface {

    private ExplorerUnableToProtectException(String message) {
        super(message);
    }

    public static ExplorerUnableToProtectException createProtectAlreadyUsed() {
        return new ExplorerUnableToProtectException(
            String.format(
                "You've already used your `%sprotect` for this encounter",
                CommandListener.COMMAND_KEY
            )
        );
    }

}
