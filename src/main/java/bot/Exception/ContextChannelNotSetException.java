package bot.Exception;

import bot.CustomExceptionInterface;

public class ContextChannelNotSetException extends RuntimeException implements CustomExceptionInterface {
    public ContextChannelNotSetException() {
        super("I'm not sure which channel to talk in...");
    }
}
