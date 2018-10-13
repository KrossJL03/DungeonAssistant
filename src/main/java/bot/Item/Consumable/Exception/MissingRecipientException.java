package bot.Item.Consumable.Exception;

import bot.CustomExceptionInterface;

public class MissingRecipientException extends RuntimeException implements CustomExceptionInterface {
    private MissingRecipientException(String message) {
        super(message);
    }

    public static MissingRecipientException create(String itemName) {
        return new MissingRecipientException(String.format("Wait, who was it you wanted to use %s on?", itemName));
    }
}
