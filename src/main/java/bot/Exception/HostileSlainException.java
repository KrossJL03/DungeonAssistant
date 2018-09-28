package bot.Exception;

public class HostileSlainException extends RuntimeException implements EncounterException {
    public HostileSlainException(String hostileName, String slayerName) {
        super(String.format("%s was slain by %s", hostileName, slayerName));
    }
}
