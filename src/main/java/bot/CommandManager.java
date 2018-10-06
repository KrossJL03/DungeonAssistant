package bot;

import bot.Hostile.Exception.NoHostileFoundException;
import bot.Hostile.Hostile;
import bot.Exception.*;
import bot.Hostile.HostileManager;
import bot.Player.Player;
import bot.Player.PlayerRepository;
import bot.PlayerCharacter.PlayerCharacter;
import bot.Hostile.HostileRepository;
import bot.PlayerCharacter.PlayerCharacterRepository;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandManager {

    private EncounterManager   encounterManager;
    private EncyclopediaLogger encyclopediaLogger;
    private PrivateLogger      privateLogger;

    public CommandManager(
        EncounterManager encounterManager,
        EncyclopediaLogger encyclopediaLogger,
        PrivateLogger privateLogger
    ) {
        this.encounterManager = encounterManager;
        this.encyclopediaLogger = encyclopediaLogger;
        this.privateLogger = privateLogger;
    }

    void addHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   species    = splitInput[2];
            String   name       = splitInput.length > 3 ? splitInput[3] : splitInput[2];
            this.encounterManager.addHostile(species, name);
        }
    }

    void attackCommand(MessageReceivedEvent event) {
        Player   player      = PlayerRepository.getPlayer(event.getAuthor().getId());
        String[] splitInput  = event.getMessage().getContentRaw().split("\\s+");
        String   hostileName = splitInput[1];
        this.encounterManager.attackAction(player, hostileName);
    }

    // todo move to RepositoryManager
    void createCharacterCommand(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        User           author     = event.getAuthor();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");

        String name = splitInput[1];
        int    STR  = Integer.parseInt(splitInput[2]);
        int    DEF  = Integer.parseInt(splitInput[3]);
        int    AGI  = Integer.parseInt(splitInput[4]);
        int    WIS  = Integer.parseInt(splitInput[5]);
        int    HP   = Integer.parseInt(splitInput[6]);
        PlayerRepository.addPlayerIfNotExists(author.getId(), author.getName());
        PlayerCharacterRepository.createPlayerCharacter(author.getId(), name, STR, DEF, AGI, WIS, HP);
        // todo move to RepositoryLogger
        channel.sendMessage(String.format("%s record has been saved!", name)).queue();
    }

    void createEncounter(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.createEncounter(event.getChannel(), this.getDungeonMaster(event));
        }
    }

    void createHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            MessageChannel channel     = event.getChannel();
            String[]       splitInput  = event.getMessage().getContentRaw().split("\\s+");
            String         species     = splitInput[1];
            int            dangerLevel = Integer.parseInt(splitInput[2]);
            int            hitpoints   = Integer.parseInt(splitInput[3]);
            int            attackDice  = Integer.parseInt(splitInput[4]);

            HostileManager.createHostile(species, dangerLevel, hitpoints, attackDice);
            // todo move to RepositoryLogger
            channel.sendMessage(String.format("%s record has been saved!", species)).queue();
        }
    }

    void deleteCharacterCommand(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        User           author     = event.getAuthor();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
        String         name       = splitInput[1];

        PlayerCharacterRepository.removePlayerCharacterIfExists(author.getId(), name);
        // todo move to RepositoryLogger
        channel.sendMessage(String.format("%s record has been deleted!", name)).queue();
    }

    void deleteHostile(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
        String         species    = splitInput[1];

        HostileRepository.deleteHostileIfExists(species);
        // todo move to RepositoryLogger
        channel.sendMessage(String.format("%s record has been deleted!", species)).queue();
    }

    void dodgeCommand(MessageReceivedEvent event) {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        this.encounterManager.dodgeAction(player);
    }

    void healHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            int      hitpoints  = Integer.parseInt(splitInput[3]);
            this.encounterManager.healHostile(name, hitpoints);
        }
    }

    void healPlayer(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            int      hitpoints  = Integer.parseInt(splitInput[3]);
            this.encounterManager.healPlayer(name, hitpoints);
        }
    }

    void helpCommand(MessageReceivedEvent event) {
        this.privateLogger.showHelpPage(event.getAuthor(), this.isAdmin(event));
    }

    void hurtHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            int      hitpoints  = Integer.parseInt(splitInput[3]);
            this.encounterManager.hurtHostile(name, hitpoints);
        }
    }

    void hurtPlayer(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            int      hitpoints  = Integer.parseInt(splitInput[3]);
            this.encounterManager.hurtPlayer(name, hitpoints);
        }
    }

    void joinEncounter(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        User           owner      = event.getAuthor();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
        String         name       = splitInput[1];

        try {
            PlayerCharacter playerCharacter = PlayerCharacterRepository.getMyPC(owner.getId(), name);
            this.encounterManager.joinEncounter(playerCharacter);
        } catch (NoCharacterFoundException e) {
            // todo create new logger
            ArrayList<PlayerCharacter> ownersPCs = PlayerCharacterRepository.getAllMyPCs(owner.getId());
            if (!ownersPCs.isEmpty()) {
                StringBuilder output = new StringBuilder();
                output.append(String.format(
                    "I don't think I know %s, but I do know these characters: %s", name, ownersPCs
                ));
                channel.sendMessage(output).queue();
            } else {
                channel.sendMessage(String.format(
                    "I don't remember %s, who are they again? Please tell me using the `$createCharacter` command", name
                )).queue();
            }
        }
    }

    void leaveCommand(MessageReceivedEvent event) {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        this.encounterManager.leaveEncounter(player);
    }

    void lootCommand(MessageReceivedEvent event) {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        this.encounterManager.lootAction(player);
    }

    void protectCommand(MessageReceivedEvent event) {
        Player   player     = PlayerRepository.getPlayer(event.getAuthor().getId());
        String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
        String   name       = splitInput[1];
        this.encounterManager.protectAction(player, name);
    }

    void removePlayerCharacter(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            this.encounterManager.removePlayerCharacter(name);
        }
    }

    void removeHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[2];
            this.encounterManager.removeHostile(name);
        }
    }

    void returnCommand(MessageReceivedEvent event) {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        this.encounterManager.returnToEncounter(player);
    }

    void setMaxPlayers(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput     = event.getMessage().getContentRaw().split("\\s+");
            int      maxPlayerCount = Integer.parseInt(splitInput[2]);
            this.encounterManager.setMaxPlayerCount(maxPlayerCount);
        }
    }

    void skipCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.skipPlayerTurn();
        }
    }

    void startAttackPhase(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.startAttackPhase();
        }
    }

    void startDodgePhase(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.startDodgePhase();
        }
    }

    void startEncounter(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.startEncounter(event.getChannel(), event.getGuild().getPublicRole());
        }
    }

    void viewCharacters(MessageReceivedEvent event) {
        this.encyclopediaLogger.viewCharacters(event.getChannel(), PlayerCharacterRepository.getAllPC());
    }

    void viewEncounterSummary() {
        this.encounterManager.viewEncounterSummary();
    }

    void viewHostileLoot(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            try {
                String[] splitInput  = event.getMessage().getContentRaw().split("\\s+");
                String   speciesName = splitInput[2];
                Hostile  hostile     = HostileRepository.getHostile(speciesName);
                this.encyclopediaLogger.viewHostileLoot(event.getChannel(), hostile);
            } catch (NoHostileFoundException e) {
                this.encyclopediaLogger.logException(event.getChannel(), e);
            }
        }
    }

    void viewHostiles(MessageReceivedEvent event) {
        this.encyclopediaLogger.viewHostiles(event.getChannel(), HostileRepository.getAllHostiles());
    }

    void populate(User owner) {
//        PlayerCharacter froyo       = new PlayerCharacter(owner, "Froyo", 4, 3, 4, 13, 170);
//        PlayerCharacter babaGanoush = new PlayerCharacter(owner, "BabaGanoush", 20, 20, 20, 20, 240);
//        PlayerCharacter rose        = new PlayerCharacter(owner, "Rose", 20, 5, 20, 20, 195);
//        PlayerCharacter toffee      = new PlayerCharacter(owner, "ButterToffee", 12, 6, 12, 14, 135);
//        PlayerCharacter cl          = new PlayerCharacter(owner, "CocoaLiquor", 19, 13, 20, 20, 165);

//        this.pcRepository.addPC(froyo);
//        this.pcRepository.addPC(babaGanoush);
//        this.pcRepository.addPC(rose);
//        this.pcRepository.addPC(toffee);
//        this.pcRepository.addPC(cl);

//        this.encounterManager.insertHostile("culebratu", "culebratu");
//        this.encounterManager.insertHostile("culebratushaman", "culebratushaman");
//        this.encounterManager.insertHostile("volpup", "volpup");
//        this.encounterManager.insertHostile("volpup", "volpup");
//        this.encounterManager.insertHostile("volpire", "volpire");

//        this.encounterManager.setMaxPlayerCount(5);
    }

    private Role getDungeonMaster(MessageReceivedEvent event) {
        List<Role> roles = event.getGuild().getRolesByName("DungeonMaster", false);
        if (!roles.isEmpty()) {
            return roles.get(0);
        }
        throw new DungeonMasterNotFoundException();
    }

    private boolean isAdmin(MessageReceivedEvent event) {
        // todo remove after testing
        if (event.getAuthor().getName().equals("JKSketchy")) {
            return true;
        }
        return event.getMessage().getMember().getRoles().indexOf(this.getDungeonMaster(event)) > -1;
    }
}
