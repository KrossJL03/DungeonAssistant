package bot.Battle.HostileEncounter;

import bot.Battle.Capitalizer;
import bot.Battle.Encounter;
import bot.Battle.EncounterPhaseException;
import bot.Battle.EncounterPhaseInterface;
import bot.Battle.EncounteredCreature.AttackActionResult;
import bot.Battle.EncounteredCreature.DodgeActionResult;
import bot.Battle.EncounteredCreature.EncounteredHostile;
import bot.Battle.EncounteredCreature.GuardActionResult;
import bot.Battle.EncounteredCreature.HealActionResult;
import bot.Battle.EncounteredCreature.HurtActionResult;
import bot.Battle.EncounteredCreature.LootActionResult;
import bot.Battle.EncounteredCreature.ProtectActionResult;
import bot.Battle.EncounteredCreatureInterface;
import bot.Battle.EncounteredExplorerInterface;
import bot.Battle.EncounteredHostileInterface;
import bot.Battle.InitiativeTrackerException;
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
    public HostileEncounter(@NotNull EncounterLogger logger)
    {
        super(logger, new InitiativeQueueFactory());

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
     */
    public @NotNull EncounteredHostileInterface addHostile(@NotNull Hostile hostile, @NotNull String nickname)
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
     */
    public void dodgeAction(@NotNull Player player)
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer(player);
        DodgeActionResult            result          = currentExplorer.dodge(hostileRoster.getActiveHostiles());

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
     */
    public void guardAction(@NotNull Player player)
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer(player);
        GuardActionResult            result          = currentExplorer.guard(hostileRoster.getActiveHostiles());

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
     */
    public void lootAction(@NotNull Player player)
    {
        assertLootPhase();

        EncounteredExplorerInterface encounteredExplorer = getExplorer(player);
        LootActionResult             result              = encounteredExplorer.getLoot();

        logger.logAction(result);
    }

    /**
     * Manual command to make the current explorer protect a target. Heals current explorer by given hitpoints.
     *
     * @param targetName Name of target to protect
     * @param hitpoints  Hitpoints to heal
     */
    public void manualProtectAction(@NotNull String targetName, int hitpoints)
    {
        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();

        if (hitpoints > 0) {
            heal(currentExplorer.getName(), hitpoints);
        }
        currentExplorer.giveProtectAction();

        doProtect(currentExplorer, targetName);
    }

    /**
     * Modify stat for all hostiles
     *
     * @param statName     Stat name
     * @param statModifier Amount to modify stat
     */
    public void modifyStatForAllHostiles(@NotNull String statName, int statModifier)
    {
        for (EncounteredHostileInterface hostile : hostileRoster.getActiveHostiles()) {
            modifyStat(hostile.getName(), statName, statModifier);
        }
    }

    /**
     * Pass action
     */
    public void passAction()
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
     * @param player     Owner of current explorer
     * @param targetName Name of explorer to protect
     */
    public void protectAction(@NotNull Player player, @NotNull String targetName)
    {
        assertDodgePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer(player);

        doProtect(currentExplorer, targetName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCreature(@NotNull String name)
    {
        EncounteredCreatureInterface encounterCreature = getCreature(name);
        if (encounterCreature instanceof EncounteredExplorerInterface) {
            removeExplorer((EncounteredExplorerInterface) encounterCreature);
        } else {
            removeHostile((EncounteredHostileInterface) encounterCreature);
        }
    }

    /**
     * Set stat for all hostiles
     *
     * @param statName  Stat name
     * @param statValue Stat value
     */
    public void setStatForAllHostiles(@NotNull String statName, int statValue)
    {
        for (EncounteredHostileInterface hostile : hostileRoster.getActiveHostiles()) {
            modifyStat(hostile.getName(), statName, statValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn()
    {
        assertInitiativePhase();

        EncounteredExplorerInterface currentExplorer = getCurrentExplorer();
        if (currentPhase.isAttackPhase()) {
            currentExplorer.useAllActions();
            logger.logActionAttackSkipped(currentExplorer.getName());
            handleEndOfAction();
        } else if (currentPhase.isDodgePhase()) {
            DodgeActionResult result = currentExplorer.failToDodge(hostileRoster.getActiveHostiles());
            logger.logAction(result);
            postDodgePhaseAction(currentExplorer);
            handleEndOfAction();
        }
    }

    /**
     * Start dodge phase
     *
     * @throws EncounterPhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If dodge phase is in progress
     */
    public void startDodgePhase() throws EncounterPhaseException
    {
        assertCurrentPhaseIsChangable();
        if (currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
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
    protected @NotNull AttackActionResult doAttack(
        @NotNull EncounteredExplorerInterface explorer,
        @NotNull String targetName
    )
    {
        EncounteredHostileInterface encounteredHostile = hostileRoster.getHostile(targetName);
        if (!encounteredHostile.isBloodied()) {
            addOpponentToActiveExplorers(encounteredHostile);
        }

        AttackActionResult result = explorer.attack(encounteredHostile);
        if (result.isTargetSlain()) {
            finalizeKillForExplorers(encounteredHostile);
        }

        return result;
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
    protected void postHeal(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostileInterface && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHurt(@NotNull EncounteredCreatureInterface target, @NotNull HurtActionResult result)
    {
        if (target instanceof EncounteredHostileInterface && !result.wasBloodied()) {
            addOpponentToActiveExplorers(target);
        }

        if (target.isSlain()) {
            if (target instanceof EncounteredHostile) {
                finalizeKillForExplorers(target);
            } else if (hasPhoenixDown && target instanceof EncounteredExplorerInterface) {
                usePhoenixDown((EncounteredExplorerInterface) target);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postRevive(@NotNull EncounteredCreatureInterface target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostileInterface && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preJoinPhase() throws HostileEncounterException
    {
        if (!hostileRoster.isNull() && hostileRoster.hasActiveHostiles()) {
            throw HostileEncounterException.createStartWithoutHostiles();
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
    private void assertDodgePhase() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }
    }

    /**
     * Do protect
     *
     * @param protector  Explorer doing the protecting
     * @param targetName Name of the protected target
     */
    private void doProtect(@NotNull EncounteredExplorerInterface protector, @NotNull String targetName)
    {
        EncounteredExplorerInterface protectedExplorer = getExplorer(targetName);
        ProtectActionResult result = protector.protect(
            protectedExplorer,
            hostileRoster.getActiveHostiles()
        );

        logger.logAction(result);
        postDodgePhaseAction(protector);
        handleEndOfAction();
    }

    /**
     * Finalize kill for explorers
     *
     * @param slainCreature Slain creature
     */
    private void finalizeKillForExplorers(@NotNull EncounteredCreatureInterface slainCreature)
    {
        for (EncounteredExplorerInterface explorer : getAllExplorers()) {
            explorer.finalizeKill(slainCreature);
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
        for (EncounteredExplorerInterface encounteredExplorer : getAllExplorers()) {
            encounteredExplorer.removeOpponent(hostile);
        }
    }

    /**
     * Remove encountered hostile from encounter
     *
     * @param encounteredHostile Hostile to remove
     */
    private void removeHostile(@NotNull EncounteredHostileInterface encounteredHostile)
    {
        assertNotFinalPhase();

        preRemoveHostile(encounteredHostile);
        hostileRoster.remove(encounteredHostile);
        logger.logRemovedHostile(encounteredHostile.getName());

        handleEndOfAction();
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
     * @param explorer Encountered explorer
     *
     * @throws HostileEncounterException If the explorer is not slain
     *                                   If there is no phoenix down to be used
     */
    private void usePhoenixDown(@NotNull EncounteredExplorerInterface explorer)
        throws HostileEncounterException
    {
        if (!explorer.isSlain()) {
            throw HostileEncounterException.createReviveNonSlainExplorer(explorer.getName());
        } else if (!hasPhoenixDown) {
            throw HostileEncounterException.createUsedPhoenixDown(explorer.getName());
        }

        hasPhoenixDown = false;
        HealActionResult result = explorer.healPercent((float) 0.5);
        logPhoenixDownUsed();
        logAction(result);
    }
}
