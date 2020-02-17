package bot.Battle.Logger;

import bot.CustomException;

class LoggerException extends CustomException
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
