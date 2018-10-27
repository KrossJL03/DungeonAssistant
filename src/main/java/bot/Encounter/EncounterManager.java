package bot.Encounter;

import bot.Encounter.EncounterData.EncounterDataInterface;
import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.*;
import bot.Hostile.Hostile;
import bot.Hostile.HostileManager;
import bot.Item.Consumable.ConsumableItem;
import bot.Item.Consumable.Exception.MissingRecipientException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;

public class EncounterManager {

    private EncounterContext       context;
    private EncounterLogger        logger;
    private EncounterLoggerContext loggerContext;

    public EncounterManager(
        EncounterContext context,
        EncounterLogger logger,
        EncounterLoggerContext loggerContext
    ) {
        this.context = context;
        this.logger = logger;
        this.loggerContext = loggerContext;
    }

    public void addHostile(String speciesName, String nickname) {
        Hostile              hostileSpecies  = HostileManager.getHostile(speciesName);
        String               capitalNickname = nickname.substring(0, 1).toUpperCase() + nickname.substring(1);
        HostileEncounterData newHostile      = new HostileEncounterData(hostileSpecies, capitalNickname);
        if (speciesName.toLowerCase().equals(nickname.toLowerCase())) {
            int speciesCount = 0;
            for (HostileEncounterData hostile : this.context.getAllHostiles()) {
                if (hostile.getSpecies().equals(newHostile.getSpecies())) {
                    if (hostile.getSpecies().equals(hostile.getName())) {
                        hostile.setName(hostile.getName() + "A");
                    }
                    speciesCount++;
                }
            }
            if (speciesCount > 0) {
                char letter = (char) (65 + speciesCount);
                newHostile = new HostileEncounterData(hostileSpecies, newHostile.getSpecies() + letter);
            }
        } else {
            for (HostileEncounterData hostile : this.context.getAllHostiles()) {
                if (hostile.getName().toLowerCase().equals(newHostile.getName().toLowerCase())) {
                    throw new HostileNicknameInUseException(nickname);
                }
            }
        }
        this.context.addHostile(newHostile);
        this.logger.logAddedHostile(newHostile);
    }

    public void attackAction(Player player, String hostileName) {
        if (!this.context.isAttackPhase()) {
            throw WrongPhaseException.createForAttack();
        }
        PCEncounterData      playerCharacter = this.getPlayerCharacter(player);
        HostileEncounterData hostile         = this.context.getHostile(hostileName);
        int                  hitRoll         = playerCharacter.rollToHit();

        if (hitRoll < 2) {
            this.logger.logActionAttackFail(playerCharacter.getName(), hostile.getName());
        } else if (hitRoll < 6) {
            this.logger.logActionAttackMiss(hostile, playerCharacter.getName(), hitRoll);
        } else if (playerCharacter.isCrit(hitRoll)) {
            int damage = playerCharacter.getCritDamage();
            hostile.takeDamage(playerCharacter, damage);
            this.logger.logActionAttackCrit(playerCharacter.getName(), hostile, hitRoll, damage);
        } else {
            int damage = playerCharacter.rollDamage();
            hostile.takeDamage(playerCharacter, damage);
            this.logger.logActionAttackHit(playerCharacter, hostile, hitRoll, damage);
        }

        playerCharacter.useAction();
        if (hostile.isSlain()) {
            this.addKillToPlayerCharacters(hostile);
        }
        this.endPlayerAction();
    }

    public void createEncounter(MessageChannel channel, Role dungeonMaster) {
        this.context = new EncounterContext();
        this.loggerContext.setChannel(channel);
        this.loggerContext.setDungeonMaster(dungeonMaster);
        this.logger.logCreateEncounter();
    }

    public void dodgeAction(Player player) {
        if (!this.context.isDodgePhase()) {
            throw WrongPhaseException.createForDodge();
        }
        PCEncounterData    playerCharacter = this.getPlayerCharacter(player);
        ArrayList<Integer> dodgeRolls      = new ArrayList<>();
        int                totalDamage     = 0;
        int                totalDefended   = 0;
        for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
            int dodgeRoll = playerCharacter.rollDodge();
            if (dodgeRoll < 10) {
                int damage = playerCharacter.takeDamage(hostile, hostile.getAttackRoll());
                totalDamage += damage;
                totalDefended += hostile.getAttackRoll() - damage;
            }
            dodgeRolls.add(dodgeRoll);
        }
        this.logger.logActionDodge(
            playerCharacter,
            this.context.getActiveHostiles(),
            dodgeRolls,
            totalDamage,
            totalDefended
        );
        playerCharacter.useAction();
        this.endPlayerAction();
    }

    public void heal(String name, int hitpoints) {
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
        EncounterDataInterface recipient = this.context.getEncounterData(name);
        recipient.hurt(hitpoints);
        this.logger.logDungeonMasterHurt(
            recipient.getName(),
            hitpoints,
            recipient.getCurrentHP(),
            recipient.getMaxHP()
        );
        if (recipient.isSlain()) {
            if (recipient instanceof HostileEncounterData) {
                this.addKillToPlayerCharacters((HostileEncounterData) recipient);
            }
            this.logger.logDungeonMasterSlay(recipient.getName());
        }
    }

    public void joinEncounter(PlayerCharacter playerCharacter) {
        if (!this.context.isStarted()) {
            throw EncounterStatusException.createNotStarted();
        }
        PCEncounterData encounterData = new PCEncounterData(playerCharacter);
        this.context.addCharacter(encounterData);
        this.logger.logAddedPlayerCharacter(encounterData);
        if (this.context.isFullDungeon()) {
            this.logger.logDungeonIsFull();
        }
    }

    public void leaveEncounter(Player player) {
        PCEncounterData currentPlayerCharacter = this.context.getCurrentPlayerCharacter();
        PCEncounterData playerCharacter = this.context.playerHasLeft(player);
        this.logger.logLeftEncounter(playerCharacter.getName());
        if (playerCharacter == currentPlayerCharacter) {
            this.logger.pingPlayerTurn(this.context.getCurrentPlayerCharacter());
        }
    }

    public void lootAction(Player player) {
        if (!this.context.isLootPhase()) {
            throw WrongPhaseException.createForLoot();
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

    public void protectAction(Player player, String name) {
        if (!this.context.isDodgePhase()) {
            throw WrongPhaseException.createForProtect();
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
        this.endPlayerAction();
    }

    public void removeHostile(String name) {
        HostileEncounterData hostile = this.context.getHostile(name);
        this.context.removeHostile(hostile);
        this.logger.logRemovedHostile(name);
    }

    public void removePlayerCharacter(String name) {
        PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
        this.context.removePlayerCharacter(playerCharacter);
        this.logger.logRemovedPlayerCharacter(name);
    }

    public void rejoinEncounter(Player player) {
        PCEncounterData playerCharacter = this.context.playerHasRejoined(player);
        this.logger.logRejoinEncounter(playerCharacter.getName());
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.context.setMaxPlayerCount(maxPlayerCount);
        this.logger.logSetMaxPlayers(maxPlayerCount);
    }

    public void skipPlayerTurn() {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        if (this.context.isAttackPhase()) {
            this.logger.logActionAttackSkipped(playerCharacter.getName());
            playerCharacter.useAllActions();
            this.endPlayerAction();
        } else if (this.context.isDodgePhase()) {
            this.dodgeActionSkipped(playerCharacter.getOwner());
        }
    }

    public void startAttackPhase() {
        if (!this.context.isStarted()) {
            throw EncounterStatusException.createNotStarted();
        } else if (this.context.isOver()) {
            throw EncounterStatusException.createIsOver();
        }
        this.context.startAttackPhase();
        this.logger.logStartAttackPhase(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
        this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
    }

    public void startDodgePhase() {
        if (!this.context.isStarted()) {
            throw EncounterStatusException.createNotStarted();
        } else if (this.context.isOver()) {
            throw EncounterStatusException.createIsOver();
        } else {
            this.context.startDodgePhase();
            for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
                hostile.attack();
            }
            this.logger.logStartDodgePhase(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
            this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
        }
    }

    public void startEncounter(MessageChannel channel, Role mentionRole) throws NoHostilesException {
        if (this.context.isStarted()) {
            throw EncounterStatusException.createInProgress();
        } else if (this.context.getMaxPlayerCount() == 0) {
            throw new MaxZeroPlayersException();
        } else if (this.context.getAllHostiles().size() == 0) {
            throw new NoHostilesException();
        }
        this.loggerContext.setChannel(channel);
        this.context.startEncounter();
        this.logger.logStartEncounter(mentionRole, this.context.getMaxPlayerCount());
    }

    public void useItem(Player player, ConsumableItem item, String recipientName) {
        if (!this.context.isPhase(item.getUsablePhase())) {
            throw WrongPhaseException.createForItem(item.getName(), item.getUsablePhase());
        }
        PCEncounterData        playerCharacter = this.getPlayerCharacter(player);
        EncounterDataInterface recipient       = recipientName == null ? playerCharacter : this.context.getEncounterData(recipientName);
        boolean                usedOnSelf      = playerCharacter == recipient;
        int                    hitpointsHealed = 0;
        int                    damage          = 0;

        EncounterManager.validateItemUse(playerCharacter, item, recipient);

        if (item.isHealing()) {
            if (!usedOnSelf && item.isUserHealed()) {
                if (item.isPercentHealing()) {
                    hitpointsHealed = playerCharacter.healPercent(item.getPercentHealed());
                } else {
                    hitpointsHealed = playerCharacter.healPoints(item.getHitpointsHealed());
                }
            } else {
                if (item.isPercentHealing()) {
                    hitpointsHealed = recipient.healPercent(item.getPercentHealed());
                } else {
                    hitpointsHealed = recipient.healPoints(item.getHitpointsHealed());
                }
            }
        }

        if (item.isDamaging()) {
            damage = recipient.takeDamage(playerCharacter, item.getDamage());
            if (recipient instanceof HostileEncounterData && recipient.isSlain()) {
                this.addKillToPlayerCharacters((HostileEncounterData) recipient);
            }
        }

//        if (item.isTempStatBoost()) {
//            // todo
//        }

        this.logger.logUsedItem(
            playerCharacter,
            recipient,
            item,
            hitpointsHealed,
            damage,
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
        this.endPlayerAction();
    }

    public void viewEncounterSummary() {
        this.logger.logEncounterSummary(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
    }

    private void addKillToPlayerCharacters(HostileEncounterData hostile) {
        for (PCEncounterData playerCharcter : this.context.getActivePlayerCharacters()) {
            playerCharcter.addKill(hostile);
        }
    }

    private void dodgeActionSkipped(Player player) {
        if (!this.context.isDodgePhase()) {
            throw WrongPhaseException.createForDodge();
        }
        PCEncounterData playerCharacter = this.getPlayerCharacter(player);
        int             totalDamage     = 0;
        int             totalDefended   = 0;
        int             endurance       = playerCharacter.getEndurance();
        for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
            int damage = playerCharacter.takeDamage(hostile, hostile.getAttackRoll());
            totalDamage += damage;
            totalDefended += damage == 0 ? hostile.getAttackRoll() : endurance;
        }
        this.logger.logActionDodgeSkipped(playerCharacter, totalDamage, totalDefended);
        playerCharacter.useAllActions();
        this.endPlayerAction();
    }

    private void endPlayerAction() {
        if (this.context.getActiveHostiles().isEmpty()) {
            this.context.startLootPhase();
            this.logger.logEndEncounter(this.context.getAllPlayerCharacters(), this.context.getAllHostiles(), true);
        } else if (this.context.getActivePlayerCharacters().isEmpty()) {
            this.logger.logEndEncounter(
                this.context.getAllPlayerCharacters(),
                this.context.getAllHostiles(),
                false
            );
        } else {
            PCEncounterData currentPlayerCharacter = this.context.getCurrentPlayerCharacter();
            if (currentPlayerCharacter.hasActions()) {
                this.logger.logActionsRemaining(
                    currentPlayerCharacter.getName(),
                    currentPlayerCharacter.getRemainingActions()
                );
            } else {
                PCEncounterData nextPlayerCharacter = this.context.getNextPlayerCharacter();
                if (nextPlayerCharacter == null) {
                    if (this.context.isAttackPhase()) {
                        this.context.endCurrentPhase();
                        this.logger.logEndAttackPhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    } else if (this.context.isDodgePhase()) {
                        this.context.endCurrentPhase();
                        this.logger.logEndDodgePhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    }
                } else {
                    this.logger.pingPlayerTurn(nextPlayerCharacter);
                }
            }
        }
    }

    private PCEncounterData getPlayerCharacter(Player player) {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        if (!playerCharacter.isOwner(player.getUserId())) {
            throw new NotYourTurnException();
        }
        return playerCharacter;
    }

    private static void validateItemUse(
        PCEncounterData playerCharacter,
        ConsumableItem item,
        EncounterDataInterface recipient
    ) {
        if (recipient == playerCharacter && item.isRecipientRequired()) {
            throw MissingRecipientException.create(item.getName());
        }
        if (item.isHealing()) {
            if (item.isReviving() && !recipient.isSlain()) {
                throw ItemRecipientException.createReviveLiving(
                    recipient.getName(),
                    recipient.getCurrentHP(),
                    recipient.getMaxHP()
                );
            }
            if (!item.isReviving() && recipient.isSlain()) {
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
            } else if (recipient.equals(playerCharacter)) {
                throw ProtectedCharacterException.createProtectYourself();
            } else if (recipient.isSlain() && !item.isReviving()) {
                throw ProtectedCharacterException.createIsSlain(recipient.getName());
            } else if (!((PCEncounterData) recipient).hasActions()) {
                throw ProtectedCharacterException.createTurnHasPassed(recipient.getName());
            }
        }
    }
}
