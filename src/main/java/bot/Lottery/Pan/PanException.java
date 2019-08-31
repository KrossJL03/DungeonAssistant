package bot.Lottery.Pan;

import bot.Encounter.EncounterExceptionInterface;
import org.jetbrains.annotations.NotNull;

class PanException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * Constructor
     *
     * @param message Message
     */
    private @NotNull PanException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "item not found"
     *
     * @param rarity   Rarity
     * @param itemRoll Item roll
     *
     * @return EncounterException
     */
    static @NotNull PanException createItemNotFound(@NotNull String rarity, int itemRoll)
    {
        return new PanException(String.format(
            "It looks like you rolled a %d for %s, but I don't know what that prize should be...",
            itemRoll,
            rarity
        ));
    }
}
