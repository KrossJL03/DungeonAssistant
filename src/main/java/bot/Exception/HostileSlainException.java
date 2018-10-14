package bot.Exception;

import bot.CustomExceptionInterface;

public class HostileSlainException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public HostileSlainException(String hostileName, String slayerName) {
        super(String.format("%s was slain by %s", hostileName, slayerName));
    }
}
