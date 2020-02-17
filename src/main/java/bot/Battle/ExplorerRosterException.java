package bot.Battle;

import bot.CustomException;
import bot.MyProperties;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

class ExplorerRosterException extends CustomException
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
     * Factory method for "does not fit tier"
     *
     * @param explorer Explorer
     * @param tier     Tier
     *
     * @return RosterException
     */
    static @NotNull ExplorerRosterException createDoesNotFitTier(@NotNull CombatExplorer explorer, @NotNull Tier tier)
    {
        Player owner = explorer.getOwner();
        return new ExplorerRosterException(
            String.format(
                "%s, %s does not fit the %s tier.",
                (Mention.createForPlayer(owner.getUserId())).getValue(),
                explorer.getName(),
                tier.getName()
            )
        );
    }

    /**
     * Factory method for "explorer not found"
     *
     * @param name Explorer name
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createExplorerNotFound(@NotNull String name)
    {
        return new ExplorerRosterException(
            String.format("I can't find %s, are you sure they are part of this encounter?", name)
        );
    }

    /**
     * Factory method for "explorer not found"
     *
     * @param player Owner
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull ExplorerRosterException createExplorerNotFound(@NotNull Player player)
    {
        return new ExplorerRosterException(
            String.format("I could not find your character in this encounter %s", player.getName())
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
     * Factory for "kicked player returns"
     *
     * @param player Kicked player
     *
     * @return RosterException
     */
    static @NotNull ExplorerRosterException createKickedPlayerReturns(@NotNull Player player)
    {
        return new ExplorerRosterException(
            String.format(
                "Sorry %s, you were kicked. Try again next time.",
                (Mention.createForPlayer(player.getUserId())).getValue()
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
     * Factory method for "max players not set"
     *
     * @return BattlePhaseException
     */
    static @NotNull ExplorerRosterException createMaxPlayersNotSet()
    {
        return new ExplorerRosterException(
            String.format(
                "Wait, I don't know how many players to have. DM could you tell me using `%smaxPlayers`?",
                MyProperties.COMMAND_PREFIX
            )
        );
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
     * Factory method for "cannot rejoin if present"
     *
     * @param player Owner
     *
     * @return ExplorerRosterException
     */
    static @NotNull ExplorerRosterException createNameTaken(@NotNull Player player, @NotNull String name)
    {
        return new ExplorerRosterException(
            String.format(
                "%s Someone named '%s' is already in the battle. Could you use a nickname?",
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
