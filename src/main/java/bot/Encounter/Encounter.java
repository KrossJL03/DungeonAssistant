package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredExplorer;
import bot.Encounter.EncounteredCreature.EncounteredHostile;
import bot.Explorer.Explorer;
import bot.Encounter.Exception.*;
import bot.Encounter.Tier.Tier;
import bot.Hostile.Hostile;
import bot.Player.Player;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Encounter implements EncounterInterface
{
    private ActionListener                         listener;
    private ArrayList<EncounteredHostileInterface> hostiles;
    private ExplorerRoster                         explorerRoster;
    private InitiativeQueue                        initiative;
    private EncounterPhase                         currentPhase;
    private boolean                                hasPhoenixDown;

    /**
     * Encounter constructor
     */
    public Encounter()
    {
        this.currentPhase = EncounterPhase.createCreatePhase();
        this.initiative = new InitiativeQueue();
        this.hasPhoenixDown = true;
        this.hostiles = new ArrayList<>();
        this.explorerRoster = new ExplorerRoster();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isOver() {
        return this.isLootPhase() || this.isEndPhase();
    }

    /**
     * Add hostile
     *
     * @param hostile  Hostile
     * @param nickname Nickname
     *
     * @throws EncounterPhaseException If encounter is over
     * @throws HostileRosterException  If nickname is in use
     */
    void addHostile(@NotNull Hostile hostile, @NotNull String nickname)
        throws EncounterPhaseException, HostileRosterException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        String                      capitalNickname       = WordUtils.capitalizeFully(nickname);
        EncounteredHostileInterface newEncounteredHostile = new EncounteredHostile(hostile, capitalNickname);
        String                      hostileSpecies        = hostile.getSpecies();
        String                      nicknameToLower       = nickname.toLowerCase();
        if (hostileSpecies.toLowerCase().equals(nicknameToLower)) {
            // todo clean up
            int speciesCount = 0;
            for (EncounteredHostileInterface encounteredHostile : hostiles) {
                if (encounteredHostile.getSpecies().equals(newEncounteredHostile.getSpecies())) {
                    if (encounteredHostile.getSpecies().equals(newEncounteredHostile.getName())) {
                        encounteredHostile.setName(encounteredHostile.getName() + "A");
                    }
                    speciesCount++;
                }
            }
            if (speciesCount > 0) {
                char letter = (char) (65 + speciesCount);
                newEncounteredHostile = new EncounteredHostile(hostile, hostile.getSpecies() + letter);
            }
        } else {
            for (EncounteredHostileInterface hostileData : hostiles) {
                if (hostileData.getName().toLowerCase().equals(nicknameToLower)) {
                    throw HostileRosterException.createNicknameInUse(nickname);
                }
            }
        }
        hostiles.add(newEncounteredHostile);
        listener.onAddHostile(newEncounteredHostile);
    }

    /**
     * Attack action
     *
     * @param player      Player
     * @param hostileName Hostile name
     *
     * @throws EncounterPhaseException If not attack phase
     * @throws NotYourTurnException    If it is not the player's turn
     */
    void attackAction(@NotNull Player player, @NotNull String hostileName)
        throws EncounterPhaseException, NotYourTurnException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isAttackPhase()) {
            throw EncounterPhaseException.createNotAttackPhase();
        }

        EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        EncounteredHostileInterface encounteredHostile = getHostile(hostileName);
        AttackActionResultInterface result             = currentExplorer.attack(encounteredHostile);

        if (result.isTargetSlain()) {
            addKillToExplorers(encounteredHostile);
        }

        listener.onAction(result);
        handleEndOfAction();
    }

    /**
     * Dodge action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     * @throws NotYourTurnException    If it is not the player's turn
     */
    void dodgeAction(@NotNull Player player) throws EncounterPhaseException, NotYourTurnException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }

        EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        DodgeActionResultInterface result = currentExplorer.dodge(getActiveHostiles());

        listener.onAction(result);

        if (currentExplorer.isSlain() && hasPhoenixDown) {
            usePhoenixDown(currentExplorer);
        }

        handleEndOfAction();
    }

    /**
     * Dodge pass action
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    void dodgePassAction() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }

        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAction();

        listener.onDodgePassAction(
            encounteredExplorer.getName(),
            encounteredExplorer.getCurrentHP(),
            encounteredExplorer.getMaxHP()
        );
    }

    /**
     * Get all explorers
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        return explorerRoster.getAllExplorers();
    }

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles()
    {
        return new ArrayList<>(hostiles);
    }

    /**
     * Heal encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounterCreatureInterface encounterCreature = getCreature(name);
        HealActionResultInterface  result            = encounterCreature.healPoints(hitpoints);

        listener.onAction(result);
    }

    /**
     * Heal all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    void healAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            heal(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * Heal all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    void healAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            this.heal(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * Hurt encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounterCreatureInterface encounterCreature = getCreature(name);
        HurtActionResultInterface  result            = encounterCreature.hurt(hitpoints);

        listener.onAction(result);

        if (encounterCreature.isSlain()) {
            if (encounterCreature instanceof EncounteredHostile) {
                addKillToExplorers(encounterCreature);
            } else if (encounterCreature instanceof EncounteredExplorerInterface) {
                if (encounterCreature.isSlain() && hasPhoenixDown) {
                    usePhoenixDown((EncounteredExplorerInterface) encounterCreature);
                }
            }
        }

        handleEndOfAction();
    }

    /**
     * Hurt all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    void hurtAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            hurt(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    void hurtAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            hurt(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * Join encounter
     *
     * @param explorer Explorer
     *
     * @throws EncounterPhaseException If encounter is over or has not started
     */
    void join(@NotNull Explorer explorer) throws EncounterPhaseException
    {
        if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = new EncounteredExplorer(explorer);
        explorerRoster.addExplorer(encounteredExplorer);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(encounteredExplorer);
            encounteredExplorer.resetActions();
        }

        JoinActionResultInterface result = new JoinActionResult(encounteredExplorer, explorerRoster.isFull());
        listener.onAction(result);
    }

    /**
     * Kick player
     *
     * @param EncounteredExplorerInterface explorer
     */
    void kickPlayer(@NotNull EncounteredExplorerInterface explorer) {
        explorerRoster.kick(explorer);
        if (initiative.contains(explorer)) {
            initiative.remove(explorer);
        }
    }

    /**
     * Player is leaving
     *
     * @param player Player
     */
    void leave(@NotNull Player player)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.leave(player);
        listener.onLeave(encounteredExplorer.getName());
        handleEndOfAction();
    }

    /**
     * Loot action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not loot phase
     */
    void lootAction(@NotNull Player player) throws EncounterPhaseException
    {
        if (!currentPhase.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.getExplorer(player);
        LootActionResultInterface    result              = encounteredExplorer.getLoot();
        listener.onAction(result);
    }

    /**
     * Modify stat
     *
     * @param name         Name of creature to modify stat for
     * @param statName     Name of stat to modify
     * @param statModifier Modifier to apply to stat
     *
     * @throws EncounterPhaseException If the encounter is over
     */
    void modifyStat(
        @NotNull String name,
        @NotNull String statName,
        int statModifier
    )
        throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface    encounteredExplorer = explorerRoster.getExplorer(name);
        ModifyStatActionResultInterface result              = encounteredExplorer.modifyStat(statName, statModifier);
        explorerRoster.sort();
        listener.onAction(result);
    }

    /**
     * Protect action
     *
     * @param player Owner of current explorer
     * @param name   Name of explorer to protect
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    void protectAction(@NotNull Player player, @NotNull String name)
        throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotProtectPhase();
        }

        EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        EncounteredExplorerInterface protectedCharacter = explorerRoster.getExplorer(name);
        ProtectActionResultInterface result = currentExplorer.protect(
            protectedCharacter,
            getActiveHostiles()
        );
        listener.onAction(result);

        if (currentExplorer.isSlain() && hasPhoenixDown) {
            usePhoenixDown(currentExplorer);
        }

        handleEndOfAction();
    }

    /**
     * Player is rejoining
     *
     * @param player Player
     */
    void rejoin(@NotNull Player player)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.rejoin(player);
        listener.onRejoin(encounteredExplorer.getName());
    }

    /**
     * Remove encountered explorer from encounter
     *
     * @param name Name of explorer
     */
    void removeExplorer(@NotNull String name)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.getExplorer(name);
        explorerRoster.remove(encounteredExplorer);
        initiative.remove(encounteredExplorer);

        listener.onRemoveExplorer(encounteredExplorer.getName());
        handleEndOfAction();
    }

    /**
     * Remove encountered hostile from encounter
     *
     * @param hostileName Name of hostile to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void removeHostile(@NotNull String hostileName) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredHostileInterface encounteredHostile = getHostile(hostileName);
        hostiles.remove(encounteredHostile);

        listener.onRemoveHostile(encounteredHostile.getName());
        handleEndOfAction();
    }

    /**
     * Set listener
     *
     * @param listener Listener
     */
    void setListener(ActionListener listener)
    {
        this.listener = listener;
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max amount of players allowed for this encounter
     */
    void setMaxPlayerCount(int maxPlayerCount)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        explorerRoster.setMaxPlayerCount(maxPlayerCount);
        listener.onSetMaxPlayers(maxPlayerCount);
    }

    /**
     * Set tier
     *
     * @param tier Tier
     *
     * @throws EncounterPhaseException If not create phase
     */
    void setTier(@NotNull Tier tier) throws EncounterPhaseException {
        if (!currentPhase.equals(Encounter.CREATE_PHASE)) {
            throw EncounterPhaseException.createSetTierAfterCreatePhase();
        }
        pcRoster.setTier(tier);
    }

    /**
     * Skip current player turn
     *
     * @throws EncounterPhaseException If encounter is over
     */
    void skipCurrentPlayerTurn() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isInitiativePhase()) {
            throw EncounterPhaseException.createNotInitiativePhase();
        }

        EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
        if (currentPhase.isAttackPhase()) {
            currentExplorer.useAllActions();
            listener.onAttackActionSkipped(currentExplorer.getName());
            handleEndOfAction();
        } else if (currentPhase.isDodgePhase()) {
            DodgeActionResultInterface result = currentExplorer.failToDodge(getActiveHostiles());
            listener.onAction(result);
            handleEndOfAction();
        }
    }

    /**
     * Start attack phase
     *
     * @throws EncounterException      If no players have joined
     * @throws EncounterPhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If attack phase is in progress
     */
    void startAttackPhase() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (haveNoPlayersJoined()) {
            throw EncounterException.createNoPlayersHaveJoined();
        } else if (currentPhase.isAttackPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            encounteredExplorer.resetActions();
        }

        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createAttackPhase();
        initiative = new InitiativeQueue(getAllExplorers());
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Start dodge phase
     *
     * @throws EncounterException      If no players have joined
     * @throws EncounterPhaseException If the encounter is over
     *                                 If the encounter has not started
     *                                 If dodge phase is in progress
     */
    void startDodgePhase() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (haveNoPlayersJoined()) {
            throw EncounterException.createNoPlayersHaveJoined();
        } else if (currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            encounteredExplorer.resetActions();
        }

        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            encounteredHostile.attack();
        }

        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createDodgePhase();
        initiative = new InitiativeQueue(getAllExplorers());
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Start end phase
     */
    void startEndPhaseForced()
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        }

        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createEndPhase();
        initiative = new InitiativeQueue();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Start join phase
     *
     * @throws EncounterPhaseException If encounter is over
     *                                 If encounter has already started
     *                                 If max players has not beet set
     *                                 If hostiles have not been added
     */
    void startJoinPhase() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createStartInProgressEncounter();
        } else if (explorerRoster.getMaxPlayerCount() == 0) {
            throw EncounterPhaseException.createStartWithoutMaxPlayers();
        } else if (hasNoActiveHostiles()) {
            throw EncounterPhaseException.createStartWithoutHostiles();
        } else if (currentPhase.isJoinPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createJoinPhase();
        initiative = new InitiativeQueue();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Use all current explorer actions
     */
    void useAllCurrentExplorerActions()
    {
        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAllActions();
    }

    /**
     * Use current explorer action
     */
    void useCurrentExplorerAction()
    {
        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAction();
    }

    /**
     * Add kill to explorers
     *
     * @param target Encountered hostile
     */
    private void addKillToExplorers(@NotNull EncounterCreatureInterface target)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            encounteredExplorer.addKill(target);
        }
    }

    /**
     * Get active hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    private @NotNull ArrayList<EncounteredHostileInterface> getActiveHostiles()
    {
        ArrayList<EncounteredHostileInterface> activeHostiles = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : hostiles) {
            if (!encounteredHostile.isSlain()) {
                activeHostiles.add(encounteredHostile);
            }
        }
        return activeHostiles;
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
    private @NotNull EncounterCreatureInterface getCreature(@NotNull String name)
        throws EncounteredCreatureNotFoundException
    {
        ArrayList<EncounterCreatureInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(explorerRoster.getAllExplorers());
        allCreatures.addAll(hostiles);
        for (EncounterCreatureInterface creature : allCreatures) {
            if (creature.isName(name)) {
                return creature;
            }
        }
        throw EncounteredCreatureNotFoundException.createForCreature(name);
    }

    /**
     * Get hostile
     *
     * @param name Name of hostile to find
     *
     * @return EncounteredHostileInterface
     *
     * @throws EncounteredCreatureNotFoundException If hostile with name not found
     * @throws HostileRosterException               If hostile is slain
     */
    private @NotNull EncounteredHostileInterface getHostile(@NotNull String name) throws HostileRosterException
    {
        for (EncounteredHostileInterface encounteredHostile : hostiles) {
            if (encounteredHostile.isName(name)) {
                if (encounteredHostile.isSlain()) {
                    throw HostileRosterException.createIsSlain(
                        encounteredHostile.getName(),
                        encounteredHostile.getSlayer().getName()
                    );
                }
                return encounteredHostile;
            }
        }
        throw EncounteredCreatureNotFoundException.createForHostile(name);
    }

    /**
     * Handle the end of an action
     */
    private void handleEndOfAction()
    {
        if (hasNoActiveHostiles()) {
            startLootPhase();
        } else if (!explorerRoster.hasActiveExplorers()) {
            startEndPhase();
        } else if (currentPhase.isInitiativePhase()) {
            EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
            if (currentExplorer.isActive() && currentExplorer.hasActions()) {
                listener.onActionsRemaining(currentExplorer.getName(), currentExplorer.getRemainingActions());
            } else {
                try {
                    listener.onNextPlayerTurn(initiative.getNextExplorer());
                } catch (InitiativeQueueException exception) {
                    startRpPhase();
                }
            }
        }
    }

    /**
     * Has active hostiles
     *
     * @return boolean
     */
    private boolean hasNoActiveHostiles()
    {
        return getActiveHostiles().size() < 1;
    }

    /**
     * Have players joined
     *
     * @return boolean
     */
    private boolean haveNoPlayersJoined()
    {
        return currentPhase.isJoinPhase() && !explorerRoster.hasActiveExplorers();
    }

    /**
     * Notify listener of phase change
     *
     * @param previousPhase Previous phase
     */
    private void notifyListenerOfPhaseChange(EncounterPhase previousPhase)
    {
        PhaseChangeResult result = new PhaseChangeResult(
            currentPhase,
            previousPhase,
            explorerRoster.getAllExplorers(),
            getAllHostiles(),
            explorerRoster.getMaxPlayerCount(),
            explorerRoster.getOpenSlotCount()
        );

        listener.onPhaseChange(result);

        if (currentPhase.isInitiativePhase()) {
            listener.onNextPlayerTurn(initiative.getCurrentExplorer());
        }
    }

    /**
     * Start end phase
     */
    private void startEndPhase()
    {
        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createEndPhase();
        initiative = new InitiativeQueue();
        notifyListenerOfPhaseChange(previousPhase);
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

        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createLootPhase();
        initiative = new InitiativeQueue();

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getAllExplorers()) {
            encounteredExplorer.rollLoot();
        }

        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * Start RP phase
     */
    private void startRpPhase()
    {
        EncounterPhase previousPhase = currentPhase;
        currentPhase = EncounterPhase.createRpPhase();
        initiative = new InitiativeQueue();
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
        int hitpoints = encounteredExplorer.healPercent((float) 0.5);
        listener.onUsePhoenixDown(encounteredExplorer.getName(), hitpoints);
    }
}
