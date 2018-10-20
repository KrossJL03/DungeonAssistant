package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.Exception.EncounterException;

public class HostileSlainException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public HostileSlainException(String hostileName, String slayerName) {
        super(String.format("%s was slain by %s", hostileName, slayerName));
    }
}
