package bot.Exception;

import bot.CustomExceptionInterface;

public class ProtectYourselfException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    public ProtectYourselfException() {
        super("You can't protect yourself.");
    }
}
