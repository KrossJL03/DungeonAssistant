package bot.Encounter;

import bot.CustomExceptionInterface;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Tier.Tier;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

class RosterException extends RuntimeException implements CustomExceptionInterface
{
    private RosterException(String message)
    {
        super(message);
    }

    /**
     * Factory method for "does not fit tier"
     *
     * @param encounteredExplorer Encountered explorer
     * @param tier                Tier
     *
     * @return RosterException
     */
    static @NotNull RosterException createDoesNotFitTier(@NotNull PCEncounterData encounteredExplorer, Tier tier)
    {
        Player owner = encounteredExplorer.getOwner();
        return new RosterException(
            String.format(
                "%s, %s does not fit the %s tier.",
                owner.getAsMention(),
                encounteredExplorer.getName(),
                tier.getName()
            )
        );
    }

    static RosterException createFullRoster(Player player){
        return new RosterException(
            String.format("Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention())
        );
    }

    /**
     * Factory for "kicked player returns"
     *
     * @param player Kicked player
     *
     * @return RosterException
     */
    static @NotNull RosterException createKickedPlayerReturns(@NotNull Player player){
        return new RosterException(
            String.format("Sorry %s, you were kicked. Try again next time.", player.getAsMention())
        );
    }
}
