package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ProtectedCharacterException extends RuntimeException implements CustomExceptionInterface {

    private ProtectedCharacterException(String message) {
        super(message);
    }

    public static ProtectedCharacterException createIsSlain(String name) {
        return new ProtectedCharacterException(
            String.format("%s has already been slain. They can not be protected.", name)
        );
    }

    public static ProtectedCharacterException createNotPlayerCharacter(String name) {
        return new ProtectedCharacterException(
            String.format("%s is not a player character and can not be protected.", name)
        );
    }

    public static ProtectedCharacterException createProtectYourself() {
        return new ProtectedCharacterException("You can't protect yourself.");
    }

    public static ProtectedCharacterException createTurnHasPassed(String name) {
        return new ProtectedCharacterException(
            String.format("%s's turn has already passed. They can not be protected.", name)
        );
    }

}
