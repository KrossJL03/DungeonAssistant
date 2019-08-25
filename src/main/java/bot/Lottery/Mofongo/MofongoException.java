package bot.Lottery.Mofongo;

import bot.Encounter.EncounterExceptionInterface;
import org.jetbrains.annotations.NotNull;

class MofongoException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * Constructor
     *
     * @param message Message
     */
    private @NotNull MofongoException(@NotNull String message)
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
    static @NotNull MofongoException createItemNotFound(@NotNull String rarity, int itemRoll)
    {
        return new MofongoException(String.format(
            "It looks like you rolled a %d for %s, but I don't know what that prize should be...",
            itemRoll,
            rarity
        ));
    }

    /**
     * Factory method for "pet roll count not found"
     *
     * @param speciesName Pet species name
     *
     * @return EncounterException
     */
    static @NotNull MofongoException createPetRollCountNotFound(@NotNull String speciesName)
    {
        return new MofongoException(String.format(
            "Hmm... I don't know how many rolls a %s should get. I don't think I know that pet.",
            speciesName
        ));
    }
}
