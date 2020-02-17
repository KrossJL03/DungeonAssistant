package bot.Battle;

import bot.Battle.HostileEncounter.EncounteredExplorer;
import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Logger.Mention;
import bot.Battle.Phase.EncounterPhaseFactory;
import bot.Explorer.Explorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class Battle implements EncounterInterface
{
    protected EncounterPhaseInterface           currentPhase;
    protected EncounterLogger                   logger;
    private   ExplorerRosterInterface           explorerRoster;
    private   InitiativeTrackerInterface        initiative;
    private   InitiativeTrackerFactoryInterface initiativeFactory;

    /**
     * Constructor.
     *
     * @param logger            Logger
     * @param initiativeFactory Initiative factory
     */
    protected Battle(
        @NotNull EncounterLogger logger,
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

        CombatExplorer currentExplorer = getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        AttackActionResult result = doAttack(currentExplorer, targetName);

        logger.logAction(result);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public @NotNull ArrayList<CombatExplorer> getAllExplorers()
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

        CombatCreature   target = getCreature(name);
        HealActionResult result = target.healPoints(hitpoints);

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
        for (CombatExplorer explorer : explorerRoster.getActiveExplorers()) {
            heal(explorer.getName(), hitpoints);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        CombatCreature   target = getCreature(name);
        HurtActionResult result = target.hurt(hitpoints);

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
        for (CombatExplorer explorer : explorerRoster.getActiveExplorers()) {
            hurt(explorer.getName(), hitpoints);
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
    public boolean isOver()
    {
        return currentPhase.isFinalPhase();
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
        CombatExplorer newExplorer = createExplorer(explorer, nickname);

        explorerRoster.addExplorer(newExplorer);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(newExplorer);
            newExplorer.resetActions(currentPhase.isDodgePhase());
        }

        JoinActionResult result = new JoinActionResult(newExplorer, explorerRoster.isFull());
        logger.logAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name)
    {
        assertNotFinalPhase();

        CombatExplorer target = explorerRoster.kick(name);
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

        CombatExplorer explorer = explorerRoster.markAsLeft(player);
        initiative.remove(explorer);
        logger.logLeftEncounter(explorer.getName());
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

        CombatCreature         target = getCreature(name);
        ModifyStatActionResult result = target.modifyStat(statName, statModifier);
        explorerRoster.sort();
        logger.logAction(result);
    }

    /**
     * Modify stat for all explorers
     *
     * @param statName     Stat name
     * @param statModifier Amount to modify stat
     */
    public void modifyStatForAllExplorers(@NotNull String statName, int statModifier)
    {
        for (CombatExplorer explorer : explorerRoster.getActiveExplorers()) {
            modifyStat(explorer.getName(), statName, statModifier);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player)
    {
        assertNotFinalPhase();

        CombatExplorer explorer = explorerRoster.markAsReturned(player);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(explorer);
        }

        logger.logRejoinEncounter(explorer.getName());
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

        CombatCreature   target = getCreature(name);
        HealActionResult result = target.healPercent((float) 0.5);

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

        CombatCreature         target = getCreature(name);
        ModifyStatActionResult result = target.setStat(statName, statValue);
        explorerRoster.sort();
        logger.logAction(result);
    }

    /**
     * Set stat for all explorers
     *
     * @param statName  Stat name
     * @param statValue Amount to modify stat
     */
    public void setStatForAllExplorers(@NotNull String statName, int statValue)
    {
        for (CombatExplorer explorer : explorerRoster.getActiveExplorers()) {
            modifyStat(explorer.getName(), statName, statValue);
        }
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

        for (CombatExplorer explorer : explorerRoster.getAllExplorers()) {
            explorer.resetActions(false);
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

        CombatExplorer explorer = getCurrentExplorer();
        explorer.useAllActions();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction()
    {
        assertInitiativePhase();

        CombatExplorer explorer = getCurrentExplorer();
        explorer.useAction();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useItemAction(@NotNull Player player)
    {
        if (!currentPhase.isAttackPhase()) {
            return;
        }

        CombatExplorer explorer = getCurrentExplorer(player);

        explorer.useAction();
        handleEndOfAction();
    }

    /**
     * Assert that the current phase can be changed
     *
     * @throws EncounterPhaseException If the battle is in it's final phase
     *                                 If the battle is still in the create phase
     * @throws EncounterException      If no players have joined
     */
    final protected void assertCurrentPhaseIsChangable() throws EncounterPhaseException, EncounterException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (haveNoPlayersJoined()) {
            throw EncounterException.createNoPlayersHaveJoined();
        }
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
     * Assert that the current phase is the loot phase
     *
     * @throws EncounterPhaseException If not loot phase
     */
    final protected void assertLootPhase() throws EncounterPhaseException
    {
        if (!currentPhase.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
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
     * Create explorer
     *
     * @param explorer Explorer
     * @param nickname Explorer nickname
     *
     * @return CombatExplorer
     */
    abstract protected CombatExplorer createExplorer(@NotNull Explorer explorer, @Nullable String nickname);

    /**
     * Do attack target
     *
     * @param explorer   The attacker
     * @param targetName The name of the target
     *
     * @return AttackActionResult
     */
    protected abstract @NotNull AttackActionResult doAttack(
        @NotNull CombatExplorer explorer,
        @NotNull String targetName
    );

    /**
     * Get all active explorers
     *
     * @return ArrayList
     */
    final protected @NotNull ArrayList<CombatExplorer> getActiveExplorers()
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
    final protected @NotNull CombatCreature getCreature(@NotNull String name)
        throws EncounteredCreatureNotFoundException
    {
        for (CombatCreature creature : getAllCreatures()) {
            if (creature.isName(name)) {
                return creature;
            }
        }

        throw EncounteredCreatureNotFoundException.createForCreature(name);
    }

    /**
     * Get current explorer
     *
     * @return EncounteredExplorer
     */
    final protected @NotNull CombatExplorer getCurrentExplorer()
    {
        return initiative.getCurrentExplorer();
    }

    /**
     * Get current explorer
     *
     * @param player The player that should be the current explorer's owner
     *
     * @return CombatExplorer
     *
     * @throws NotYourTurnException If the given player is not the owner of the current explorer
     */
    final protected @NotNull CombatExplorer getCurrentExplorer(@NotNull Player player) throws NotYourTurnException
    {
        CombatExplorer explorer = initiative.getCurrentExplorer();
        if (!explorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        return explorer;
    }

    /**
     * Get encountered explorer by name
     *
     * @param name Name of explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getExplorer(@NotNull String name)
    {
        return explorerRoster.getExplorer(name);
    }

    /**
     * Get encountered explorer by player
     *
     * @param player Owner of explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getExplorer(@NotNull Player player)
    {
        return explorerRoster.getExplorer(player);
    }

    /**
     * Get next explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getNextExplorer()
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
            CombatExplorer explorer = getCurrentExplorer();
            if (!explorer.isActive() || !explorer.hasActions()) {
                explorer = getNextExplorer();
            }
            logger.pingPlayerTurn(explorer);
        }
    }

    /**
     * Handle any additional post heal related processes
     *
     * @param target Target of healing
     * @param result Result of healing
     */
    abstract protected void postHeal(@NotNull CombatCreature target, @NotNull HealActionResult result);

    /**
     * Handle any additional post hurt related processes
     *
     * @param target Target of hurting
     * @param result Result of hurting
     */
    abstract protected void postHurt(@NotNull CombatCreature target, @NotNull HurtActionResult result);

    /**
     * Handle any additional post revive related processes
     *
     * @param target Target of reviving
     * @param result Result of reviving
     */
    abstract protected void postRevive(@NotNull CombatCreature target, @NotNull HealActionResult result);

    /**
     * handle any additional pre join phase related processes
     */
    abstract protected void preJoinPhase();

    /**
     * Remove encountered explorer from battle
     *
     * @param explorer Explorer to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    final protected void removeExplorer(EncounteredExplorer explorer) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        explorerRoster.remove(explorer);
        initiative.remove(explorer);
        explorer.useAllActions();
        logger.logRemovedExplorer(explorer.getName());
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
     * Have players joined
     *
     * @return boolean
     */
    private boolean haveNoPlayersJoined()
    {
        return currentPhase.isJoinPhase() && !explorerRoster.hasAtLeastOneActiveExplorer();
    }
}