package bot.Exception;

import bot.CustomExceptionInterface;

public class ConsumerIsDeadException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    // todo use for items
    public ConsumerIsDeadException() {}
}
