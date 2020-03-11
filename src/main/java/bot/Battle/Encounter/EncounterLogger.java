package bot.Battle.Encounter;

import bot.Battle.BattleLogger;
import bot.Battle.Mention;
import bot.Battle.SummaryMessageBuilder;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

class EncounterLogger extends BattleLogger
{
    /**
     * Constructor.
     *
     * @param channel   Channel
     * @param dmMention DM mention
     */
    EncounterLogger(@NotNull MessageChannel channel, @NotNull Mention dmMention)
    {
        super(
            new EncounterActionMessageBuilder(),
            new EncounterPhaseChangeMessageBuilder(),
            new SummaryMessageBuilder(),
            channel,
            dmMention
        );
    }

    /**
     * Log removed hostile
     *
     * @param name Name of hostile removed
     */
    void logRemovedHostile(@NotNull String name)
    {
        sendMessage(String.format("Hostile %s has been removed", name));
    }

    /**
     * Ping the DM to request a dodge pass
     *
     * @param player Player
     */
    void pingDmDodgePass(@NotNull Player player)
    {
        sendMessage(
            String.format(
                "%s, %s wants to successfully pass their dodge turn. Is this ok? If so please use the command DM.",
                dmMention.getValue(),
                (Mention.createForPlayer(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Log dodged action passed
     *
     * @param name      Name of explorer
     * @param currentHp Current hp of explorer
     * @param maxHp     Max hp of explorer
     */
    void logActionDodgePass(@NotNull String name, int currentHp, int maxHp)
    {
        sendMessage(
            "```ml" +
            BattleLogger.NEWLINE +
            String.format("%s successfully Dodges all attacks!", name) +
            BattleLogger.NEWLINE +
            BattleLogger.NEWLINE +
            String.format("%s takes 0 dmg total!", name) +
            BattleLogger.NEWLINE +
            String.format("%d/%d health remaining", currentHp, maxHp) +
            "```"
        );
    }

    /**
     * Log added hostile
     *
     * @param hostile Hostile
     */
    void logAddedHostile(@NotNull EncounteredHostile hostile)
    {
        sendMessage(
            String.format(
                "Hostile %s has been added to the encounter %s",
                hostile.getName(),
                getHostilePrintout(hostile)
            )
        );
    }

    /**
     * Log first death revival
     */
    void logFirstDeathRevived()
    {
        sendMessage("The guild leader in charge takes a phoenix feather out of their bag");
    }

    /**
     * Get hostile printout
     *
     * @param hostile Hostile
     *
     * @return String
     */
    private @NotNull String getHostilePrintout(@NotNull EncounteredHostile hostile)
    {
        int    nameBuffer = (int) Math.floor(15 + hostile.getName().length() / 2);
        String output     = "";
        output += "```prolog";
        output += BattleLogger.NEWLINE;
        output += (nameBuffer < 29 ? String.format("%" + nameBuffer + "s", hostile.getName()) : hostile.getName());
        output += BattleLogger.NEWLINE;
        output += "*****************************";
        output += BattleLogger.NEWLINE;
        output += "       HP     |     ATK      ";
        output += BattleLogger.NEWLINE;
        output += String.format("%9s     |     %2s", hostile.getMaxHP(), hostile.getAttackDice());
        output += BattleLogger.NEWLINE;
        output += "```";
        return output;
    }
}
