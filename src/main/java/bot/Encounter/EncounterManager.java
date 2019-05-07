package bot.Encounter;

import bot.Encounter.EncounteredCreature.*;
import bot.Encounter.Exception.*;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.EncounterLoggerContext;
import bot.Explorer.Explorer;
import bot.Hostile.HostileManager;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

public class EncounterManager
{
    private Encounter              encounter;
    private EncounterLogger        logger;
    private EncounterLoggerContext loggerContext;

    /**
     * EncounterManager constructor
     *
     * @param encounter     Encounter
     * @param logger        Encounter logger
     * @param loggerContext Encounter logger encounter
     */
    public @NotNull EncounterManager(
        @NotNull Encounter encounter,
        @NotNull EncounterLogger logger,
        @NotNull EncounterLoggerContext loggerContext
    )
    {
        this.encounter = encounter;
        this.logger = logger;
        this.loggerContext = loggerContext; // todo move to logger only
    }

    /**
     * Add hostile to encounter
     *
     * @param speciesName Hostile species name
     * @param nickname    Hostile name
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void addHostile(@NotNull String speciesName, @NotNull String nickname) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredHostileInterface hostileData = encounter.addHostile(
            HostileManager.getHostile(speciesName),
            nickname
        );
        logger.logAddedHostile(hostileData);
    }

    /**
     * Attack action
     *
     * @param player      Player
     * @param hostileName Hostile name
     *
     * @throws EncounterPhaseException If not attack phase
     */
    public void attackAction(@NotNull Player player, @NotNull String hostileName) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!encounter.isAttackPhase()) {
            throw EncounterPhaseException.createNotAttackPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = getCurrentExplorer(player);
        EncounteredHostileInterface  encounteredHostile  = encounter.getHostile(hostileName);

        AttackActionResultInterface result = encounteredExplorer.attack(encounteredHostile);
        logger.logAction(result);

        if (encounteredHostile.isSlain()) {
            addKillToExplorers(encounteredHostile);
        }
        endCurrentPlayerAction();
    }

    /**
     * Create encounter
     *
     * @param channel       Encounter channel
     * @param dungeonMaster Dungeon master role
     */
    public void createEncounter(@NotNull MessageChannel channel, @NotNull Role dungeonMaster)
    {
        encounter = new Encounter();
        loggerContext.setChannel(channel);
        loggerContext.setDungeonMasterMention(dungeonMaster);
        logger.logCreateEncounter();
    }

    /**
     * Dodge action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    public void dodgeAction(@NotNull Player player) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!encounter.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }

        EncounteredExplorerInterface encounteredExplorer = getCurrentExplorer(player);
        DodgeActionResultInterface   result              = encounteredExplorer.dodge(encounter.getActiveHostiles());

        logger.logAction(result);
        endCurrentPlayerAction();
    }

    /**
     * Dodge pass action
     */
    public void dodgePassAction()
    {
        EncounteredExplorerInterface encounteredExplorer = encounter.getCurrentExplorer();
        logger.logActionDodgePass(encounteredExplorer);
        encounteredExplorer.useAction();
        endCurrentPlayerAction();
    }

    /**
     * Request dodge pass action
     *
     * @param player Player
     */
    public void dodgePassActionHelp(@NotNull Player player)
    {
        logger.pingDmDodgePass(player);
    }

    /**
     * End current player action
     */
    public void endCurrentPlayersAction()
    {
        // todo rename method
        EncounteredExplorerInterface currentExplorer = encounter.getCurrentExplorer();
        currentExplorer.useAction();
        endCurrentPlayerAction();
    }

    /**
     * End current player turn
     */
    public void endCurrentPlayersTurn()
    {
        // todo rename method
        EncounteredExplorerInterface currentExplorer = encounter.getCurrentExplorer();
        currentExplorer.useAllActions();
        endCurrentPlayerAction();
    }

    /**
     * End encounter
     */
    public void endEncounter()
    {
        encounter.startEndPhase();
        logger.logEndEncounterForced(encounter.getAllExplorers(), encounter.getAllHostiles());
    }

    /**
     * Heal encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounterCreatureInterface encounterCreature = encounter.getCreature(name);
        encounterCreature.healPoints(hitpoints);
        logger.logDungeonMasterHeal(
            encounterCreature.getName(),
            hitpoints,
            encounterCreature.getCurrentHP(),
            encounterCreature.getMaxHP()
        );
    }

    /**
     * Heal all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : encounter.getActiveExplorers()) {
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
        for (EncounteredHostileInterface encounteredHostile : encounter.getActiveHostiles()) {
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
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounterCreatureInterface encounterCreature = encounter.getCreature(name);
        encounterCreature.hurt(hitpoints);
        logger.logDungeonMasterHurt(
            encounterCreature.getName(),
            hitpoints,
            encounterCreature.getCurrentHP(),
            encounterCreature.getMaxHP()
        );
        if (encounterCreature.isSlain()) {
            logger.logDungeonMasterSlay(encounterCreature.getName());
            if (encounterCreature instanceof EncounteredHostile) {
                addKillToExplorers((EncounteredHostile) encounterCreature);
                if (!encounter.hasActiveHostiles()) {
                    startLootPhase();
                }
            } else if (encounterCreature instanceof EncounteredExplorerInterface) {
                if (encounter.isInitiativePhase() && encounterCreature == encounter.getCurrentExplorer()) {
                    endCurrentPlayerAction();
                } else {
                    reviveIfFirstSlainExplorer((EncounteredExplorerInterface) encounterCreature);
                }
            }
        }
    }

    /**
     * Hurt all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllExplorers(int hitpoints)
    {
        for (EncounteredExplorerInterface encounteredExplorer : encounter.getActiveExplorers()) {
            this.hurt(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllHostiles(int hitpoints)
    {
        for (EncounteredHostileInterface encounteredHostile : encounter.getActiveHostiles()) {
            this.hurt(encounteredHostile.getName(), hitpoints);
        }
    }

    /**
     * Join encounter
     *
     * @param explorer Explorer
     *
     * @throws EncounterPhaseException If encounter is over or has not started
     */
    public void joinEncounter(@NotNull Explorer explorer) throws EncounterPhaseException
    {
        if (!encounter.isStarted()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = new EncounteredExplorer(explorer);
        encounter.addExplorer(encounteredExplorer);
        logger.logAddedExplorer(encounteredExplorer);
        if (encounter.isFullDungeon()) {
            logger.logDungeonIsFull();
        }
    }

    /**
     * Leave encounter
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void leaveEncounter(@NotNull Player player) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface currentExplorer;
        try {
            currentExplorer = encounter.getCurrentExplorer();
        } catch (NotInInitiativeException e) {
            currentExplorer = null;
        }
        EncounteredExplorerInterface encounteredExplorer = encounter.playerHasLeft(player);
        logger.logLeftEncounter(encounteredExplorer.getName());
        if (encounteredExplorer == currentExplorer) {
            endCurrentPlayerAction();
        } else if (!encounter.hasActiveExplorers()) {
            encounter.startEndPhase();
            logger.logEndEncounterLose(encounter.getAllExplorers(), encounter.getAllHostiles());
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
        if (!encounter.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = encounter.getExplorer(player);
        LootActionResultInterface    actionResult        = encounteredExplorer.getLoot();
        logger.logAction(actionResult);
    }

    /**
     * Modify stat
     *
     * @param name     Name of creature to modify stat for
     * @param statName Name of stat to modify
     * @param statMod  Mod to apply to stat
     *
     * @throws EncounterPhaseException If the encounter is over
     */
    public void modifyStat(@NotNull String name, @NotNull String statName, int statMod) throws EncounterPhaseException
    {
        statName = statName.toLowerCase();
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = encounter.getExplorer(name);
        encounteredExplorer.modifyStat(statName, statMod);
        encounter.sortRoster();
        logger.logDungeonMasterStatMod(
            encounteredExplorer.getName(),
            statName,
            statMod,
            encounteredExplorer.getStat(statName)
        );
    }

    /**
     * Protect action
     *
     * @param player Owner of current explorer
     * @param name   Name of explorer to protect
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    public void protectAction(@NotNull Player player, @NotNull String name) throws EncounterPhaseException
    {
        if (encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!encounter.isDodgePhase()) {
            throw EncounterPhaseException.createNotProtectPhase();
        }

        EncounteredExplorerInterface protectorCharacter = getCurrentExplorer(player);
        EncounteredExplorerInterface protectedCharacter = encounter.getExplorer(name);

        ProtectActionResultInterface actionResult = protectorCharacter.protect(
            protectedCharacter,
            encounter.getActiveHostiles()
        );

        logger.logAction(actionResult);
        endCurrentPlayerAction();
    }

    /**
     * Remove hostile with given name
     *
     * @param name Name of hostile to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void removeHostile(@NotNull String name) throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredHostileInterface hostile = encounter.getHostile(name);
        this.encounter.removeHostile(hostile);
        this.logger.logRemovedHostile(name);
    }

    /**
     * Remove explorer with given name
     *
     * @param name Name of explorer to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void removeExplorer(@NotNull String name) throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = this.encounter.getExplorer(name);
        if (encounter.isInitiativePhase()) {
            EncounteredExplorerInterface currentExplorer = encounter.getCurrentExplorer();
            if (currentExplorer.isName(name)) {
                currentExplorer.useAllActions();
                endCurrentPlayerAction();
            }
        }
        encounter.removeExplorer(encounteredExplorer);
        logger.logRemovedExplorer(name);
    }

    /**
     * Rejoin player
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void rejoinEncounter(@NotNull Player player) throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface encounteredExplorer = this.encounter.playerHasRejoined(player);
        this.logger.logRejoinEncounter(encounteredExplorer.getName());
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        this.encounter.setMaxPlayerCount(maxPlayerCount);
        this.logger.logSetMaxPlayers(maxPlayerCount);
    }

    /**
     * Skip current player turn
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void skipPlayerTurn() throws EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounteredExplorerInterface currentExplorer = this.encounter.getCurrentExplorer();
        if (this.encounter.isAttackPhase()) {
            this.logger.logActionAttackSkipped(currentExplorer.getName());
            currentExplorer.useAllActions();
            this.endCurrentPlayerAction();
        } else if (this.encounter.isDodgePhase()) {
            this.dodgeActionSkipped(currentExplorer.getOwner());
        }
    }

    /**
     * Start attack phase
     */
    public void startAttackPhase()
    {
        this.validateStartInitiativePhase();
        this.encounter.startAttackPhase();
        this.logger.logStartAttackPhase(this.encounter.getAllExplorers(), this.encounter.getAllHostiles());
        this.logger.pingPlayerTurn(this.encounter.getNextExplorer());
    }

    /**
     * Start dodge phase
     */
    public void startDodgePhase()
    {
        this.validateStartInitiativePhase();
        this.encounter.startDodgePhase();
        for (EncounteredHostileInterface encounteredHostile : this.encounter.getActiveHostiles()) {
            encounteredHostile.attack();
        }
        this.logger.logStartDodgePhase(this.encounter.getAllExplorers(), this.encounter.getActiveHostiles());
        this.logger.pingPlayerTurn(this.encounter.getNextExplorer());
    }

    /**
     * Start encounters
     *
     * @param channel     Encounter channel
     * @param mentionRole Mention role
     *
     * @throws EncounterPhaseException If encounter is over or already started
     * @throws MaxZeroPlayersException If no max player count has been set
     * @throws NoHostilesException     If the encounter has no hostiles
     */
    public void startEncounter(@NotNull MessageChannel channel, @NotNull Role mentionRole)
        throws EncounterPhaseException, MaxZeroPlayersException, NoHostilesException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (this.encounter.isStarted()) {
            throw EncounterPhaseException.createStartInProgressEncounter();
        } else if (this.encounter.getMaxPlayerCount() == 0) {
            throw new MaxZeroPlayersException();
        } else if (!this.encounter.hasActiveHostiles()) {
            throw new NoHostilesException();
        }
        this.loggerContext.setChannel(channel);
        this.encounter.startJoinPhase();
        this.logger.logStartEncounter(mentionRole, this.encounter.getMaxPlayerCount());
    }

    /**
     * View encounter summary
     */
    public void viewEncounterSummary()
    {
        this.logger.logSummary(this.encounter.getAllExplorers(), this.encounter.getAllHostiles());
    }

    /**
     * Request item use
     *
     * @param player Player
     */
    public void pingDmItemUsed(@NotNull Player player)
    {
        this.logger.pingDmItemUsed(player);
    }

    /**
     * Add kill to explorers
     *
     * @param encounteredHostile Encountered hostile
     */
    private void addKillToExplorers(@NotNull EncounteredHostileInterface encounteredHostile)
    {
        for (EncounteredExplorerInterface encounteredExplorer : this.encounter.getActiveExplorers()) {
            encounteredExplorer.addKill(encounteredHostile);
        }
    }

    /**
     * Dodge action skipped
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    private void dodgeActionSkipped(@NotNull Player player) throws EncounterPhaseException
    {
        if (!this.encounter.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }
        EncounteredExplorerInterface encounteredExplorer = this.getCurrentExplorer(player);
        DodgeActionResultInterface   result              = encounteredExplorer.failToDodge(this.encounter.getActiveHostiles());

        this.logger.logAction(result);
        this.endCurrentPlayerAction();
    }

    /**
     * End current player action
     */
    private void endCurrentPlayerAction()
    {
        EncounteredExplorerInterface currentExplorer = this.encounter.getCurrentExplorer();
        if (currentExplorer.isSlain()) {
            reviveIfFirstSlainExplorer(currentExplorer);
        }
        if (!encounter.hasActiveHostiles()) {
            startLootPhase();
        } else if (!encounter.hasActiveExplorers()) {
            encounter.startEndPhase();
            logger.logEndEncounterLose(encounter.getAllExplorers(), encounter.getAllHostiles());
        } else {
            if (currentExplorer.hasActions()) {
                this.logger.logActionsRemaining(
                    currentExplorer.getName(),
                    currentExplorer.getRemainingActions()
                );
            } else {
                try {
                    this.logger.pingPlayerTurn(this.encounter.getNextExplorer());
                } catch (EncounteredCreatureNotFoundException e) {
                    if (this.encounter.isAttackPhase()) {
                        this.encounter.startRpPhase();
                        this.logger.logEndAttackPhase(
                            this.encounter.getAllExplorers(),
                            this.encounter.getAllHostiles()
                        );
                    } else if (this.encounter.isDodgePhase()) {
                        this.encounter.startRpPhase();
                        this.logger.logEndDodgePhase(
                            this.encounter.getAllExplorers(),
                            this.encounter.getAllHostiles()
                        );
                    }
                }
            }
        }
    }

    /**
     * Get explorer
     *
     * @param player Owner of explorer to retrieve
     *
     * @return EncounteredExplorerInterface
     *
     * @throws NotYourTurnException If not players turn
     */
    private @NotNull EncounteredExplorerInterface getCurrentExplorer(@NotNull Player player) throws NotYourTurnException
    {
        EncounteredExplorerInterface currentExplorer = this.encounter.getCurrentExplorer();
        if (!currentExplorer.isOwner(player)) {
            throw new NotYourTurnException();
        }
        return currentExplorer;
    }

    /**
     * Revive slain explorer if they are the first explorer to be slain
     *
     * @param encounteredExplorer Encountered explorer
     */
    private void reviveIfFirstSlainExplorer(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        if (encounteredExplorer.isSlain() && this.encounter.hasPhoenixDown()) {
            this.encounter.usePhoenixDown();
            int hitpoints = encounteredExplorer.healPercent((float) 0.5);
            this.logger.logFirstDeathRevived(encounteredExplorer.getName(), hitpoints);
        }
    }

    /**
     * Start loot phase
     */
    private void startLootPhase()
    {
        encounter.startLootPhase();
        for (EncounteredExplorerInterface encounteredExplorer : encounter.getAllExplorers()) {
            encounteredExplorer.rollLoot();
        }
        logger.logEndEncounterWin(encounter.getAllExplorers(), encounter.getAllHostiles());
    }

    /**
     * Validate start initiative phase
     *
     * @throws DungeonException        If no players have joined
     * @throws EncounterPhaseException If encounter is over or encounter has not started
     */
    private void validateStartInitiativePhase() throws DungeonException, EncounterPhaseException
    {
        if (this.encounter.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!this.encounter.isStarted()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (!this.encounter.havePlayersJoined()) {
            throw DungeonException.createNoPlayersHaveJoined();
        }
    }
}
