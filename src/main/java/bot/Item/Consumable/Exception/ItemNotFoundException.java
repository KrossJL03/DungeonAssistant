package bot.Item.Consumable.Exception;

import bot.CustomExceptionInterface;

public class ItemNotFoundException extends RuntimeException implements CustomExceptionInterface {
    private ItemNotFoundException(String message) {
        super(message);
    }

    public static ItemNotFoundException createForConsumable(String name) {
        return new ItemNotFoundException(String.format("I've never heard of the item '%s' before...", name));
    }
}
