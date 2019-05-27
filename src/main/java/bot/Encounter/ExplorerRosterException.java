package bot.Encounter;

import bot.CommandListener;
import bot.Encounter.Logger.Mention;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

class ExplorerRosterException extends RuntimeException implements EncounterExceptionInterface
{
    /**
     * ExplorerRosterException constructor
     *
     * @param message Message
     */
    private @NotNull ExplorerRosterException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "cannot rejoin if present"
     *
     * @param player Owner
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createCannotRejoinIfPresent(@NotNull Player player)
    {
        return new ExplorerRosterException(
            String.format(
                "%s You are currently active in this encounter. There is not need to `%srejoin`.",
                (Mention.createForPlayer(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "full roster"
     *
     * @param player Owner
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createFullRoster(@NotNull Player player)
    {
        return new ExplorerRosterException(
            String.format(
                "Uh oh, looks like the dungeon is full. Sorry %s.",
                (Mention.createForPlayer(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Factory method for "has already left"
     *
     * @param player Owner
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createHasAleadyLeft(@NotNull Player player)
    {
        return new ExplorerRosterException(
            String.format(
                "%s You have already left. You can't leave again unless you `%srejoin` first",
                (Mention.createForPlayer(player.getUserId())).getValue(),
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "max players not set"
     *
     * @return EncounterPhaseException
     */
    static @NotNull ExplorerRosterException createMaxPlayersNotSet()
    {
        return new ExplorerRosterException(
            String.format(
                "Wait, I don't know how many players to have. DM could you tell me using `%smaxPlayers`?",
                CommandListener.COMMAND_KEY
            )
        );
    }

    /**
     * Factory method for "max player count less than 1"
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createMaxPlayerCountLessThanOne()
    {
        return new ExplorerRosterException("You can't have less than 1 player... that just doesn't work");
    }

    /**
     * Factory method for "multiple explorers"
     *
     * @param player Owner
     * @param name   Explorer name
     */
    static @NotNull ExplorerRosterException createMultipleExplorers(@NotNull Player player, @NotNull String name)
    {
        return new ExplorerRosterException(
            String.format(
                "%s, you have already joined this encounter with %s. " +
                "If you'd like to switch please talk to the DungeonMaster",
                (Mention.createForPlayer(player.getUserId())).getValue(),
                name
            )
        );
    }

    /**
     * Factory method for "new player max less than current player count"
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createNewPlayerMaxLessThanCurrentPlayerCount(
        int newMaxPlayerCount,
        int presentPlayerCount
    )
    {
        return new ExplorerRosterException(
            String.format(
                "It looks like we already have %d present players." +
                "If you want to lower the max player count to %d please remove some players first.",
                presentPlayerCount,
                newMaxPlayerCount
            )
        );
    }

}
