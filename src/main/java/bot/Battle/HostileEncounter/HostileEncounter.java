package bot.Battle.HostileEncounter;

import bot.Battle.AttackActionResult;
import bot.Battle.Battle;
import bot.Battle.BattlePhaseChange;
import bot.Battle.BattlePhaseException;
import bot.Battle.Capitalizer;
import bot.Battle.CombatCreature;
import bot.Battle.CombatExplorer;
import bot.Battle.EncounterException;
import bot.Battle.HealActionResult;
import bot.Battle.HurtActionResult;
import bot.Battle.InitiativeTrackerException;
import bot.Battle.Logger.EncounterLogger;
import bot.Explorer.Explorer;
import bot.Hostile.Hostile;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HostileEncounter extends Battle
{
    private static String BATTLE_STYLE = "hostile";

    private boolean                hasPhoenixDown;
    private HostileRosterInterface hostileRoster;
    private EncounterPhaseManager  phaseManager;

    /**
     * Constructor.
     *
     * @param logger Logger
     */
    public HostileEncounter(@NotNull EncounterLogger logger)
    {
        super(logger, new InitiativeQueueFactory(), new EncounterPhaseManager());

        this.hasPhoenixDown = true;
        this.hostileRoster = new HostileRoster();
    }

    /**
     * Add hostile
     *
     * @param hostile  Hostile
     * @param nickname Nickname
     *
     * @return EncounteredHostile
     */
    public @NotNull EncounteredHostile addHostile(@NotNull Hostile hostile, @NotNull String nickname)
    {
        phaseManager.assertNotFinalPhase();

        String             capitalNickname = Capitalizer.nameCaseIfLowerCase(nickname);
        EncounteredHostile newHostile      = new EncounteredHostile(hostile, capitalNickname);

        hostileRoster.addHostile(newHostile);

        return newHostile;
    }

    /**
     * Dodge action
     *
     * @param player Player
     */
    public void dodgeAction(@NotNull Player player)
    {
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("dodge", EncounterPhase.DODGE_PHASE);
        }

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer(player);
        DodgeActionResult   result          = currentExplorer.dodge(hostileRoster.getActiveHostiles());

        logger.logAction(result);
        postDodgePhaseAction(currentExplorer);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ArrayList<CombatCreature> getAllCreatures()
    {
        ArrayList<CombatCreature> allCreatures = new ArrayList<>();
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
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("guard", EncounterPhase.DODGE_PHASE);
        }

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer(player);
        GuardActionResult   result          = currentExplorer.guard(hostileRoster.getActiveHostiles());

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
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            heal(hostile.getName(), hitpoints);
        }
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllHostiles(int hitpoints)
    {
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            hurt(hostile.getName(), hitpoints);
        }
    }

    /**
     * Loot action
     *
     * @param player Player
     */
    public void lootAction(@NotNull Player player)
    {
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isLootPhase()) {
            throw EncounterException.createWrongPhase("loot", EncounterPhase.LOOT_PHASE);
        }

        EncounteredExplorer explorer = getEncounteredExplorer(player);
        LootActionResult    result   = explorer.getLoot();

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
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("protect", EncounterPhase.DODGE_PHASE);
        }

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer();

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
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            modifyStat(hostile.getName(), statName, statModifier);
        }
    }

    /**
     * Pass action
     */
    public void passAction()
    {
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("pass", EncounterPhase.DODGE_PHASE);
        }

        CombatExplorer explorer = getCurrentEncounteredExplorer();
        explorer.useAllActions();

        logger.logActionDodgePass(
            explorer.getName(),
            explorer.getCurrentHP(),
            explorer.getMaxHP()
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
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("protect", EncounterPhase.DODGE_PHASE);
        }

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer(player);

        doProtect(currentExplorer, targetName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCreature(@NotNull String name)
    {
        CombatCreature creature = getCreature(name);
        if (creature instanceof EncounteredExplorer) {
            removeExplorer((EncounteredExplorer) creature);
        } else {
            removeHostile((EncounteredHostile) creature);
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
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            modifyStat(hostile.getName(), statName, statValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn()
    {
        phaseManager.assertInitiativePhase();

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer();
        if (phaseManager.isAttackPhase()) {
            currentExplorer.useAllActions();
            logger.logActionAttackSkipped(currentExplorer.getName());
            handleEndOfAction();
        } else if (phaseManager.isDodgePhase()) {
            DodgeActionResult result = currentExplorer.failToDodge(hostileRoster.getActiveHostiles());
            logger.logAction(result);
            postDodgePhaseAction(currentExplorer);
            handleEndOfAction();
        }
    }

    /**
     * Start dodge phase
     *
     * @throws BattlePhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If dodge phase is in progress
     */
    public void startDodgePhase() throws BattlePhaseException
    {
        phaseManager.assertDodgePhaseMayStart();
        assertPlayersHaveJoined();

        BattlePhaseChange result = phaseManager.startDodgePhase();

        for (EncounteredExplorer explorer : getAllEncounteredExplorer()) {
            explorer.resetActions(true);
        }

        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            hostile.attack();
        }

        restartInitiative();
        notifyListenerOfPhaseChange(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CombatExplorer createExplorer(@NotNull Explorer explorer, @Nullable String nickname)
    {
        return new EncounteredExplorer(explorer, nickname);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull AttackActionResult doAttack(@NotNull CombatExplorer explorer, @NotNull String targetName)
    {
        EncounteredHostile hostile = hostileRoster.getHostile(targetName);
        if (!hostile.isBloodied()) {
            addOpponentToActiveExplorers(hostile);
        }

        AttackActionResult result = explorer.attack(hostile);
        if (result.isTargetSlain()) {
            finalizeKillForExplorers(hostile);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleEndOfAction()
    {
        if (!phaseManager.isJoinPhase()) {
            if (!hostileRoster.hasActiveHostiles()) {
                startLootPhase();
            } else if (!hasAtLeastOneActiveExplorer()) {
                startEndPhase();
            } else if (phaseManager.isInitiativePhase()) {
                CombatExplorer currentExplorer = getCurrentEncounteredExplorer();
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
    protected void postHeal(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostile && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHurt(@NotNull CombatCreature target, @NotNull HurtActionResult result)
    {
        if (target instanceof EncounteredHostile && !result.wasBloodied()) {
            addOpponentToActiveExplorers(target);
        }

        if (target.isSlain()) {
            if (target instanceof EncounteredHostile) {
                finalizeKillForExplorers(target);
            } else if (hasPhoenixDown && target instanceof EncounteredExplorer) {
                usePhoenixDown((EncounteredExplorer) target);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postJoin(@NotNull CombatExplorer explorer)
    {
        if (phaseManager.isInitiativePhase()) {
            addToInitiative(explorer);
            explorer.resetActions(phaseManager.isDodgePhase());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postRevive(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostile && result.wasTargetRevived() && !target.isBloodied()) {
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
    private void addOpponentToActiveExplorers(@NotNull CombatCreature opponent)
    {
        if (opponent instanceof EncounteredHostile) {
            for (EncounteredExplorer explorer : getAllEncounteredExplorer()) {
                if (explorer.isActive()) {
                    explorer.addOpponent((EncounteredHostile) opponent);
                }
            }
        }
    }

    /**
     * Do protect
     *
     * @param protector  Explorer doing the protecting
     * @param targetName Name of the protected target
     */
    private void doProtect(@NotNull EncounteredExplorer protector, @NotNull String targetName)
    {
        CombatExplorer      protectedExplorer = getEncounteredExplorer(targetName);
        ProtectActionResult result            = protector.protect(protectedExplorer, hostileRoster.getActiveHostiles());

        logger.logAction(result);
        postDodgePhaseAction(protector);
        handleEndOfAction();
    }

    /**
     * Finalize kill for explorers
     *
     * @param slainCreature Slain creature
     */
    private void finalizeKillForExplorers(@NotNull CombatCreature slainCreature)
    {
        for (EncounteredExplorer explorer : getAllEncounteredExplorer()) {
            explorer.finalizeKill((EncounteredHostile) slainCreature);
        }
    }

    /**
     * Get encountered explorers
     * todo REMOVE
     *
     * @return ArrayList
     *
     * @deprecated
     */
    private @NotNull ArrayList<EncounteredExplorer> getAllEncounteredExplorer()
    {
        ArrayList<EncounteredExplorer> encounteredExplorers = new ArrayList<>();
        for (CombatExplorer explorer : getAllExplorers()) {
            if (explorer instanceof EncounteredExplorer) {
                encounteredExplorers.add((EncounteredExplorer) explorer);
            }
        }

        return encounteredExplorers;
    }

    /**
     * Get encountered explorer
     * todo REMOVE
     *
     * @return EncounteredExplorer
     *
     * @deprecated
     */
    private @NotNull EncounteredExplorer getCurrentEncounteredExplorer()
    {
        return (EncounteredExplorer) getCurrentExplorer();
    }

    /**
     * Get encountered explorer
     * todo REMOVE
     *
     * @param player Player
     *
     * @return EncounteredExplorer
     *
     * @deprecated
     */
    private @NotNull EncounteredExplorer getCurrentEncounteredExplorer(@NotNull Player player)
    {
        return (EncounteredExplorer) getCurrentExplorer(player);
    }

    /**
     * Get encountered explorer
     * todo REMOVE
     *
     * @param player Player
     *
     * @return EncounteredExplorer
     *
     * @deprecated
     */
    private @NotNull EncounteredExplorer getEncounteredExplorer(@NotNull Player player)
    {
        return (EncounteredExplorer) getExplorer(player);
    }

    /**
     * Get encountered explorer
     * todo REMOVE
     *
     * @param name Name
     *
     * @return EncounteredExplorer
     *
     * @deprecated
     */
    private @NotNull EncounteredExplorer getEncounteredExplorer(@NotNull String name)
    {
        return (EncounteredExplorer) getExplorer(name);
    }

    /**
     * Handle any additional post dodge related processes
     *
     * @param explorer Explorer
     */
    private void postDodgePhaseAction(@NotNull EncounteredExplorer explorer)
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
    private void preRemoveHostile(@NotNull EncounteredHostile hostile)
    {
        for (EncounteredExplorer explorer : getAllEncounteredExplorer()) {
            explorer.removeOpponent(hostile);
        }
    }

    /**
     * Remove hostile from encounter
     *
     * @param hostile Hostile to remove
     */
    private void removeHostile(@NotNull EncounteredHostile hostile)
    {
        phaseManager.assertNotFinalPhase();

        preRemoveHostile(hostile);
        hostileRoster.remove(hostile);
        logger.logRemovedHostile(hostile.getName());

        handleEndOfAction();
    }

    /**
     * Start loot phase
     *
     * @throws BattlePhaseException If loot phase is in progress
     */
    private void startLootPhase() throws BattlePhaseException
    {
        BattlePhaseChange result = phaseManager.startLootPhase();

        clearInitiative();

        for (EncounteredExplorer explorer : getAllEncounteredExplorer()) {
            explorer.lootKills();
        }

        notifyListenerOfPhaseChange(result);
    }

    /**
     * Start RP phase
     */
    private void startRpPhase()
    {
        BattlePhaseChange result = phaseManager.startRpPhase();

        clearInitiative();

        notifyListenerOfPhaseChange(result);
    }

    /**
     * Revive slain explorer if they are the first explorer to be slain
     *
     * @param explorer Encountered explorer
     *
     * @throws HostileEncounterException If the explorer is not slain
     *                                   If there is no phoenix down to be used
     */
    private void usePhoenixDown(@NotNull EncounteredExplorer explorer) throws HostileEncounterException
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
