package bot.Encounter;

import bot.Encounter.Exception.EncounterDataNotFoundException;
import bot.Encounter.Exception.WrongPhaseException;
import bot.Encounter.EncounterData.*;
import bot.Exception.*;
import bot.Hostile.Exception.NoHostileFoundException;
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
        try {
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
        } catch (NoHostileFoundException | HostileNicknameInUseException e) {
            this.logger.logException(e);
        }
    }

    public void attackAction(Player player, String hostileName) {
        try {
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
        } catch (WrongPhaseException | NotYourTurnException | HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    public void createEncounter(MessageChannel channel, Role dungeonMaster) {
        this.context = new EncounterContext();
        this.loggerContext.setChannel(channel);
        this.loggerContext.setDungeonMaster(dungeonMaster);
        this.logger.logCreateEncounter();
    }

    public void dodgeAction(Player player) {
        try {
            if (!this.context.isDodgePhase()) {
                throw WrongPhaseException.createForDodge();
            }
            PCEncounterData    playerCharacter = this.getPlayerCharacter(player);
            ArrayList<Integer> dodgeRolls      = new ArrayList<>();
            int                totalDamage     = 0;
            int                totalDefended   = 0;
            int                endurance       = playerCharacter.getEndurance();
            for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
                int dodgeRoll = playerCharacter.rollDodge();
                if (dodgeRoll < 10) {
                    int damage = playerCharacter.takeDamage(hostile, hostile.getAttackRoll());
                    totalDamage += damage;
                    totalDefended += damage == 1 ? hostile.getAttackRoll() : endurance;
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
        } catch (WrongPhaseException | NotYourTurnException e) {
            this.logger.logException(e);
        }
    }

    public void healHostile(String name, int hitpoints) {
        try {
            HostileEncounterData hostile = this.context.getHostile(name);
            hostile.heal(hitpoints);
            this.logger.logDungeonMasterHeal(
                hostile.getName(),
                hitpoints,
                hostile.getCurrentHP(),
                hostile.getMaxHP()
            );
        } catch (HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    public void healPlayer(String name, int hitpoints) {
        PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
        playerCharacter.heal(hitpoints);
        this.logger.logDungeonMasterHeal(
            playerCharacter.getName(),
            hitpoints,
            playerCharacter.getCurrentHP(),
            playerCharacter.getMaxHP()
        );
    }

    public void hurtHostile(String name, int hitpoints) {
        try {
            HostileEncounterData hostile = this.context.getHostile(name);
            hostile.hurt(hitpoints);
            this.logger.logDungeonMasterHurt(
                hostile.getName(),
                hitpoints,
                hostile.getCurrentHP(),
                hostile.getMaxHP()
            );
            if (hostile.isSlain()) {
                this.addKillToPlayerCharacters(hostile);
                this.logger.logDungeonMasterSlay(hostile.getName());
            }
        } catch (HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    public void hurtPlayer(String name, int hitpoints) {
        try {
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
            playerCharacter.hurt(hitpoints);
            this.logger.logDungeonMasterHurt(
                playerCharacter.getName(),
                hitpoints,
                playerCharacter.getCurrentHP(),
                playerCharacter.getMaxHP()
            );
            if (playerCharacter.isSlain()) {
                this.logger.logDungeonMasterSlay(playerCharacter.getName());
            }
        } catch (CharacterSlainException e) {
            this.logger.logException(e);
        }
    }

    public void joinEncounter(PlayerCharacter playerCharacter) {
        try {
            if (!this.context.isStarted()) {
                throw new EncounterNotStartedException();
            }
            PCEncounterData encounterData = new PCEncounterData(playerCharacter);
            this.context.addCharacter(encounterData);
            this.logger.logAddedPlayerCharacter(encounterData);
            if (this.context.isFullDungeon()) {
                this.logger.logDungeonIsFull();
            }
        } catch (EncounterNotStartedException | FullDungeonException | MultiplePlayerCharactersException e) {
            this.logger.logException(e);
        }
    }

    public void leaveEncounter(Player player) {
        try {
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(player);
            playerCharacter.useAllActions();
            this.context.playerHasLeft(playerCharacter);
            this.logger.logLeftEncounter(playerCharacter.getName());
            this.endPlayerAction();
        } catch (PlayerCharacterAlreadyLeftException e) {
            this.logger.logException(e);
        }
    }

    public void lootAction(Player player) {
        try {
            if (!this.context.isLootPhase()) {
                throw WrongPhaseException.createForLoot();
            }
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(player);
            if (playerCharacter.hasLoot()) {
                throw new LootRerollException();
            }
            playerCharacter.rollLoot();
            playerCharacter.useAllActions();
            this.logger.logActionLoot(playerCharacter);
        } catch (LootRerollException e) {
            this.logger.logException(e);
        }
    }

    public void protectAction(Player player, String name) {
        try {
            if (!this.context.isDodgePhase()) {
                throw WrongPhaseException.createForProtect();
            }

            PCEncounterData protectorCharacter = this.getPlayerCharacter(player);
            if (!protectorCharacter.isAbleToProtect()) {
                throw new CharacterUnableToProtectException();
            }
            PCEncounterData protectedCharacter = this.context.getPlayerCharacter(name);
            if (protectedCharacter.equals(protectorCharacter)) {
                throw new ProtectYourselfException();
            } else if (protectedCharacter.isSlain()) {
                throw new ProtectedCharacterIsSlain(protectedCharacter.getName());
            } else if (this.context.hasPlayerCharacterGone(protectedCharacter)) {
                throw new ProtectedCharactersTurnHasPassedException(protectedCharacter.getName());
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
        } catch (
            WrongPhaseException |
                NotYourTurnException |
                CharacterUnableToProtectException |
                ProtectYourselfException |
                ProtectedCharacterIsSlain |
                ProtectedCharactersTurnHasPassedException e
            ) {
            this.logger.logException(e);
        }
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

    public void returnToEncounter(Player player) {
        try {
            this.context.playerHasReturned(player);
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(player);
            this.logger.logReturnToEncounter(playerCharacter.getName());
        } catch (FullDungeonException e) {
            this.logger.logException(e);
        }
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.context.setMaxPlayerCount(maxPlayerCount);
        this.logger.logSetMaxPlayers(maxPlayerCount);
    }

    public void skipPlayerTurn() {
        try {
            PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
            if (this.context.isAttackPhase()) {
                this.logger.logActionAttackSkipped(playerCharacter.getName());
                playerCharacter.useAllActions();
                this.endPlayerAction();
            } else if (this.context.isDodgePhase()) {
                this.dodgeActionSkipped(playerCharacter.getOwner());
            }
        } catch (NotInInitiativeException e) {
            this.logger.logException(e);
        }
    }

    public void startAttackPhase() {
        try {
            if (!this.context.isStarted()) {
                throw new EncounterNotStartedException();
            } else if (this.context.isOver()) {
                throw new EncounterIsOverException();
            } else {
                this.context.startAttackPhase();
                this.logger.logStartAttackPhase(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
                this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
            }
        } catch (EncounterNotStartedException | EncounterIsOverException | StartCurrentPhaseException e) {
            this.logger.logException(e);
        }
    }

    public void startDodgePhase() {
        try {
            if (!this.context.isStarted()) {
                throw new EncounterNotStartedException();
            } else if (this.context.isOver()) {
                throw new EncounterIsOverException();
            } else {
                this.context.startDodgePhase();
                for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
                    hostile.attack();
                }
                this.logger.logStartDodgePhase(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
                this.logger.pingPlayerTurn(this.context.getNextPlayerCharacter());
            }
        } catch (EncounterNotStartedException | EncounterIsOverException | StartCurrentPhaseException e) {
            this.logger.logException(e);
        }
    }

    public void startEncounter(MessageChannel channel, Role mentionRole) throws NoHostilesException {
        try {
            if (this.context.isStarted()) {
                throw new EncounterInProgessException();
            } else if (this.context.getMaxPlayerCount() == 0) {
                throw new MaxZeroPlayersException();
            } else if (this.context.getAllHostiles().size() == 0) {
                throw new NoHostilesException();
            }
            this.loggerContext.setChannel(channel);
            this.context.startEncounter();
            this.logger.logStartEncounter(mentionRole, this.context.getMaxPlayerCount());
        } catch (EncounterInProgessException | MaxZeroPlayersException | DungeonMasterNotFoundException e) {
            this.logger.logException(e);
        }
    }

    public void useItem(Player player, ConsumableItem item, String recipientName) {
        if (!this.context.isPhase(item.getUsablePhase())) {
            throw WrongPhaseException.createForItem(item.getName(), item.getUsablePhase());
        }
        PCEncounterData        playerCharacter = this.getPlayerCharacter(player);
        EncounterDataInterface recipient;
        boolean                usedOnSelf      = false;
        int                    hitpointsHealed = 0;
        int                    damage          = 0;
        boolean                isRevived       = false;

        if (recipientName == null) {
            if (item.isRecipientRequired()) {
                throw MissingRecipientException.create(item.getName());
            }
            recipient = playerCharacter;
            usedOnSelf = true;
        } else {
            recipient = this.context.getEncounterData(recipientName);
            if (recipient == null) {
                throw EncounterDataNotFoundException.createForRecipient(recipientName);
            }
        }

        if (item.isHealing()) {
            if (!usedOnSelf && item.isUserHealed()) {
                hitpointsHealed = playerCharacter.heal(
                    item.isPercentHealing() ? item.getPercentHealed() : item.getHitpointsHealed()
                );
            } else {
                if (recipient.isSlain()) {
                    if (!item.isReviving()) {
                        throw CharacterSlainException.createFailedToHeal(
                            recipient.getName(),
                            recipient.getSlayer().getName()
                        );
                    }
                    isRevived = true;
                }
                hitpointsHealed = recipient.heal(
                    item.isPercentHealing() ? item.getPercentHealed() : item.getHitpointsHealed()
                );
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
//
//        if (item.isProtecting()) {
//            // todo is protecting item and is NOT the dodge turn
//            if (!usedOnSelf) {
//                // todo results in protect action
//            } else {
//                // todo results in successful dodge action
//
//            }
//        }

        this.logger.logUsedItem(
            playerCharacter, recipient, item,
            hitpointsHealed,
            damage,
            isRevived
        );

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
        try {
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
        } catch (WrongPhaseException | NotYourTurnException e) {
            this.logger.logException(e);
        }
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
}
