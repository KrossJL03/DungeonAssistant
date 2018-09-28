package bot.Exception;

public class ProtectedCharacterIsSlain extends RuntimeException implements EncounterException {
    public ProtectedCharacterIsSlain(String name) {
        super(String.format("%s has already been slain. They can not be protected.", name));
    }
}
