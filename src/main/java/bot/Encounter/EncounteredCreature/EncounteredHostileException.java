package bot.Encounter.EncounteredCreature;

import bot.Encounter.EncounterExceptionInterface;
import org.jetbrains.annotations.NotNull;

class EncounteredHostileException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * EncounteredExplorerException constructor
     *
     * @param message Message
     */
    private @NotNull EncounteredHostileException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "invalid stat name"
     *
     * @param name Stat name
     *
     * @return EncounteredHostileException
     */
    static @NotNull EncounteredHostileException createInvalidStatName(@NotNull String name)
    {
        return new EncounteredHostileException(String.format("%s is not the name of a hostile stat", name));
    }
}
