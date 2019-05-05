package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ProtectedExplorerException extends RuntimeException implements CustomExceptionInterface {

    private ProtectedExplorerException(String message) {
        super(message);
    }

    public static ProtectedExplorerException createIsSlain(String name) {
        return new ProtectedExplorerException(
            String.format("%s has already been slain. They can not be protected.", name)
        );
    }

    public static ProtectedExplorerException createProtectYourself() {
        return new ProtectedExplorerException("You can't protect yourself.");
    }

    public static ProtectedExplorerException createTurnHasPassed(String name) {
        return new ProtectedExplorerException(
            String.format("%s's turn has already passed. They can not be protected.", name)
        );
    }

}
