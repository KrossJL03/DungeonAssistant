package bot.Exception;

public class ProtectedCharactersTurnHasPassedException extends RuntimeException implements EncounterException {
    public ProtectedCharactersTurnHasPassedException(String name) {
        super(String.format("%s's turn has already passed. They can not be protected.", name));
    }
}
