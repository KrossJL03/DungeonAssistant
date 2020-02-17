package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class NotYourTurnException extends CustomException
{
    // todo rename or move

    /**
     * NotYourTurnException constructor
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
     * @return NotYourTurnException
     */
    static @NotNull NotYourTurnException createNotYourTurn()
    {
        return new NotYourTurnException("Hey! Wait your turn!");
    }
}
