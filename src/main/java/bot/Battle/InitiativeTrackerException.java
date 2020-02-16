package bot.Battle;

import bot.CustomExceptionInterface;

class InitiativeTrackerException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * InitiativeQueueException constructor
     *
     * @param message Message
     */
    private InitiativeTrackerException(String message){
        super(message);
    }

    /**
     * Factory method for "empty queue"
     *
     * @return InitiativeQueueException
     */
    static InitiativeTrackerException createEmptyQueue() {
        return new InitiativeTrackerException("It looks like we're not in initiative, I'm not sure what to do...");
    }

    /**
     * Factory method for "not supported"
     *
     * @return InitiativeQueueException
     */
    static InitiativeTrackerException createNotSupported() {
        return new InitiativeTrackerException("Help me idk");
    }
}
