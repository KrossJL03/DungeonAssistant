package bot.Encounter;

import bot.Encounter.EncounterData.*;
import bot.Encounter.Exception.*;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.EncounterLoggerContext;
import bot.Hostile.HostileManager;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

public class EncounterManager {

    private Encounter              context;
    private EncounterLogger        logger;
    private EncounterLoggerContext loggerContext;

    public EncounterManager(
        Encounter context,
        EncounterLogger logger,
        EncounterLoggerContext loggerContext
    ) {
        this.context = context;
        this.logger = logger;
        this.loggerContext = loggerContext;
    }

    public void addHostile(String speciesName, String nickname) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        HostileEncounterData hostileData = this.context.addHostile(HostileManager.getHostile(speciesName), nickname);
        this.logger.logAddedHostile(hostileData);
    }

    public void attackAction(Player player, String hostileName) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!this.context.isAttackPhase()) {
            throw EncounterPhaseException.createNotAttackPhase();
        }
        PCEncounterData      playerCharacter = this.getPlayerCharacter(player);
        HostileEncounterData hostile         = this.context.getHostile(hostileName);

        AttackActionResult result = playerCharacter.attack(hostile);
        this.logger.logAction(result);

        if (hostile.isSlain()) {
            this.addKillToPlayerCharacters(hostile);
        }
        this.endCurrentPlayerAction();
    }

    public void createEncounter(MessageChannel channel, Role dungeonMaster) {
        this.context = new Encounter();
        this.loggerContext.setChannel(channel);
        this.loggerContext.setDungeonMasterMention(dungeonMaster);
        this.logger.logCreateEncounter();
    }

    public void dodgeAction(Player player) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!this.context.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }

        PCEncounterData   playerCharacter = this.getPlayerCharacter(player);
        DodgeActionResult result          = playerCharacter.dodge(this.context.getActiveHostiles());

        this.logger.logAction(result);
        this.endCurrentPlayerAction();
    }

    public void dodgePassAction() {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        this.logger.logActionDodgePass(playerCharacter);
        playerCharacter.useAction();
        this.endCurrentPlayerAction();
    }

    public void dodgePassActionHelp(Player player) {
        this.logger.pingDmDodgePass(player);
    }

    public void endCurrentPlayersAction() {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        playerCharacter.useAction();
        this.endCurrentPlayerAction();
    }

    public void endCurrentPlayersTurn() {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        playerCharacter.useAllActions();
        this.endCurrentPlayerAction();
    }

    public void heal(String name, int hitpoints) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounterDataInterface recipient = this.context.getEncounterData(name);
        recipient.healPoints(hitpoints);
        this.logger.logDungeonMasterHeal(
            recipient.getName(),
            hitpoints,
            recipient.getCurrentHP(),
            recipient.getMaxHP()
        );
    }

    public void hurt(String name, int hitpoints) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        EncounterDataInterface recipient = this.context.getEncounterData(name);
        recipient.hurt(hitpoints);
        this.logger.logDungeonMasterHurt(
            recipient.getName(),
            hitpoints,
            recipient.getCurrentHP(),
            recipient.getMaxHP()
        );
        if (recipient.isSlain()) {
            this.logger.logDungeonMasterSlay(recipient.getName());
            if (recipient instanceof HostileEncounterData) {
                this.addKillToPlayerCharacters((HostileEncounterData) recipient);
                if (!this.context.hasActiveHostiles()) {
                    this.startLootPhase();
                }
            } else if (recipient instanceof PCEncounterData) {
                if (this.context.isInitiativePhase() && recipient == this.context.getCurrentPlayerCharacter()) {
                    this.endCurrentPlayerAction();
                } else {
                    this.reviveIfFirstSlainPC((PCEncounterData) recipient);
                }
            }
        }
    }

    public void joinEncounter(PlayerCharacter playerCharacter) {
        if (!this.context.isStarted()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData encounterData = new PCEncounterData(playerCharacter);
        this.context.addCharacter(encounterData);
        this.logger.logAddedExplorer(encounterData);
        if (this.context.isFullDungeon()) {
            this.logger.logDungeonIsFull();
        }
    }

    public void leaveEncounter(Player player) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData currentPlayerCharacter;
        try {
            currentPlayerCharacter = this.context.getCurrentPlayerCharacter();
        } catch (NotInInitiativeException e) {
            currentPlayerCharacter = null;
        }
        PCEncounterData playerCharacter = this.context.playerHasLeft(player);
        this.logger.logLeftEncounter(playerCharacter.getName());
        if (playerCharacter == currentPlayerCharacter) {
            this.endCurrentPlayerAction();
        } else if (!this.context.hasActivePCs()) {
            this.context.startEndPhase();
            this.logger.logEndEncounter(
                this.context.getAllPlayerCharacters(),
                this.context.getAllHostiles(),
                false
            );
        }
    }

    public void lootAction(Player player) {
        if (!this.context.isLootPhase()) {
            throw EncounterPhaseException.createNotLootPhase();
        }
        PCEncounterData  playerCharacter = this.context.getPlayerCharacter(player);
        LootActionResult actionResult    = playerCharacter.getLoot();
        this.logger.logAction(actionResult);
    }

    public void modifyStat(String name, String statName, int statMod) {
        statName = statName.toLowerCase();
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
        playerCharacter.modifyStat(statName, statMod);
        this.context.sortRoster();
        this.logger.logDungeonMasterStatMod(
            playerCharacter.getName(),
            statName,
            statMod,
            playerCharacter.getStat(statName)
        );
    }

    public void protectAction(Player player, String name) {
        if (context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!context.isDodgePhase()) {
            throw EncounterPhaseException.createNotProtectPhase();
        }

        PCEncounterData protectorCharacter = getPlayerCharacter(player);
        PCEncounterData protectedCharacter = context.getPlayerCharacter(name);

        ProtectActionResult actionResult = protectorCharacter.protect(
            protectedCharacter,
            context.getActiveHostiles()
        );

        logger.logAction(actionResult);
        endCurrentPlayerAction();
    }

    public void removeHostile(String name) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        HostileEncounterData hostile = this.context.getHostile(name);
        this.context.removeHostile(hostile);
        this.logger.logRemovedHostile(name);
    }

    public void removePlayerCharacter(String name) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
        this.context.removePlayerCharacter(playerCharacter);
        this.logger.logRemovedExplorer(name);
    }

    public void rejoinEncounter(Player player) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData playerCharacter = this.context.playerHasRejoined(player);
        this.logger.logRejoinEncounter(playerCharacter.getName());
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        this.context.setMaxPlayerCount(maxPlayerCount);
        this.logger.logSetMaxPlayers(maxPlayerCount);
    }

    public void skipPlayerTurn() {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        }
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        if (this.context.isAttackPhase()) {
            this.logger.logActionAttackSkipped(playerCharacter.getName());
            playerCharacter.useAllActions();
            this.endCurrentPlayerAction();
        } else if (this.context.isDodgePhase()) {
            this.dodgeActionSkipped(playerCharacter.getOwner());
        }
    }

    public void startAttackPhase() {
        this.validateStartEncounterPhase();
        this.context.startAttackPhase();
        this.logger.logStartAttackPhase(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
        this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
    }

    public void startDodgePhase() {
        this.validateStartEncounterPhase();
        this.context.startDodgePhase();
        for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
            hostile.attack();
        }
        this.logger.logStartDodgePhase(this.context.getAllPlayerCharacters(), this.context.getActiveHostiles());
        this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
    }

    public void startEncounter(MessageChannel channel, Role mentionRole) throws NoHostilesException {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (this.context.isStarted()) {
            throw EncounterPhaseException.createStartInProgressEncounter();
        } else if (this.context.getMaxPlayerCount() == 0) {
            throw new MaxZeroPlayersException();
        } else if (!this.context.hasActiveHostiles()) {
            throw new NoHostilesException();
        }
        this.loggerContext.setChannel(channel);
        this.context.startJoinPhase();
        this.logger.logStartEncounter(mentionRole, this.context.getMaxPlayerCount());
    }

    public void viewEncounterSummary() {
        this.logger.logSummary(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
    }

    // todo remove once inventory is implemented
    public void pingDmItemUsed(Player player) {
        this.logger.pingDmItemUsed(player);
    }

    private void addKillToPlayerCharacters(HostileEncounterData hostile) {
        for (PCEncounterData playerCharcter : this.context.getActivePlayerCharacters()) {
            playerCharcter.addKill(hostile);
        }
    }

    private void dodgeActionSkipped(Player player) {
        if (!this.context.isDodgePhase()) {
            throw EncounterPhaseException.createNotDodgePhase();
        }
        PCEncounterData   playerCharacter = this.getPlayerCharacter(player);
        DodgeActionResult result          = playerCharacter.failToDodge(this.context.getActiveHostiles());

        this.logger.logAction(result);
        this.endCurrentPlayerAction();
    }

    private void endCurrentPlayerAction() {
        PCEncounterData currentPlayerCharacter = this.context.getCurrentPlayerCharacter();
        if (currentPlayerCharacter.isSlain()) {
            this.reviveIfFirstSlainPC(currentPlayerCharacter);
        }
        if (!this.context.hasActiveHostiles()) {
            this.startLootPhase();
        } else if (!this.context.hasActivePCs()) {
            this.context.startEndPhase();
            this.logger.logEndEncounter(
                this.context.getAllPlayerCharacters(),
                this.context.getAllHostiles(),
                false
            );
        } else {
            if (currentPlayerCharacter.hasActions()) {
                this.logger.logActionsRemaining(
                    currentPlayerCharacter.getName(),
                    currentPlayerCharacter.getRemainingActions()
                );
            } else {
                try {
                    this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
                } catch (EncounterDataNotFoundException e) {
                    if (this.context.isAttackPhase()) {
                        this.context.startRpPhase();
                        this.logger.logEndAttackPhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    } else if (this.context.isDodgePhase()) {
                        this.context.startRpPhase();
                        this.logger.logEndDodgePhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    }
                }
            }
        }
    }

    private PCEncounterData getPlayerCharacter(Player player) {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        if (!playerCharacter.isOwner(player)) {
            throw new NotYourTurnException();
        }
        return playerCharacter;
    }

    private void startLootPhase() {
        this.context.startLootPhase();
        for (PCEncounterData playerCharacter : this.context.getAlivePlayerCharacters()) {
            playerCharacter.rollLoot();
        }
        this.logger.logEndEncounter(
            this.context.getAllPlayerCharacters(),
            this.context.getAllHostiles(),
            true
        );
    }

    private void reviveIfFirstSlainPC(PCEncounterData playerCharacter) {
        if (playerCharacter.isSlain() && this.context.hasPhoenixDown()) {
            this.context.usePhoenixDown();
            int hitpoints = playerCharacter.healPercent((float) 0.5);
            this.logger.logFirstDeathRevived(playerCharacter.getName(), hitpoints);
        }
    }

    private void validateStartEncounterPhase() {
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!this.context.isStarted()) {
            throw EncounterPhaseException.createNotStarted();
        } else if (!this.context.havePlayersJoined()) {
            throw DungeonException.createNoPlayersHaveJoined();
        }
    }
}
