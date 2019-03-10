package bot.Encounter;

import bot.Constant;
import bot.Encounter.EncounterData.*;
import bot.Encounter.Exception.*;
import bot.Hostile.HostileManager;
import bot.Item.Consumable.ConsumableItem;
import bot.Item.Consumable.Exception.MissingRecipientException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;

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

        if (result.isFail()) {
            this.logger.logActionAttackFail(playerCharacter.getName(), hostile.getName());
        } else if (result.isMiss()) {
            this.logger.logActionAttackMiss(hostile, playerCharacter.getName(), result.getHitRoll());
        } else if (result.isCrit()) {
            this.logger.logActionAttackCrit(
                playerCharacter.getName(),
                hostile,
                result.getHitRoll(),
                result.getDamageRoll()
            );
        } else {
            this.logger.logActionAttackHit(playerCharacter, hostile, result.getHitRoll(), result.getDamageRoll());
        }

        if (hostile.isSlain()) {
            this.addKillToPlayerCharacters(hostile);
        }
        this.endCurrentPlayerAction();
    }

    public void createEncounter(MessageChannel channel, Role dungeonMaster) {
        this.context = new Encounter();
        this.loggerContext.setChannel(channel);
        this.loggerContext.setDungeonMaster(dungeonMaster);
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

        this.logger.logActionDodge(result);

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
                    this.context.startLootPhase();
                    this.logger.logEndEncounter(
                        this.context.getAllPlayerCharacters(),
                        this.context.getAllHostiles(),
                        true
                    );
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
        this.logger.logAddedPlayerCharacter(encounterData);
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
        PCEncounterData playerCharacter = this.context.getPlayerCharacter(player);
        if (playerCharacter.hasLoot()) {
            throw LootException.createReroll(player.getAsMention());
        } else if (!playerCharacter.hasKills()) {
            throw LootException.createNoKills(player.getAsMention());
        }
        playerCharacter.rollLoot();
        this.logger.logActionLoot(playerCharacter);
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
        if (this.context.isOver()) {
            throw EncounterPhaseException.createEndPhase();
        } else if (!this.context.isDodgePhase()) {
            throw EncounterPhaseException.createNotProtectPhase();
        }

        PCEncounterData protectorCharacter = this.getPlayerCharacter(player);
        if (!protectorCharacter.isAbleToProtect()) {
            throw PlayerCharacterUnableToProtectException.createProtectAlreadyUsed();
        }
        PCEncounterData protectedCharacter = this.context.getPlayerCharacter(name);
        if (protectedCharacter.equals(protectorCharacter)) {
            throw ProtectedCharacterException.createProtectYourself();
        } else if (protectedCharacter.isSlain()) {
            throw ProtectedCharacterException.createIsSlain(protectedCharacter.getName());
        } else if (!protectedCharacter.hasActions()) {
            throw ProtectedCharacterException.createTurnHasPassed(protectedCharacter.getName());
        }

        ArrayList<HostileEncounterData> hostiles      = this.context.getActiveHostiles();
        int                             totalDamage   = 0;
        int                             totalDefended = 0;

        for (HostileEncounterData hostile : hostiles) {
            int damage = protectorCharacter.takeDamage(hostile, hostile.getAttackRoll());
            totalDamage += damage;
            totalDefended += damage == 0 ? hostile.getAttackRoll() : protectorCharacter.getEndurance();
        }

        this.logger.logActionProtect(protectorCharacter, protectedCharacter, totalDamage, totalDefended);
        protectorCharacter.useAction();
        protectorCharacter.useProtect();
        protectedCharacter.useAllActions();
        this.endCurrentPlayerAction();
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
        this.logger.logRemovedPlayerCharacter(name);
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

    /**
     * Use Item
     *
     * @param player  Player
     * @param item    ConsumableItem
     * @param context Context
     *
     * @deprecated
     */
    public void useItem(Player player, ConsumableItem item, String[] context) {
        if (!this.context.isPhase(item.getUsablePhase())) {
            throw EncounterPhaseException.createNotItemUsablePhase(item.getName(), item.getUsablePhase());
        }
        PCEncounterData        playerCharacter = this.getPlayerCharacter(player);
        EncounterDataInterface recipient       = null;
        String                 statName        = null;

        for (String parameter : context) {
            if (Constant.isStatName(parameter)) {
                if (statName == null) {
                    statName = parameter;
                } else {
                    throw ItemException.createMultipleStats(statName, parameter);
                }
            } else if (this.context.isInEncounter(parameter)) {
                if (recipient == null) {
                    recipient = this.context.getEncounterData(parameter);
                } else {
                    throw ItemException.createMultipleRecipients(recipient.getName(), parameter);
                }
            } else {
                throw ItemException.createInvalidParameter(parameter);
            }
        }
        if (recipient == null) {
            recipient = playerCharacter;
        }

        boolean usedOnSelf      = playerCharacter == recipient;
        int     hitpointsHealed = 0;
        int     damage          = 0;

        EncounterManager.validateItemUse(playerCharacter, item, recipient, statName);

        if (item.isHealing()) {
            EncounterDataInterface healedED = (!usedOnSelf && item.isUserHealed()) ? playerCharacter : recipient;
            hitpointsHealed = item.isPercentHealing() ? healedED.healPercent(item.getPercentHealed()) :
                              healedED.healPoints(item.getHitpointsHealed());
        }

        if (item.isDamaging()) {
            damage = recipient.takeDamage(playerCharacter, item.getDamage());
            if (recipient instanceof HostileEncounterData && recipient.isSlain()) {
                this.addKillToPlayerCharacters((HostileEncounterData) recipient);
            }
        }

        if (item.isTempStatBoost() && recipient instanceof PCEncounterData) {
            ((PCEncounterData) recipient).modifyStat(statName, item.getTempStatBoost());
        }

        this.logger.logUsedItem(
            playerCharacter,
            recipient,
            item,
            hitpointsHealed,
            damage,
            statName,
            item.isReviving()
        );

        if (item.isProtecting()) {
            ArrayList<HostileEncounterData> hostiles      = this.context.getActiveHostiles();
            int                             totalDamage   = 0;
            int                             totalDefended = 0;

            for (HostileEncounterData hostile : hostiles) {
                int damage2 = playerCharacter.takeDamage(hostile, hostile.getAttackRoll());
                totalDamage += damage2;
                totalDefended += damage2 == 0 ? hostile.getAttackRoll() : playerCharacter.getEndurance();
            }

            if (recipient instanceof PCEncounterData) {
                this.logger.logActionProtect(playerCharacter, (PCEncounterData) recipient, totalDamage, totalDefended);
                ((PCEncounterData) recipient).useAllActions();
            }
        }

        playerCharacter.useAction();
        this.endCurrentPlayerAction();
    }

    public void viewEncounterSummary() {
        this.logger.logEncounterSummary(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
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

        this.logger.logActionDodgeSkipped(
            playerCharacter,
            result.getTotalDamageDealt(),
            result.getTotalDamageResisted()
        );

        this.endCurrentPlayerAction();
    }

    private void endCurrentPlayerAction() {
        PCEncounterData currentPlayerCharacter = this.context.getCurrentPlayerCharacter();
        if (currentPlayerCharacter.isSlain()) {
            this.reviveIfFirstSlainPC(currentPlayerCharacter);
        }
        if (!this.context.hasActiveHostiles()) {
            this.context.startLootPhase();
            this.logger.logEndEncounter(
                this.context.getAllPlayerCharacters(),
                this.context.getAllHostiles(),
                true
            );
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

    private void reviveIfFirstSlainPC(PCEncounterData playerCharacter) {
        if (playerCharacter.isSlain() && this.context.hasPhoenixDown()) {
            this.context.usePhoenixDown();
            int hitpoints = playerCharacter.healPercent((float) 0.5);
            this.logger.logFirstDeathRevived(playerCharacter.getName(), hitpoints);
        }
    }

    private static void validateItemUse(
        PCEncounterData playerCharacter,
        ConsumableItem item,
        EncounterDataInterface recipient,
        String statName
    ) {
        if (recipient.isName(playerCharacter.getName()) && item.isRecipientRequired()) {
            if (item.isProtecting()) {
                throw ProtectedCharacterException.createProtectYourself();
            } else {
                throw MissingRecipientException.create(item.getName());
            }
        }
        if (item.isHealing()) {
            if (item.isReviving()) {
                if (!recipient.isSlain()) {
                    throw ItemRecipientException.createReviveLiving(
                        recipient.getName(),
                        recipient.getCurrentHP(),
                        recipient.getMaxHP()
                    );
                }
            } else if (recipient.isSlain()) {
                throw PlayerCharacterSlainException.createFailedToHeal(
                    recipient.getName(),
                    recipient.getSlayer().getName()
                );
            }
            if (!item.isUserHealed() && recipient.getMaxHP() == recipient.getCurrentHP()) {
                throw ItemRecipientException.createHealMaxHealth(recipient.getName(), recipient.getMaxHP());
            }
        }
        if (item.isDamaging() && recipient instanceof PCEncounterData) {
            throw ItemRecipientException.createDamagePlayer(recipient.getName(), item.getName());
        }
        if (item.isProtecting()) {
            if (!(recipient instanceof PCEncounterData)) {
                throw ProtectedCharacterException.createNotPlayerCharacter(recipient.getName());
            } else if (recipient.isSlain() && !item.isReviving()) {
                throw ProtectedCharacterException.createIsSlain(recipient.getName());
            } else if (!((PCEncounterData) recipient).hasActions()) {
                throw ProtectedCharacterException.createTurnHasPassed(recipient.getName());
            }
        }
        if (item.isTempStatBoost()) {
            if (statName == null) {
                throw ItemException.createMissingStatName(item.getName());
            } else if (recipient instanceof PCEncounterData) {
                if (!((PCEncounterData) recipient).isStatModifiable(statName, item.getTempStatBoost())) {
                    throw PCEncounterDataException.createStatOutOfBounds(recipient.getName(), statName);
                }
            } else {
                throw ItemRecipientException.createBoostHostile(recipient.getName());
            }
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
