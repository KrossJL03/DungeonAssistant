package bot.Battle;

import bot.MyProperties;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattleLogger
{
    final protected static String NEWLINE = System.getProperty("line.separator");

    protected Mention                   dmMention;
    private   ActionMessageBuilder      actionMessageBuilder;
    private   MessageChannel            channel;
    private   Mention                   everyoneMention;
    private   PhaseChangeMessageBuilder phaseChangeMessageBuilder;
    private   SummaryMessageBuilder     summaryMessageBuilder;

    /**
     * Constructor.
     *
     * @param actionMessageBuilder      Action message builder
     * @param phaseChangeMessageBuilder Phase change message builder
     * @param summaryMessageBuilder     Summary message builder
     * @param channel                   Channel
     * @param dmMention                 DM mention
     */
    public @NotNull BattleLogger(
        @NotNull ActionMessageBuilder actionMessageBuilder,
        @NotNull PhaseChangeMessageBuilder phaseChangeMessageBuilder,
        @NotNull SummaryMessageBuilder summaryMessageBuilder,
        @NotNull MessageChannel channel,
        @NotNull Mention dmMention
    )
    {
        this.actionMessageBuilder = actionMessageBuilder;
        this.channel = channel;
        this.dmMention = dmMention;
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
     * Ping player that it is their turn
     *
     * @param explorer Explorer
     */
    public void pingPlayerTurn(@NotNull CombatExplorer explorer)
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
     * Log create encounter
     */
    void logCreateEncounter()
    {
        sendMessage("Battle creation has started!");
    }

    /**
     * Log kicked player
     *
     * @param player Kicked player
     */
    void logKickedPlayer(@NotNull Player player)
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
    void logLeftEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has left the encounter", name));
    }

    /**
     * Log phase change
     *
     * @param result Phase change result
     */
    void logPhaseChange(BattlePhaseChangeResult result)
    {
        String message = phaseChangeMessageBuilder.buildPhaseChangeMessage(result);
        if (MyProperties.BOOL_PING_EVERYONE && result.getNextPhase().isJoinPhase()) {
            message = everyoneMention.getValue() + " " + message;
        }

        sendMessage(message);
        logSummary(result.getCreatures());
    }

    /**
     * Log rejoined encounter
     *
     * @param name Name of explorer that rejoined
     */
    void logRejoinEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has rejoined the encounter!", name));
    }

    /**
     * Log removed explorer
     *
     * @param name Name of explorer removed
     */
    void logRemovedExplorer(@NotNull String name)
    {
        sendMessage(String.format("Explorer %s has been removed", name));
    }

    /**
     * Log set max players
     *
     * @param maxPlayerCount Max player count
     */
    void logSetMaxPlayers(int maxPlayerCount)
    {
        sendMessage(String.format("Max player count has been set to %d", maxPlayerCount));
    }

    /**
     * Tier has been set
     *
     * @param tier Tier
     */
    void logSetTier(@NotNull Tier tier)
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
     * @param creatures Creatures
     */
    void logSummary(@NotNull ArrayList<CombatCreature> creatures)
    {
        sendMessage(summaryMessageBuilder.buildSummary(creatures));
    }

    /**
     * Ping DM to request item use
     *
     * @param player Player
     */
    void pingDmItemUsed(@NotNull Player player)
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
     * Send message
     *
     * @param message Message
     */
    final protected void sendMessage(@NotNull String message)
    {
        channel.sendMessage(message).queue();
    }
}