package bot.Encounter;

import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

class ActionListener
{
    private EncounterLogger logger;

    /**
     * ActionListener constructor
     *
     * @param logger Logger
     */
    @NotNull ActionListener(@NotNull EncounterLogger logger)
    {
        this.logger = logger;
    }

    /**
     * Log action when action occurs
     *
     * @param result Action result
     */
    void onAction(ActionResultInterface result)
    {
        logger.logAction(result);
    }

    /**
     * Log on actions remaining
     *
     * @param name             Name of skipped explorer
     * @param remainingActions Number of actions
     */
    void onActionsRemaining(String name, int remainingActions)
    {
        logger.logActionsRemaining(name, remainingActions);
    }

    /**
     * Log on add hostile
     *
     * @param encounteredHostile Encountered hostile
     */
    void onAddHostile(EncounteredHostileInterface encounteredHostile)
    {
        logger.logAddedHostile(encounteredHostile);
    }

    /**
     * Log on attack action skipped
     *
     * @param name Name of skipped explorer
     */
    void onAttackActionSkipped(String name)
    {
        logger.logActionAttackSkipped(name);
    }

    /**
     * Log created encounter
     */
    void onCreateEncounter()
    {
        logger.logCreateEncounter();
    }

    /**
     * Log dodge pass action when action occurs
     *
     * @param name      Name of explorer
     * @param currentHp Current hp of explorer
     * @param maxHp     Max hp of explorer
     */
    void onDodgePassAction(String name, int currentHp, int maxHp)
    {
        logger.logActionDodgePass(name, currentHp, maxHp);
    }

    /**
     * Log on give protect action
     *
     * @param explorer Name of explorer
     */
    void onGiveProtectAction(EncounteredExplorerInterface explorer)
    {
        logger.logGiveProtectAction(explorer.getName());
    }

    /**
     * Log on kick
     *
     * @param owner Kicked player
     */
    void onKick(Player owner)
    {
        logger.logKickedPlayer(owner);
    }

    /**
     * Log on leave
     *
     * @param name Name of explorer that left
     */
    void onLeave(String name)
    {
        logger.logLeftEncounter(name);
    }

    /**
     * Log on next player turn
     *
     * @param encounteredExplorer Next explorer
     */
    void onNextPlayerTurn(EncounteredExplorerInterface encounteredExplorer)
    {
        logger.pingPlayerTurn(encounteredExplorer);
    }

    /**
     * Log phase change when it occurs
     *
     * @param result Action result
     */
    void onPhaseChange(PhaseChangeResult result)
    {
        logger.logPhaseChange(result);
    }

    /**
     * Log on rejoin
     *
     * @param name Name of explorer to rejoin
     */
    void onRejoin(String name)
    {
        logger.logRejoinEncounter(name);
    }

    /**
     * Log remove explorer
     *
     * @param name Name of removed explorer
     */
    void onRemoveExplorer(String name)
    {
        logger.logRemovedExplorer(name);
    }

    /**
     * Log remove hostile
     *
     * @param name Name of removed hostile
     */
    void onRemoveHostile(String name)
    {
        logger.logRemovedHostile(name);
    }

    /**
     * Log on set max players
     *
     * @param maxPlayerCount Max player count
     */
    void onSetMaxPlayers(int maxPlayerCount)
    {
        logger.logSetMaxPlayers(maxPlayerCount);
    }

    /**
     * Log on set tier
     *
     * @param tier Tier
     */
    void onSetTier(TierInterface tier)
    {
        logger.logSetTier(tier);
    }

    /**
     * Log first death revival
     */
    void onUsePhoenixDown()
    {
        logger.logFirstDeathRevived();
    }
}
