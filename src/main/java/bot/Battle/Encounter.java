package bot.Battle;

import bot.Battle.EncounteredCreature.EncounteredExplorer;
import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Logger.Mention;
import bot.Battle.Phase.EncounterPhaseFactory;
import bot.Explorer.Explorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

abstract class Encounter implements EncounterInterface
{
    protected EncounterPhaseInterface           currentPhase;
    protected EncounterLogger                   logger;
    private   ExplorerRoster                    explorerRoster;
    private   InitiativeTrackerInterface        initiative;
    private   InitiativeTrackerFactoryInterface initiativeFactory;

    /**
     * Constructor.
     *
     * @param logger            Logger
     * @param hostileRoster     Hostile roster
     * @param initiativeFactory Initiative factory
     */
    Encounter(
        @NotNull EncounterLogger logger,
        @NotNull HostileRosterInterface hostileRoster,
        @NotNull InitiativeTrackerFactoryInterface initiativeFactory
    )
    {
        this.currentPhase = EncounterPhaseFactory.createCreatePhase();
        this.explorerRoster = new ExplorerRoster();
        this.initiativeFactory = initiativeFactory;
        this.logger = logger;

        this.initiative = initiativeFactory.createNull();

        logger.logCreateEncounter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attackAction(@NotNull Player player, @NotNull String targetName)
        throws EncounterPhaseException, NotYourTurnException
    {
        assertNotFinalPhase();
        if (!currentPhase.isAttackPhase()) {
            throw EncounterPhaseException.createNotAttackPhase();
        }

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        AttackActionResultInterface result = doAttack(currentExplorer, targetName);

        logger.logAction(result);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        return explorerRoster.getAllExplorers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        EncounteredCreatureInterface target = getCreature(name);
        HealActionResultInterface    result = target.healPoints(hitpoints);

        logger.logAction(result);
        postHeal(target, result);
        handleEndOfAction();
    }

    /**
     * Heal all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            heal(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        EncounteredCreatureInterface target = getCreature(name);
        HurtActionResultInterface    result = target.hurt(hitpoints);

        logger.logAction(result);
        postHurt(target, result);
        handleEndOfAction();
    }

    /**
     * Hurt all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            hurt(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLockingDatabase()
    {
        return !currentPhase.isFinalPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(@NotNull Explorer explorer, @Nullable String nickname) throws EncounterPhaseException
    {
        assertNotFinalPhase();
        if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (!isAlwaysJoinable() && !currentPhase.isJoinPhase()) {
            throw EncounterPhaseException.createNotJoinPhase(Mention.createForPlayer(explorer.getOwner().getUserId()));
        }

        String capitalNickname = nickname != null
                                 ? Capitalizer.nameCaseIfLowerCase(nickname)
                                 : null;
        EncounteredExplorerInterface encounteredExplorer = new EncounteredExplorer(explorer, capitalNickname);

        explorerRoster.addExplorer(encounteredExplorer);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(encounteredExplorer);
            encounteredExplorer.resetActions(currentPhase.isDodgePhase());
        }

        JoinActionResultInterface result = new JoinActionResult(encounteredExplorer, explorerRoster.isFull());
        logger.logAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name)
    {
        assertNotFinalPhase();

        EncounteredExplorerInterface target = explorerRoster.kick(name);
        initiative.remove(target);
        target.useAllActions();
        logger.logKickedPlayer(target.getOwner());
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(@NotNull Player player)
    {
        assertNotFinalPhase();

        EncounteredExplorerInterface target = explorerRoster.markAsLeft(player);
        initiative.remove(target);
        logger.logLeftEncounter(target.getName());
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyStat(
        @NotNull String name,
        @NotNull String statName,
        int statModifier
    ) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        EncounteredCreatureInterface    target = getCreature(name);
        ModifyStatActionResultInterface result = target.modifyStat(statName, statModifier);
        explorerRoster.sort();
        logger.logAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player)
    {
        assertNotFinalPhase();

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.markAsReturned(player);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(encounteredExplorer);
        }

        logger.logRejoinEncounter(encounteredExplorer.getName());
    }

    /**
     * Revive an explorer and heal to half health
     *
     * @param name Encountered explorer name
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void revive(@NotNull String name) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        EncounteredCreatureInterface target = getCreature(name);
        HealActionResultInterface    result = target.healPercent((float) 0.5);

        postRevive(target, result);
        logger.logAction(result);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        explorerRoster.setMaxPlayerCount(maxPlayerCount);
        logger.logSetMaxPlayers(maxPlayerCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStat(@NotNull String name, @NotNull String statName, int statValue) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        EncounteredCreatureInterface    target = getCreature(name);
        ModifyStatActionResultInterface result = target.setStat(statName, statValue);
        explorerRoster.sort();
        logger.logAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTier(@NotNull TierInterface tier) throws EncounterPhaseException
    {
        if (!currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createSetTierAfterCreatePhase();
        }

        explorerRoster.setTier(tier);
        logger.logSetTier(tier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAttackPhase() throws EncounterPhaseException
    {
        assertCurrentPhaseIsChangable();
        if (currentPhase.isAttackPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getAllExplorers()) {
            encounteredExplorer.resetActions(false);
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createAttackPhase();
        restartInitiative();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEndPhaseForced()
    {
        assertNotFinalPhase();
        if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createEndPhase();
        clearInitiative();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startJoinPhase() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createStartInProgressEncounter();
        } else if (currentPhase.isJoinPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        preJoinPhase();

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createJoinPhase();
        clearInitiative();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllCurrentExplorerActions()
    {
        assertInitiativePhase();

        EncounteredExplorerInterface encounteredExplorer = getCurrentExplorer();
        encounteredExplorer.useAllActions();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction()
    {
        assertInitiativePhase();

        EncounteredExplorerInterface encounteredExplorer = getCurrentExplorer();
        encounteredExplorer.useAction();
        handleEndOfAction();
    }

    /**
     * Assert that the current phase is an initiative phase
     */
    protected void assertInitiativePhase()
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isInitiativePhase()) {
            throw EncounterPhaseException.createNotInitiativePhase();
        }
    }

    /**
     * Assert the current phase is not a final phase
     */
    final protected void assertNotFinalPhase()
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }
    }

    /**
     * Clear initiative
     */
    final protected void clearInitiative()
    {
        initiative = initiativeFactory.createNull();
    }

    /**
     * Do attack target
     *
     * @param explorer   The attacker
     * @param targetName The name of the target
     *
     * @return AttackActionResultInterface
     */
    protected abstract @NotNull AttackActionResultInterface doAttack(
        @NotNull EncounteredExplorerInterface explorer,
        @NotNull String targetName
    );

    /**
     * Get all active explorers
     *
     * @return ArrayList
     */
    final protected @NotNull ArrayList<EncounteredExplorerInterface> getActiveExplorers()
    {
        return explorerRoster.getActiveExplorers();
    }

    /**
     * Get creature
     *
     * @param name Name of creature to find
     *
     * @return EncounterCreatureInterface
     *
     * @throws EncounteredCreatureNotFoundException If creature with name not found
     */
    final protected @NotNull EncounteredCreatureInterface getCreature(@NotNull String name)
        throws EncounteredCreatureNotFoundException
    {
        for (EncounteredCreatureInterface creature : getAllCreatures()) {
            if (creature.isName(name)) {
                return creature;
            }
        }

        throw EncounteredCreatureNotFoundException.createForCreature(name);
    }

    /**
     * Get current explorer
     *
     * @return EncounteredExplorerInterface
     */
    final protected @NotNull EncounteredExplorerInterface getCurrentExplorer()
    {
        return initiative.getCurrentExplorer();
    }

    /**
     * Get encountered explorer by name
     *
     * @param name Name of explorer
     *
     * @return EncounteredExplorerInterface
     */
    final protected @NotNull EncounteredExplorerInterface getExplorer(@NotNull String name)
    {
        return explorerRoster.getExplorer(name);
    }

    /**
     * Get encountered explorer by player
     *
     * @param player Owner of explorer
     *
     * @return EncounteredExplorerInterface
     */
    final protected @NotNull EncounteredExplorerInterface getExplorer(@NotNull Player player)
    {
        return explorerRoster.getExplorer(player);
    }

    /**
     * Get next explorer
     *
     * @return EncounteredExplorerInterface
     */
    final protected @NotNull EncounteredExplorerInterface getNextExplorer()
    {
        return initiative.getNextExplorer();
    }

    /**
     * Handle the end of an action
     */
    abstract protected void handleEndOfAction();

    /**
     * Has at least one active explorer
     *
     * @return boolean
     */
    final protected boolean hasAtLeastOneActiveExplorer()
    {
        return explorerRoster.hasAtLeastOneActiveExplorer();
    }

    /**
     * Has multiple active explorer
     *
     * @return boolean
     */
    final protected boolean hasMultipleActiveExplorers()
    {
        return explorerRoster.hasMultipleActiveExplorers();
    }

    /**
     * Have players joined
     *
     * @return boolean
     */
    final protected boolean haveNoPlayersJoined()
    {
        return currentPhase.isJoinPhase() && !explorerRoster.hasAtLeastOneActiveExplorer();
    }

    /**
     * Can players join this encounter at any time
     */
    abstract protected boolean isAlwaysJoinable();

    /**
     * Log action
     *
     * @param result Action result
     */
    final protected void logAction(@NotNull ActionResultInterface result)
    {
        logger.logAction(result);
    }

    /**
     * Log phoenix down used
     */
    final protected void logPhoenixDownUsed()
    {
        logger.logFirstDeathRevived();
    }

    /**
     * Notify listener of phase change
     *
     * @param previousPhase Previous phase
     */
    final protected void notifyListenerOfPhaseChange(EncounterPhaseInterface previousPhase)
    {
        PhaseChangeResult result = new PhaseChangeResult(
            currentPhase,
            previousPhase,
            getAllCreatures(),
            explorerRoster.getTier(),
            explorerRoster.getMaxPlayerCount(),
            explorerRoster.getOpenSlotCount()
        );

        logger.logPhaseChange(result);

        if (currentPhase.isInitiativePhase()) {
            EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
            if (!currentExplorer.isActive() || !currentExplorer.hasActions()) {
                currentExplorer = getNextExplorer();
            }
            logger.pingPlayerTurn(currentExplorer);
        }
    }

    /**
     * Handle any additional post heal related processes
     *
     * @param target Target of healing
     * @param result Result of healing
     */
    abstract protected void postHeal(
        @NotNull EncounteredCreatureInterface target,
        @NotNull HealActionResultInterface result
    );

    /**
     * Handle any additional post hurt related processes
     *
     * @param target Target of hurting
     * @param result Result of hurting
     */
    abstract protected void postHurt(
        @NotNull EncounteredCreatureInterface target,
        @NotNull HurtActionResultInterface result
    );

    /**
     * Handle any additional post revive related processes
     *
     * @param target Target of reviving
     * @param result Result of reviving
     */
    abstract protected void postRevive(
        @NotNull EncounteredCreatureInterface target,
        @NotNull HealActionResultInterface result
    );

    /**
     * handle any additional pre join phase related processes
     */
    abstract protected void preJoinPhase();

    /**
     * Remove encountered explorer from battle
     *
     * @param target Explorer to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    final protected void removeExplorer(EncounteredExplorerInterface target) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        explorerRoster.remove(target);
        initiative.remove(target);
        target.useAllActions();
        logger.logRemovedExplorer(target.getName());
        handleEndOfAction();
    }

    /**
     * Restart initiative
     */
    final protected void restartInitiative()
    {
        initiative = initiativeFactory.create(explorerRoster.getActiveExplorers());
    }

    /**
     * Start end phase
     */
    final protected void startEndPhase()
    {
        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createEndPhase();
        clearInitiative();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Assert that the current phase can be changed
     *
     * @throws EncounterPhaseException If the battle is in it's final phase
     *                                 If the battle is still in the create phase
     * @throws EncounterException      If no players have joined
     */
    private void assertCurrentPhaseIsChangable() throws EncounterPhaseException, EncounterException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (haveNoPlayersJoined()) {
            throw EncounterException.createNoPlayersHaveJoined();
        }
    }
}