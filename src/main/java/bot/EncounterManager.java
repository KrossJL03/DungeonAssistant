package bot;

import bot.Entity.Hostile;
import bot.Entity.HostileEncounterData;
import bot.Entity.PCEncounterData;
import bot.Entity.PlayerCharacter;
import bot.Exception.*;
import bot.Repository.HostileRepository;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class EncounterManager {

    private EncounterContext       context;
    private EncounterLogger        logger;
    private EncounterLoggerContext loggerContext;
    private HostileRepository      hostileRepository;

    public EncounterManager(
        EncounterContext context,
        EncounterLogger logger,
        EncounterLoggerContext loggerContext,
        HostileRepository hostileRepository
    ) {
        this.context = context;
        this.hostileRepository = hostileRepository;
        this.logger = logger;
        this.loggerContext = loggerContext;
    }

    void addHostile(String speciesName, String nickname) {
        try {
            Hostile              hostileSpecies = this.hostileRepository.getHostile(speciesName);
            HostileEncounterData newHostile     = new HostileEncounterData(hostileSpecies, nickname);
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

    void attackAction(User author, String hostileName) {
        try {
            if (!this.context.isAttackPhase()) {
                throw new WrongPhaseException("attack", "attack");
            }
            PCEncounterData      playerCharacter = this.getPlayerCharacter(author);
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
            this.endPlayerAction();
        } catch (WrongPhaseException | NotYourTurnException | NoHostileInEncounterException | HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    void createEncounter(MessageChannel channel, Role dungeonMaster) {
        this.context = new EncounterContext();
        this.loggerContext.setChannel(channel);
        this.loggerContext.setDungeonMaster(dungeonMaster);
        this.logger.logCreateEncounter();
    }

    void dodgeAction(User author) {
        try {
            if (!this.context.isDodgePhase()) {
                throw new WrongPhaseException("dodge", "dodge");
            }
            PCEncounterData    playerCharacter = this.getPlayerCharacter(author);
            ArrayList<Integer> dodgeRolls      = new ArrayList<>();
            int                totalDamage     = 0;
            int                totalDefended   = 0;
            int                endurance       = playerCharacter.getEndurance();
            for (HostileEncounterData hostile : this.context.getActiveHostiles()) {
                int dodgeRoll = playerCharacter.rollDodge();
                if (dodgeRoll < 10) {
                    int damage = playerCharacter.takeDamage(hostile, hostile.getAttackRoll());
                    totalDamage += damage;
                    totalDefended += damage == 0 ? hostile.getAttackRoll() : endurance;
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

    void healHostile(String name, int hitpoints) {
        try {
            HostileEncounterData hostile = this.context.getHostile(name);
            hostile.heal(hitpoints);
            this.logger.logDungeonMasterHeal(
                hostile.getName(),
                hitpoints,
                hostile.getCurrentHP(),
                hostile.getMaxHP()
            );
        } catch (NoHostileInEncounterException | HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    void healPlayer(String name, int hitpoints) {
        try {
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(name);
            playerCharacter.heal(hitpoints);
            this.logger.logDungeonMasterHeal(
                playerCharacter.getName(),
                hitpoints,
                playerCharacter.getCurrentHP(),
                playerCharacter.getMaxHP()
            );
        } catch (NoCharacterInEncounterException e) {
            this.logger.logException(e);
        }
    }

    void hurtHostile(String name, int hitpoints) {
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
                this.logger.logDungeonMasterSlay(hostile.getName());
            }
        } catch (NoHostileInEncounterException | HostileSlainException e) {
            this.logger.logException(e);
        }
    }

    void hurtPlayer(String name, int hitpoints) {
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
        } catch (NoCharacterInEncounterException | CharacterSlainException e) {
            this.logger.logException(e);
        }
    }

    void joinEncounter(PlayerCharacter playerCharacter) {
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

    void leaveEncounter(User player) {
        try {
            PCEncounterData playerCharacter = this.context.getPlayerCharacter(player);
            playerCharacter.leave();
            this.logger.logLeftEncounter(playerCharacter);
        } catch (NoCharacterInEncounterException | PlayerCharacterAlreadyLeftException e) {
            this.logger.logException(e);
        }
    }

    void protectAction(User player, String name) {
        try {
            if (!this.context.isDodgePhase()) {
                throw new WrongPhaseException("dodge", "protect");
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
                NoCharacterInEncounterException |
                CharacterUnableToProtectException |
                ProtectYourselfException |
                ProtectedCharacterIsSlain |
                ProtectedCharactersTurnHasPassedException e
            ) {
            this.logger.logException(e);
        }
    }

    void removeHostile(String name) {
        try {
            this.context.removeHostile(name);
            this.logger.logRemovedHostile(name);
        } catch (NoHostileInEncounterException e) {
            this.logger.logException(e);
        }
    }

    void removePlayerCharacter(String name) {
        try {
            this.context.removePlayerCharacter(name);
            this.logger.logRemovedPlayerCharacter(name);
        } catch (NoCharacterInEncounterException e) {
            this.logger.logException(e);
        }
    }

    void setMaxPlayerCount(int maxPlayerCount) {
        this.context.setMaxPlayerCount(maxPlayerCount);
        this.logger.logSetMaxPlayers(maxPlayerCount);
    }

    void skipPlayerTurn() {
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

    void startAttackPhase() {
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

    void startDodgePhase() {
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

    void startEncounter(MessageChannel channel, Role mentionRole) throws NoHostilesException {
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

    void viewEncounterSummary() {
        this.logger.logEncounterSummary(this.context.getAllPlayerCharacters(), this.context.getAllHostiles());
    }

    private void dodgeActionSkipped(User player) {
        try {
            if (!this.context.isDodgePhase()) {
                throw new WrongPhaseException("dodge", "dodge");
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
            this.logger.logEndEncounter(this.context.getAllPlayerCharacters(), this.context.getAllHostiles(), true);
        } else if (this.context.getActivePlayerCharacters().isEmpty()) {
            this.logger.logEndEncounter(this.context.getAllPlayerCharacters(), this.context.getAllHostiles(), false);
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
                        this.logger.logEndAttackPhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    } else if (this.context.isDodgePhase()) {
                        this.logger.logEndDodgePhase(
                            this.context.getAllPlayerCharacters(),
                            this.context.getAllHostiles()
                        );
                    }
                    this.context.endCurrentPhase();
                } else {
                    this.logger.pingPlayerTurn(nextPlayerCharacter);
                }
            }
        }
    }

    private PCEncounterData getPlayerCharacter(User owner) {
        PCEncounterData playerCharacter = this.context.getCurrentPlayerCharacter();
        if (!playerCharacter.getOwner().equals(owner)) {
            throw new NotYourTurnException();
        }
        return playerCharacter;
    }
}
