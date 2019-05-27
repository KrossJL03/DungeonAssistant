package bot.Encounter.Logger.MessageBuilder;

import bot.CustomExceptionInterface;

class MessageBuilderException extends RuntimeException implements CustomExceptionInterface {

    /**
     * MessageBuilderException constructor
     *
     * @param message Error message
     */
    private MessageBuilderException(String message) {
        super(message);
    }

    /**
     * Factory method for no loot exception
     *
     * @param explorerName Name of explorer that is missing loot
     *
     * @return MessageBuilderException
     */
    static MessageBuilderException createNoLoot(String explorerName) {
        return new MessageBuilderException(String.format("I can't seem to find %s's loot... That's a problem", explorerName));
    }
}
