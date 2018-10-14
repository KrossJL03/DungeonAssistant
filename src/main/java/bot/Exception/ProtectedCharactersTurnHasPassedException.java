package bot.Exception;

import bot.CustomExceptionInterface;

public class ProtectedCharactersTurnHasPassedException extends RuntimeException
    implements EncounterException, CustomExceptionInterface {
    public ProtectedCharactersTurnHasPassedException(String name) {
        super(String.format("%s's turn has already passed. They can not be protected.", name));
    }
}
