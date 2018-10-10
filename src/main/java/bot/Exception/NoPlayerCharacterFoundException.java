package bot.Exception;

public class NoPlayerCharacterFoundException extends RuntimeException {
    private NoPlayerCharacterFoundException(String message) {
        super(message);
    }

    public static NoPlayerCharacterFoundException create(String name) {
        return new NoPlayerCharacterFoundException(
            String.format("I couldn't find '%s'... maybe try adding them with the `$create` command?", name)
        );
    }
}
