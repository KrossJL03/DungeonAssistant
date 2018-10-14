package bot.Exception;

import bot.CustomExceptionInterface;

public class ContextChannelNotSetException extends RuntimeException implements CustomExceptionInterface {
    public ContextChannelNotSetException() {}
}
