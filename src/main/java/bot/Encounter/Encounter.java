package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredExplorer;
import bot.Encounter.EncounteredCreature.EncounteredHostile;
import bot.Encounter.Phase.EncounterPhaseFactory;
import bot.Explorer.Explorer;
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
    private EncounterPhaseInterface                currentPhase;
    private boolean                                hasPhoenixDown;

    /**
     * Encounter constructor
     *
     * @param listener Action listener
     */
    public Encounter(ActionListener listener)
    {
        this.currentPhase = EncounterPhaseFactory.createCreatePhase();
        this.explorerRoster = new ExplorerRoster();
        this.initiative = new InitiativeQueue();
        this.hasPhoenixDown = true;
        this.hostiles = new ArrayList<>();
        this.listener = listener;

        listener.onCreateEncounter();
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
        return newEncounteredHostile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attackAction(@NotNull Player player, @NotNull String hostileName)
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
    public void dodgeAction(@NotNull Player player) throws EncounterPhaseException, NotYourTurnException
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<EncounteredExplorerInterface> getAllExplorers()
    {
        return explorerRoster.getAllExplorers();
    }

    /**
     * Get all hostiles
     *
     * @return ArrayList<EncounteredHostileInterface>
     */
    public @NotNull ArrayList<EncounteredHostileInterface> getAllHostiles()
    {
        return new ArrayList<>(hostiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getEncounterType()
    {
        return "hostile";
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
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }

        EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw NotYourTurnException.createNotYourTurn();
        }

        GuardActionResultInterface result = currentExplorer.guard(getActiveHostiles());

        listener.onAction(result);

        if (currentExplorer.isSlain() && hasPhoenixDown) {
            usePhoenixDown(currentExplorer);
        }

        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredCreatureInterface encounterCreature = getCreature(name);
        HealActionResultInterface    result            = encounterCreature.healPoints(hitpoints);

        if (result.wasTargetRevived() && encounterCreature instanceof EncounteredHostileInterface) {
            removeKillFromExplorers(encounterCreature);
        }

        listener.onAction(result);
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
     * Heal all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            this.heal(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredCreatureInterface encounterCreature = getCreature(name);
        HurtActionResultInterface    result            = encounterCreature.hurt(hitpoints);

        listener.onAction(result);

        if (encounterCreature.isSlain()) {
            if (encounterCreature instanceof EncounteredHostile) {
                addKillToExplorers(encounterCreature);
            } else if (hasPhoenixDown && encounterCreature instanceof EncounteredExplorerInterface) {
                usePhoenixDown((EncounteredExplorerInterface) encounterCreature);
            }
        }

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
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            hurt(encounteredHostile.getName(), hitpoints);
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
    public void join(@NotNull Explorer explorer) throws EncounterPhaseException
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
            encounteredExplorer.resetActions(currentPhase.isDodgePhase());
        }

        JoinActionResultInterface result = new JoinActionResult(encounteredExplorer, explorerRoster.isFull());
        listener.onAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.getExplorer(name);
        explorerRoster.kick(encounteredExplorer);
        encounteredExplorer.useAllActions();
        listener.onKick(encounteredExplorer.getOwner());
        handleEndOfAction();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(@NotNull Player player)
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
    public void lootAction(@NotNull Player player) throws EncounterPhaseException
    {
        if (!currentPhase.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.getExplorer(player);
        LootActionResultInterface    result              = encounteredExplorer.getLoot();
        listener.onAction(result);
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
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredCreatureInterface    encounteredCreature = getCreature(name);
        ModifyStatActionResultInterface result              = encounteredCreature.modifyStat(statName, statModifier);
        explorerRoster.sort();
        listener.onAction(result);
    }

    /**
     * Pass action
     *
     * @throws EncounterPhaseException If encounter is over
     *                                 If not passable phase
     */
    public void passAction() throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isDodgePhase()) {
            throw EncounterPhaseException.createNotPassablePhase();
        }

        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAllActions();

        listener.onDodgePassAction(
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
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player)
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredExplorerInterface encounteredExplorer = explorerRoster.rejoin(player);
        if (currentPhase.isInitiativePhase()) {
            initiative.add(encounteredExplorer);
        }
        listener.onRejoin(encounteredExplorer.getName());
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
     * Revive an explorer and heal to half health
     *
     * @param name Encountered explorer name
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void revive(@NotNull String name) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredCreatureInterface encounterCreature = getCreature(name);
        HealActionResultInterface    result            = encounterCreature.healPercent((float) 0.5);

        if (result.wasTargetRevived() && encounterCreature instanceof EncounteredHostileInterface) {
            removeKillFromExplorers(encounterCreature);
        }

        listener.onAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        explorerRoster.setMaxPlayerCount(maxPlayerCount);
        listener.onSetMaxPlayers(maxPlayerCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStat(@NotNull String name, @NotNull String statName, int statValue) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        EncounteredCreatureInterface    encounteredCreature = getCreature(name);
        ModifyStatActionResultInterface result              = encounteredCreature.setStat(statName, statValue);
        explorerRoster.sort();
        listener.onAction(result);
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
        listener.onSetTier(tier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn() throws EncounterPhaseException
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
     * {@inheritDoc}
     */
    @Override
    public void startAttackPhase() throws EncounterPhaseException
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

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getAllExplorers()) {
            encounteredExplorer.resetActions(false);
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createAttackPhase();
        initiative = new InitiativeQueue(explorerRoster.getActiveExplorers());
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
    public void startDodgePhase() throws EncounterPhaseException
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

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getAllExplorers()) {
            encounteredExplorer.resetActions(true);
        }

        for (EncounteredHostileInterface encounteredHostile : getActiveHostiles()) {
            encounteredHostile.attack();
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createDodgePhase();
        initiative = new InitiativeQueue(explorerRoster.getActiveExplorers());
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEndPhaseForced()
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (currentPhase.isCreatePhase()) {
            throw EncounterPhaseException.createNotStarted();
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createEndPhase();
        initiative = new InitiativeQueue();
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
        } else if (explorerRoster.getMaxPlayerCount() == 0) {
            throw EncounterPhaseException.createStartWithoutMaxPlayers();
        } else if (hasNoActiveHostiles()) {
            throw EncounterPhaseException.createStartWithoutHostiles();
        } else if (currentPhase.isJoinPhase()) {
            throw EncounterPhaseException.createStartCurrentPhase(currentPhase.getPhaseName());
        }

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createJoinPhase();
        initiative = new InitiativeQueue();
        notifyListenerOfPhaseChange(previousPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllCurrentExplorerActions()
    {
        if (!currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isInitiativePhase()) {
            throw EncounterPhaseException.createNotInitiativePhase();
        }
        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAllActions();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction()
    {
        if (!currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        } else if (!currentPhase.isInitiativePhase()) {
            throw EncounterPhaseException.createNotInitiativePhase();
        }
        EncounteredExplorerInterface encounteredExplorer = initiative.getCurrentExplorer();
        encounteredExplorer.useAction();
        handleEndOfAction();
    }

    /**
     * Add kill to explorers
     *
     * @param target Encountered hostile
     */
    private void addKillToExplorers(@NotNull EncounteredCreatureInterface target)
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
    private @NotNull EncounteredCreatureInterface getCreature(@NotNull String name)
        throws EncounteredCreatureNotFoundException
    {
        ArrayList<EncounteredCreatureInterface> allCreatures = new ArrayList<>();
        allCreatures.addAll(explorerRoster.getAllExplorers());
        allCreatures.addAll(hostiles);
        for (EncounteredCreatureInterface creature : allCreatures) {
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
        if (!currentPhase.isJoinPhase()) {
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
    private void notifyListenerOfPhaseChange(EncounterPhaseInterface previousPhase)
    {
        PhaseChangeResult result = new PhaseChangeResult(
            currentPhase,
            previousPhase,
            explorerRoster.getTier(),
            explorerRoster.getAllExplorers(),
            getAllHostiles(),
            explorerRoster.getMaxPlayerCount(),
            explorerRoster.getOpenSlotCount()
        );

        listener.onPhaseChange(result);

        if (currentPhase.isInitiativePhase()) {
            EncounteredExplorerInterface currentExplorer = initiative.getCurrentExplorer();
            if (!currentExplorer.isActive() || !currentExplorer.hasActions()) {
                currentExplorer = initiative.getNextExplorer();
            }
            listener.onNextPlayerTurn(currentExplorer);
        }
    }

    /**
     * Remove encountered explorer from encounter
     *
     * @param encounteredExplorer Explorer to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    private void removeExplorer(EncounteredExplorerInterface encounteredExplorer) throws EncounterPhaseException
    {
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        explorerRoster.remove(encounteredExplorer);
        encounteredExplorer.useAllActions();
        listener.onRemoveExplorer(encounteredExplorer.getName());
        handleEndOfAction();
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
        if (currentPhase.isFinalPhase()) {
            throw EncounterPhaseException.createFinalPhase();
        }

        hostiles.remove(encounteredHostile);
        listener.onRemoveHostile(encounteredHostile.getName());
        handleEndOfAction();
    }

    /**
     * Remove kill from explorers
     *
     * @param target Encountered hostile
     */
    private void removeKillFromExplorers(@NotNull EncounteredCreatureInterface target)
    {
        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getActiveExplorers()) {
            encounteredExplorer.removeKill(target);
        }
    }

    /**
     * Start end phase
     */
    private void startEndPhase()
    {
        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createEndPhase();
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

        EncounterPhaseInterface previousPhase = currentPhase;
        currentPhase = EncounterPhaseFactory.createLootPhase();
        initiative = new InitiativeQueue();

        for (EncounteredExplorerInterface encounteredExplorer : explorerRoster.getAllExplorers()) {
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
        HealActionResultInterface result = encounteredExplorer.healPercent((float) 0.5);
        listener.onUsePhoenixDown();
        listener.onAction(result);
    }
}
