package bot.Encounter.Logger;

import bot.CustomExceptionInterface;

class LoggerException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * LoggerException constructor
     *
     * @param message Error message
     */
    private LoggerException(String message)
    {
        super(message);
    }
}
