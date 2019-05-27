package bot.Encounter.Logger;

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
     * Factory method for action not set exception
     *
     * @return LoggerException
     */
    static LoggerException createActionNotSet() {
        return new LoggerException("I don't know how to log that action");
    }
}
