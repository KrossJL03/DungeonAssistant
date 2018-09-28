package bot.Exception;

public class MaxZeroPlayersException extends RuntimeException implements EncounterException {
    public MaxZeroPlayersException() {
        super("Wait! I don't know how many players to have! Tell me using `$setMaxPlayers`.");
    }
}
