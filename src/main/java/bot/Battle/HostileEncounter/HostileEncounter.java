package bot.Battle.HostileEncounter;

import bot.Battle.AttackActionResult;
import bot.Battle.Battle;
import bot.Battle.BattlePhaseChange;
import bot.Battle.BattlePhaseException;
import bot.Battle.Capitalizer;
import bot.Battle.CombatCreature;
import bot.Battle.CombatExplorer;
import bot.Battle.EncounterException;
import bot.Battle.ExplorerRoster;
import bot.Battle.HealActionResult;
import bot.Battle.HurtActionResult;
import bot.Battle.InitiativeTrackerException;
import bot.Battle.Mention;
import bot.Battle.ModifyStatActionResult;
import bot.Explorer.Explorer;
import bot.Hostile.Hostile;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HostileEncounter extends Battle
{
    private static String BATTLE_STYLE = "Hostile Encounter";

    private ExplorerRoster<EncounteredExplorer> explorerRoster;
    private boolean                             hasPhoenixDown;
    private HostileRosterInterface              hostileRoster;
    private EncounterLogger                     logger;
    private EncounterPhaseManager               phaseManager;

    /**
     * Constructor.
     *
     * @param channel   Channel
     * @param dmMention DM mention
     */
    public HostileEncounter(@NotNull MessageChannel channel, @NotNull Mention dmMention)
    {
        super(new EncounterLogger(channel, dmMention), new InitiativeQueueFactory(), new EncounterPhaseManager());

        this.hasPhoenixDown = true;
        this.hostileRoster = new HostileRoster();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getAllCreatures()
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
     * {@inheritDoc}
     */
    @Override
    public boolean isLockingDatabase()
    {
        return !phaseManager.isFinalPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull String name)
    {
        CombatCreature creature = getCreature(name);
        if (creature instanceof EncounteredExplorer) {
            removeExplorer((EncounteredExplorer) creature);
        } else {
            removeHostile((EncounteredHostile) creature);
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
     * Add hostile
     *
     * @param hostile  Hostile
     * @param nickname Nickname
     */
    void addHostile(@NotNull Hostile hostile, @NotNull String nickname)
    {
        phaseManager.assertNotFinalPhase();

        String             capitalNickname = Capitalizer.nameCaseIfLowerCase(nickname);
        EncounteredHostile newHostile      = new EncounteredHostile(hostile, capitalNickname);

        hostileRoster.addHostile(newHostile);
        logger.logAddedHostile(newHostile);
    }

    /**
     * Dodge action
     *
     * @param player Player
     */
    void dodgeAction(@NotNull Player player)
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
     * Dodge pass request action
     *
     * @param player Player
     */
    void dodgePassRequestAction(@NotNull Player player)
    {
        logger.pingDmDodgePass(player);
    }

    /**
     * Dodge action
     *
     * @param player Player
     */
    void guardAction(@NotNull Player player)
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
     * Heal encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws BattlePhaseException If encounter is over
     */
    void heal(@NotNull String name, int hitpoints) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

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
    void healAllExplorers(int hitpoints)
    {
        for (CombatExplorer explorer : getActiveExplorers()) {
            heal(explorer.getName(), hitpoints);
        }
    }

    /**
     * Heal all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    void healAllHostiles(int hitpoints)
    {
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            heal(hostile.getName(), hitpoints);
        }
    }

    /**
     * Hurt encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws BattlePhaseException If encounter is over
     */
    void hurt(@NotNull String name, int hitpoints) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

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
    void hurtAllExplorers(int hitpoints)
    {
        for (CombatExplorer explorer : getActiveExplorers()) {
            hurt(explorer.getName(), hitpoints);
        }
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    void hurtAllHostiles(int hitpoints)
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
    void lootAction(@NotNull Player player)
    {
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isLootPhase()) {
            throw EncounterException.createWrongPhase("loot", EncounterPhase.LOOT_PHASE);
        }

        EncounteredExplorer explorer = explorerRoster.getExplorer(player);
        LootActionResult    result   = explorer.getLoot();

        logger.logAction(result);
    }

    /**
     * Manual command to make the current explorer protect a target. Heals current explorer by given hitpoints.
     *
     * @param targetName Name of target to protect
     * @param hitpoints  Hitpoints to heal
     */
    void manualProtectAction(@NotNull String targetName, int hitpoints)
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
     * Modify stat
     *
     * @param name         Name of creature to modify stat for
     * @param statName     Name of stat to modify
     * @param statModifier Modifier to apply to stat
     *
     * @throws BattlePhaseException If the encounter is over
     */
    void modifyStat(
        @NotNull String name,
        @NotNull String statName,
        int statModifier
    ) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

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
    void modifyStatForAllExplorers(@NotNull String statName, int statModifier)
    {
        for (CombatExplorer explorer : getActiveExplorers()) {
            modifyStat(explorer.getName(), statName, statModifier);
        }
    }

    /**
     * Modify stat for all hostiles
     *
     * @param statName     Stat name
     * @param statModifier Amount to modify stat
     */
    void modifyStatForAllHostiles(@NotNull String statName, int statModifier)
    {
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            modifyStat(hostile.getName(), statName, statModifier);
        }
    }

    /**
     * Pass action
     */
    void passAction()
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
    void protectAction(@NotNull Player player, @NotNull String targetName)
    {
        phaseManager.assertNotFinalPhase();
        if (phaseManager.isDodgePhase()) {
            throw EncounterException.createWrongPhase("protect", EncounterPhase.DODGE_PHASE);
        }

        EncounteredExplorer currentExplorer = getCurrentEncounteredExplorer(player);

        doProtect(currentExplorer, targetName);
    }

    /**
     * Revive an explorer and heal to half health
     *
     * @param name Encountered explorer name
     *
     * @throws BattlePhaseException If encounter is over
     */
    void revive(@NotNull String name) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

        CombatCreature   target = getCreature(name);
        HealActionResult result = target.healPercent((float) 0.5);

        postRevive(target, result);
        logger.logAction(result);
        handleEndOfAction();
    }

    /**
     * Set stat
     *
     * @param name      Name of creature to modify stat for
     * @param statName  Name of stat to modify
     * @param statValue New stat value
     *
     * @throws BattlePhaseException If the encounter is over
     */
    void setStat(@NotNull String name, @NotNull String statName, int statValue) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

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
    void setStatForAllExplorers(@NotNull String statName, int statValue)
    {
        for (CombatExplorer explorer : getActiveExplorers()) {
            setStat(explorer.getName(), statName, statValue);
        }
    }

    /**
     * Set stat for all hostiles
     *
     * @param statName  Stat name
     * @param statValue Stat value
     */
    void setStatForAllHostiles(@NotNull String statName, int statValue)
    {
        for (EncounteredHostile hostile : hostileRoster.getActiveHostiles()) {
            setStat(hostile.getName(), statName, statValue);
        }
    }

    /**
     * Start dodge phase
     *
     * @throws BattlePhaseException If the encounter is over
     *                              If the encounter has not started
     *                              If dodge phase is in progress
     */
    void startDodgePhase() throws BattlePhaseException
    {
        phaseManager.assertDodgePhaseMayStart();
        assertPlayersHaveJoined();

        BattlePhaseChange result = phaseManager.startDodgePhase();

        for (EncounteredExplorer explorer : explorerRoster.getAllExplorers()) {
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
    protected void preAttackPhase()
    {
        assertPlayersHaveJoined();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preEndPhase(@NotNull Player player)
    {
        // do nothing
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
            for (EncounteredExplorer explorer : explorerRoster.getAllExplorers()) {
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
        CombatExplorer      protectedExplorer = explorerRoster.getExplorer(targetName);
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
        for (EncounteredExplorer explorer : explorerRoster.getAllExplorers()) {
            explorer.finalizeKill((EncounteredHostile) slainCreature);
        }
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
     * Handle any additional post heal related processes
     *
     * @param target Target of healing
     * @param result Result of healing
     */
    private void postHeal(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostile && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * Handle any additional post hurt related processes
     *
     * @param target Target of hurting
     * @param result Result of hurting
     */
    private void postHurt(@NotNull CombatCreature target, @NotNull HurtActionResult result)
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
     * Handle any additional post revive related processes
     *
     * @param target Target of reviving
     * @param result Result of reviving
     */
    private void postRevive(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        if (target instanceof EncounteredHostile && result.wasTargetRevived() && !target.isBloodied()) {
            addOpponentToActiveExplorers(target);
        }
    }

    /**
     * Handle any additional pre remove hostile related processes
     *
     * @param hostile Hostile to be removed
     */
    private void preRemoveHostile(@NotNull EncounteredHostile hostile)
    {
        for (EncounteredExplorer explorer : explorerRoster.getAllExplorers()) {
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

        for (EncounteredExplorer explorer : explorerRoster.getAllExplorers()) {
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
        logger.logFirstDeathRevived();
        logAction(result);
    }
}
