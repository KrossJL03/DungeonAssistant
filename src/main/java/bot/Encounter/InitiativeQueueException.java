package bot.Encounter;

import bot.CustomExceptionInterface;

class InitiativeQueueException extends RuntimeException implements CustomExceptionInterface
{
    /**
     * InitiativeQueueException constructor
     *
     * @param message Message
     */
    private InitiativeQueueException(String message){
        super(message);
    }

    /**
     * Factory method for "empty queue"
     *
     * @return InitiativeQueueException
     */
    static InitiativeQueueException createEmptyQueue() {
        return new InitiativeQueueException("It looks like we're not in initiative, I'm not sure what to do...");
    }
}
