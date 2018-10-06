package bot.Exception;

public class NoHostilesException extends RuntimeException implements EncounterException {
    public NoHostilesException() {
        super("Uh, wait. Who are we fighting again? Tell me using `$insertHostile NAME HP ATK`.");
    }
}