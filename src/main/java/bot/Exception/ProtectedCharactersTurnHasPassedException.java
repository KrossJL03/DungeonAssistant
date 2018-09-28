package bot.Exception;

public class ProtectedCharactersTurnHasPassedException extends RuntimeException {

    private String name;

    public ProtectedCharactersTurnHasPassedException(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
