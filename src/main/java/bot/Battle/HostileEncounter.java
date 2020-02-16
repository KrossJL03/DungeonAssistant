package bot.Battle;

import bot.Battle.EncounteredCreature.EncounteredHostile;
import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Phase.EncounterPhaseFactory;
import bot.Hostile.Hostile;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HostileEncounter extends Encounter
{
    private static String BATTLE_STYLE = "hostile";

    private boolean                hasPhoenixDown;
    private HostileRosterInterface hostileRoster;

    /**
     * Constructor.
     *
     * @param logger Logger
     */
    HostileEncounter(@NotNull EncounterLogger logger)
    {
        super(logger, new HostileRoster(), new InitiativeQueueFactory());

        this.hasPhoenixDown = true;
        this.hostileRoster = new HostileRoster();
    }

    /**
     * Add hostile
     *
     * @param hostile  Hostile
     * @param nickname Nickname
     *
     * @return EncounteredHostileInterface
     *
     * @throws EncounterPhaseException If encounter is over
     * @throws HostileRosterException  If nickname is in use
     */
    public @NotNull EncounteredHostileInterface addHostile(@NotNull Hostile hostile, @NotNull String nickname)
        throws EncounterPhaseException, HostileRosterException
    {
        assertNotFinalPhase();

        String                      capitalNickname       = Capitalizer.nameCaseIfLowerCase(nickname);
        EncounteredHostileInterface newEncounteredHostile = new EncounteredHostile(hostile, capitalNickname);

        hostileRoster.addHostile(newEncounteredHostile);

        return newEncounteredHostile;
    }

    /**
     * Dodge action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     * @throws NotYourTurnException    If it is not the player's turn
     */
    public void dodgeAction(@NotNull Player player) throws EncounterPhaseException, NotYourTurnException
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        DodgeActionResultInterface result = currentExplorer.dodge(hostileRoster.getActiveHostiles());

        logger.logAction(result);
        postDodgePhaseAction(currentExplorer);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ArrayList<EncounteredCreatureInterface> getAllCreatures()
    {
        ArrayList<EncounteredCreatureInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(getAllExplorers());
        allCreatures.addAll(hostileRoster.getAllHostiles());

        return allCreatures;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getBattleStyle()
    {
        return BATTLE_STYLE;
    }

    /**
     * Dodge action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     * @throws NotYourTurnException    If it is not the player's turn
     */
    public void guardAction(@NotNull Player player) throws EncounterPhaseException, NotYourTurnException
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        GuardActionResultInterface result = currentExplorer.guard(hostileRoster.getActiveHostiles());

        logger.logAction(result);
        postDodgePhaseAction(currentExplorer);
        handleEndOfAction();
    }

    /**
     * Heal all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : hostileRoster.getActiveHostiles()) {
            heal(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : hostileRoster.getActiveHostiles()) {
            hurt(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * Loot action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not loot phase
     */
    public void lootAction(@NotNull Player player) throws EncounterPhaseException
    {
        if (!currentPhase.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = getExplorer(player);
        LootActionResultInterface    result              = encounteredExplorer.getLoot();
        logger.logAction(result);
    }

    /**
     * Pass action
     *
     * @throws EncounterPhaseException If encounter is over
     *                                 If not passable phase
     */
    public void passAction() throws EncounterPhaseException
    {
        assertDodgePhase();

        EncounteredExplorerInterface encounteredExplorer = getCurrentExplorer();
        encounteredExplorer.useAllActions();

        logger.logActionDodgePass(
            encounteredExplorer.getName(),
            encounteredExplorer.getCurrentHP(),
            encounteredExplorer.getMaxHP()
        );

        handleEndOfAction();
    }

    /**
     * Protect action
     *
     * @param player Owner of current explorer
     * @param name   Name of explorer to protect
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    public void protectAction(@NotNull Player player, @NotNull String name)
        throws EncounterPhaseException
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        EncounteredExplorerInterface protectedCharacter = getExplorer(name);
        ProtectActionResultInterface result = currentExplorer.protect(
            protectedCharacter,
            hostileRoster.getActiveHostiles()
        );

        logger.logAction(result);
        postDodgePhaseAction(currentExplorer);
        handleEndOfAction();
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
        } else {
            removeHostile((EncounteredHostileInterface) encounterCreature);
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
        } else if (currentPhase.isDodgePhase()) {
            DodgeActionResultInterface result = currentExplorer.failToDodge(hostileRoster.getActiveHostiles());
            logger.logAction(result);
            postDodgePhaseAction(currentExplorer);
            handleEndOfAction();
        }
    }

    /**
     * Start dodge phase
     *
     * @throws EncounterException      If no players have joined
     * @throws EncounterPhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If dodge phase is in progress
     */
    public void startDodgePhase() throws EncounterPhaseException
    {
        assertNotFinalPhase();
        if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (haveNoPlayersJoined()) {
            throw EncounterException.createNoPlayersHaveJoined();
        } else if (currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        } else if (hostileRoster.isNull()) {
            throw HostileRosterException.createNullRoster();
        }

        for (EncounteredExplorerInterface encounteredExplorer : getAllExplorers()) {
            encounteredExplorer.resetActions(true);
        }

        for (EncounteredHostileInterface encounteredHostile : hostileRoster.getActiveHostiles()) {
            encounteredHostile.attack();
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createDodgePhase();
        restartInitiative();
        notifyListenerOfPhaseChange(previousPhase);
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
        EncounteredHostileInterface encounteredHostile = getHostile(targetName);
        if (!encounteredHostile.isBloodied()) {
            addOpponentToActiveExplorers(encounteredHostile);
        }

        AttackActionResultInterface result = explorer.attack(encounteredHostile);
        if (result.isTargetSlain()) {
            removeOpponentFromInactiveExplorers(encounteredHostile);
        }

        return result;
    }

    /**
     * Get hostile by name
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostileInterface
     */
    final protected @NotNull EncounteredHostileInterface getHostile(@NotNull String name)
    {
        return hostileRoster.getHostile(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleEndOfAction()
    {
        if (!currentPhase.isJoinPhase()) {
            if (!hostileRoster.hasActiveHostiles()) {
                startLootPhase();
            } else if (!hasAtLeastOneActiveExplorer()) {
                startEndPhase();
            } else if (currentPhase.isInitiativePhase()) {
                EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
                if (currentExplorer.isActive() && currentExplorer.hasActions()) {
                    logger.logActionsRemaining(currentExplorer.getName(), currentExplorer.getRemainingActions());
                } else {
                    try {
                        logger.pingPlayerTurn(getNextExplorer());
                    } catch (InitiativeTrackerException exception) {
                        startRpPhase();
                    }
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHeal(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResultInterface result)
    {
        if (target instanceof EncounteredHostileInterface && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHurt(@NotNull EncounteredCreatureInterface target, @NotNull HurtActionResultInterface result)
    {
        if (target instanceof EncounteredHostileInterface && !result.wasBloodied()) {
            addOpponentToActiveExplorers(target);
        }

        if (target.isSlain()) {
            if (target instanceof EncounteredHostile) {
                removeOpponentFromInactiveExplorers(target);
            } else if (hasPhoenixDown && target instanceof EncounteredExplorerInterface) {
                usePhoenixDown((EncounteredExplorerInterface) target);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postRevive(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResultInterface result)
    {
        if (target instanceof EncounteredHostileInterface && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preJoinPhase()
    {
        if (!hostileRoster.isNull() && hostileRoster.hasActiveHostiles()) {
            throw EncounterPhaseException.createStartWithoutHostiles();
        }
    }

    /**
     * Add opponent to active explorers
     *
     * @param opponent Opponent
     */
    private void addOpponentToActiveExplorers(@NotNull EncounteredCreatureInterface opponent)
    {
        if (opponent instanceof EncounteredHostileInterface) {
            for (EncounteredExplorerInterface encounteredExplorer : getActiveExplorers()) {
                encounteredExplorer.addOpponent(opponent);
            }
        }
    }

    /**
     * Assert that the current phase is the dodge phase
     */
    private void assertDodgePhase()
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }
    }

    /**
     * Handle any additional post dodge related processes
     *
     * @param explorer Explorer
     */
    private void postDodgePhaseAction(@NotNull EncounteredExplorerInterface explorer)
    {
        if (explorer.isSlain() && hasPhoenixDown) {
            usePhoenixDown(explorer);
        }
    }

    /**
     * Handle any additional pre remove hostile related processes
     *
     * @param hostile Hostile to be removed
     */
    private void preRemoveHostile(@NotNull EncounteredHostileInterface hostile)
    {
        removeOpponentFromAllExplorers(hostile);
    }

    /**
     * Remove encountered hostile from encounter
     *
     * @param encounteredHostile Hostile to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    private void removeHostile(@NotNull EncounteredHostileInterface encounteredHostile) throws EncounterPhaseException
    {
        assertNotFinalPhase();

        preRemoveHostile(encounteredHostile);
        hostileRoster.remove(encounteredHostile);
        logger.logRemovedHostile(encounteredHostile.getName());

        handleEndOfAction();
    }

    /**
     * Remove opponent from all explorers
     *
     * @param opponent Opponent to remove
     */
    private void removeOpponentFromAllExplorers(@NotNull EncounteredCreatureInterface opponent)
    {
        for (EncounteredExplorerInterface encounteredExplorer : getAllExplorers()) {
            encounteredExplorer.removeOpponent(opponent);
        }
    }

    /**
     * Remove opponent from non-active explorers
     * Players must be active prior to an opponent being bloodied and when they are slain in order to earn loot
     *
     * @param slainCreature Slain creature
     */
    private void removeOpponentFromInactiveExplorers(@NotNull EncounteredCreatureInterface slainCreature)
    {
        for (EncounteredExplorerInterface encounteredExplorer : getAllExplorers()) {
            if (!encounteredExplorer.isActive()) {
                encounteredExplorer.removeOpponent(slainCreature);
            }
        }
    }

    /**
     * Start loot phase
     *
     * @throws EncounterPhaseException If loot phase is in progress
     */
    private void startLootPhase() throws EncounterPhaseException
    {
        if (currentPhase.isLootPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createLootPhase();
        clearInitiative();

        for (EncounteredExplorerInterface encounteredExplorer : getAllExplorers()) {
            encounteredExplorer.rollKillLoot();
        }

        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Start RP phase
     */
    private void startRpPhase()
    {
        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createRpPhase();
        clearInitiative();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Revive slain explorer if they are the first explorer to be slain
     *
     * @param encounteredExplorer Encountered explorer
     *
     * @throws EncounterException If the encountered explorer is not slain
     *                            If there is no phoenix down to be used
     */
    private void usePhoenixDown(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        if (!encounteredExplorer.isSlain()) {
            throw EncounterException.createReviveNonSlainExplorer(encounteredExplorer.getName());
        } else if (!hasPhoenixDown) {
            throw EncounterException.createUsedPhoenixDown(encounteredExplorer.getName());
        }

        hasPhoenixDown = false;
        HealActionResultInterface result = encounteredExplorer.healPercent((float) 0.5);
        logPhoenixDownUsed();
        logAction(result);
    }
}
