package bot.Encounter.Logger;

import bot.Encounter.ActionResultInterface;
import bot.Encounter.EncounteredExplorerInterface;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.Logger.Message.Action.ActionMessageBuilder;
import bot.Encounter.Logger.Message.PhaseChange.PhaseChangeMessageBuilder;
import bot.Encounter.Logger.Message.Summary.SummaryMessageBuilder;
import bot.Encounter.PhaseChangeResult;
import bot.Encounter.TierInterface;
import bot.MyProperties;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterLogger
{
    private static String NEWLINE = System.getProperty("line.separator");

    private ActionMessageBuilder      actionMessageBuilder;
    private MessageChannel            channel;
    private Mention                   dmMention;
    private Mention                   everyoneMention;
    private PhaseChangeMessageBuilder phaseChangeMessageBuilder;
    private SummaryMessageBuilder     summaryMessageBuilder;

    /**
     * EncounterLogger constructor
     *
     * @param actionMessageBuilder      Action message builder
     * @param phaseChangeMessageBuilder Phase change message builder
     * @param summaryMessageBuilder     Summary message builder
     */
    public @NotNull EncounterLogger(
        @NotNull ActionMessageBuilder actionMessageBuilder,
        @NotNull PhaseChangeMessageBuilder phaseChangeMessageBuilder,
        @NotNull SummaryMessageBuilder summaryMessageBuilder
    )
    {
        this.actionMessageBuilder = actionMessageBuilder;
        this.everyoneMention = Mention.createForEveryone();
        this.phaseChangeMessageBuilder = phaseChangeMessageBuilder;
        this.summaryMessageBuilder = summaryMessageBuilder;
    }

    /**
     * Log action result
     *
     * @param result Action result
     */
    public void logAction(@NotNull ActionResultInterface result)
    {
        sendMessage(actionMessageBuilder.buildActionMessage(result, dmMention));
    }

    /**
     * Log skipped attack action
     *
     * @param name Character name
     */
    public void logActionAttackSkipped(@NotNull String name)
    {
        sendMessage(String.format("%s's turn has been skipped", name));
    }

    /**
     * Log dodged action passed
     *
     * @param name      Name of explorer
     * @param currentHp Current hp of explorer
     * @param maxHp     Max hp of explorer
     */
    public void logActionDodgePass(@NotNull String name, int currentHp, int maxHp)
    {
        sendMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s successfully Dodges all attacks!", name) +
            EncounterLogger.NEWLINE +
            EncounterLogger.NEWLINE +
            String.format("%s takes 0 dmg total!", name) +
            EncounterLogger.NEWLINE +
            String.format("%d/%d health remaining", currentHp, maxHp) +
            "```"
        );
    }

    /**
     * Log number of actions remaining for player
     *
     * @param name             Explorer name
     * @param actionsRemaining Number of actions remaining
     */
    public void logActionsRemaining(@NotNull String name, int actionsRemaining)
    {
        sendMessage(
            String.format("%s has %d action%s remaining", name, actionsRemaining, actionsRemaining > 1 ? "s" : "")
        );
    }

    /**
     * Log added hostile
     *
     * @param hostile Hostile
     */
    public void logAddedHostile(@NotNull EncounteredHostileInterface hostile)
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
     * Log create encounter
     */
    public void logCreateEncounter()
    {
        sendMessage("Encounter creation has started!");
    }

    /**
     * Log first death revival
     */
    public void logFirstDeathRevived()
    {
        sendMessage("The guild leader in charge takes a phoenix feather out of their bag");
    }

    /**
     * Log number of actions remaining for player
     *
     * @param name Explorer name
     */
    public void logGiveProtectAction(@NotNull String name)
    {
        sendMessage(String.format("%s has been given an extra protect action!", name));
    }

    /**
     * Log kicked player
     *
     * @param player Kicked player
     */
    public void logKickedPlayer(@NotNull Player player)
    {
        sendMessage(String.format(
            "%s has been kicked and may not rejoin the battle.",
            (Mention.createForPlayer(player.getUserId())).getValue()
        ));
    }

    /**
     * Log left encounter
     *
     * @param name Name of explorer that left
     */
    public void logLeftEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has left the encounter", name));
    }

    /**
     * Log phase change
     *
     * @param result Phase change result
     */
    public void logPhaseChange(PhaseChangeResult result)
    {
        String message = phaseChangeMessageBuilder.buildPhaseChangeMessage(result);
        if (MyProperties.BOOL_PING_EVERYONE && result.getNextPhase().isJoinPhase()) {
            message = everyoneMention.getValue() + " " + message;
        }
        sendMessage(message);
        logSummary(result.getExplorers(), result.getHostiles());
    }

    /**
     * Log rejoined encounter
     *
     * @param name Name of explorer that rejoined
     */
    public void logRejoinEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has rejoined the encounter!", name));
    }

    /**
     * Log removed explorer
     *
     * @param name Name of explorer removed
     */
    public void logRemovedExplorer(@NotNull String name)
    {
        sendMessage(String.format("Explorer %s has been removed", name));
    }

    /**
     * Log removed hostile
     *
     * @param name Name of hostile removed
     */
    public void logRemovedHostile(@NotNull String name)
    {
        sendMessage(String.format("Hostile %s has been removed", name));
    }

    /**
     * Log set max players
     *
     * @param maxPlayerCount Max player count
     */
    public void logSetMaxPlayers(int maxPlayerCount)
    {
        sendMessage(String.format("Max player count has been set to %d", maxPlayerCount));
    }

    /**
     * Tier has been set
     *
     * @param tier Tier
     */
    public void logSetTier(@NotNull TierInterface tier)
    {
        sendMessage(String.format(
            "%s tier has been set! [Stat Point Range: %d - %d]",
            tier.getName(),
            tier.getMinStatPointTotal(),
            tier.getMaxStatPointTotal()
        ));
    }

    /**
     * Log encounter summary
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    public void logSummary(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        sendMessage(summaryMessageBuilder.buildSummary(encounteredExplorers, encounteredHostiles));
    }

    /**
     * Ping the DM to request a dodge pass
     *
     * @param player Player
     */
    public void pingDmDodgePass(@NotNull Player player)
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
     * Ping DM to request item use
     *
     * @param player Player
     */
    public void pingDmItemUsed(@NotNull Player player)
    {
        sendMessage(
            String.format(
                "%s, %s has used an item. Could you tell me what to do?",
                dmMention.getValue(),
                (Mention.createForPlayer(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Ping player that it is their turn
     *
     * @param explorer Explorer
     */
    public void pingPlayerTurn(@NotNull EncounteredExplorerInterface explorer)
    {
        sendMessage(
            String.format(
                "%s, it's %s's turn!",
                (Mention.createForPlayer(explorer.getOwner().getUserId())).getValue(),
                explorer.getName()
            )
        );
    }

    /**
     * Set logger channel
     *
     * @param channel Channel
     */
    public void setChannel(@NotNull MessageChannel channel)
    {
        this.channel = channel;
    }

    /**
     * Set dungeon master mention
     *
     * @param dmMention Dungeon master mention
     */
    public void setDmMention(@NotNull Mention dmMention)
    {
        this.dmMention = dmMention;
    }

    /**
     * Get hostile printout
     *
     * @param hostile Hostile
     *
     * @return String
     */
    private @NotNull String getHostilePrintout(@NotNull EncounteredHostileInterface hostile)
    {
        int    nameBuffer = (int) Math.floor(15 + hostile.getName().length() / 2);
        String output     = "";
        output += "```prolog";
        output += EncounterLogger.NEWLINE;
        output += (nameBuffer < 29 ? String.format("%" + nameBuffer + "s", hostile.getName()) : hostile.getName());
        output += EncounterLogger.NEWLINE;
        output += "*****************************";
        output += EncounterLogger.NEWLINE;
        output += "       HP     |     ATK      ";
        output += EncounterLogger.NEWLINE;
        output += String.format("%9s     |     %2s", hostile.getMaxHP(), hostile.getAttackDice());
        output += EncounterLogger.NEWLINE;
        output += "```";
        return output;
    }

    /**
     * Send message
     *
     * @param message Message
     */
    private void sendMessage(@NotNull String message)
    {
        channel.sendMessage(message).queue();
    }
}