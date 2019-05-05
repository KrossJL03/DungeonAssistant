package bot.Encounter.Logger.Message;

import bot.CustomExceptionInterface;

class LoggerException extends RuntimeException implements CustomExceptionInterface {

    /**
     * LoggerException constructor
     *
     * @param message Error message
     */
    private LoggerException(String message) {
        super(message);
    }

    /**
     * Factory method for no loot exception
     *
     * @param explorerName Name of explorer that is missing loot
     *
     * @return LoggerException
     */
    static LoggerException createNoLoot(String explorerName) {
        return new LoggerException(String.format("I can't seem to find %s's loot... That's a problem", explorerName));
    }
}
