package bot.Encounter;

import org.jetbrains.annotations.NotNull;

class NotYourTurnException extends RuntimeException implements EncounterExceptionInterface
{
    // todo rename or move

    /**
     * DungeonException constructor
     *
     * @param message Message
     */
    private @NotNull NotYourTurnException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "no players have joined"
     *
     * @return DungeonException
     */
    static @NotNull NotYourTurnException createNotYourTurn()
    {
        return new NotYourTurnException("Hey! Wait your turn!");
    }
}
