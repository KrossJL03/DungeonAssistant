package bot.Battle;

import bot.Battle.Logger.EncounterLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerVsPlayer extends Encounter
{
    /**
     * Constructor.
     *
     * @param logger Logger
     */
    PlayerVsPlayer(@NotNull EncounterLogger logger)
    {
        super(logger, new NullHostileRoster(), new InitiativeCycleFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<EncounteredCreatureInterface> getAllCreatures()
    {
        return new ArrayList<>(getAllExplorers());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getBattleStyle()
    {
        return "pvp";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCreature(@NotNull String name) throws EncounterPhaseException
    {
        EncounteredCreatureInterface encounterCreature = getCreature(name);
        if (encounterCreature instanceof EncounteredExplorerInterface) {
            removeExplorer((EncounteredExplorerInterface) encounterCreature);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn() throws EncounterPhaseException
    {
        assertInitiativePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (currentPhase.isAttackPhase()) {
            currentExplorer.useAllActions();
            logger.logActionAttackSkipped(currentExplorer.getName());
            handleEndOfAction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull AttackActionResultInterface doAttack(
        @NotNull EncounteredExplorerInterface explorer,
        @NotNull String targetName
    )
    {
        EncounteredExplorerInterface target = getExplorer(targetName);
        return explorer.attack(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleEndOfAction()
    {
        if (!currentPhase.isJoinPhase()) {
            if (!hasMultipleActiveExplorers()) {
                startEndPhase();
            } else if (currentPhase.isInitiativePhase()) {
                EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
                if (currentExplorer.isActive() && currentExplorer.hasActions()) {
                    logger.logActionsRemaining(currentExplorer.getName(), currentExplorer.getRemainingActions());
                } else {
                    logger.pingPlayerTurn(getNextExplorer());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAlwaysJoinable()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHeal(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResultInterface result)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHurt(
        @NotNull EncounteredCreatureInterface target, @NotNull HurtActionResultInterface result
    )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postRevive(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResultInterface result)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preJoinPhase()
    {
        // do nothing
    }
}
