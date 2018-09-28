package bot;

import bot.Entity.Hostile;
import bot.Exception.*;
import bot.Entity.PlayerCharacter;
import bot.Repository.HostileRepository;
import bot.Repository.PCRepository;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandManager {

    private EncounterManager  encounterManager;
    private HostileRepository hostileRepository;
    private PCRepository      pcRepository;
    private PrivateLogger     privateLogger;

    public CommandManager(
        PrivateLogger privateLogger,
        EncounterManager encounterManager,
        PCRepository pcRepository,
        HostileRepository hostileRepository
    ) {
        this.encounterManager = encounterManager;
        this.hostileRepository = hostileRepository;
        this.pcRepository = pcRepository;
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
        String[] splitInput  = event.getMessage().getContentRaw().split("\\s+");
        String   hostileName = splitInput[1];
        this.encounterManager.attackAction(event.getAuthor(), hostileName);
    }

    // todo create RepositoryManager
    void createCharacterCommand(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");

        String          name            = splitInput[1];
        int             STR             = Integer.parseInt(splitInput[2]);
        int             DEF             = Integer.parseInt(splitInput[3]);
        int             AGI             = Integer.parseInt(splitInput[4]);
        int             WIS             = Integer.parseInt(splitInput[5]);
        int             HP              = Integer.parseInt(splitInput[6]);
        PlayerCharacter playerCharacter = new PlayerCharacter(event.getAuthor(), name, STR, DEF, AGI, WIS, HP);

        this.pcRepository.addPC(playerCharacter);
        channel.sendMessage(
            String.format("%s record has been saved! %s", playerCharacter.getName(), playerCharacter.print())
        ).queue();
    }

    void createEncounter(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.createEncounter(event.getChannel(), this.getDungeonMaster(event));
        }
    }

    // todo create RepositoryManager
    void createHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            MessageChannel channel    = event.getChannel();
            String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
            String         species    = splitInput[1];
            int            hitpoints  = Integer.parseInt(splitInput[2]);
            int            attackDice = Integer.parseInt(splitInput[3]);
            Hostile        hostile    = new Hostile(species, hitpoints, attackDice);

            this.hostileRepository.addHostile(hostile);
            channel.sendMessage(
                String.format("%s record has been saved! %s", hostile.getSpecies(), hostile.print())
            ).queue();
        }
    }

    void dodgeCommand(MessageReceivedEvent event) {
        this.encounterManager.dodgeAction(event.getAuthor());
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
            PlayerCharacter playerCharacter = this.pcRepository.getMyPC(owner, name);
            this.encounterManager.joinEncounter(playerCharacter);
        } catch (NoCharacterFoundException e) {
            // todo create new logger
            ArrayList<PlayerCharacter> ownersPCs = this.pcRepository.getAllMyPCs(owner);
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

    void protectCommand(MessageReceivedEvent event) {
        String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
        String   name       = splitInput[1];
        this.encounterManager.protectAction(event.getAuthor(), name);
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
        this.privateLogger.viewCharacters(event.getAuthor(), this.pcRepository.getAllPC());
    }

    void viewEncounterSummary() {
        this.encounterManager.viewEncounterSummary();
    }

    void viewHostiles(MessageReceivedEvent event) {
        this.privateLogger.viewHostiles(event.getAuthor(), this.hostileRepository.getAllHostiles());
    }

    void populate(User owner) {
        PlayerCharacter froyo  = new PlayerCharacter(owner, "Froyo", 4, 3, 4, 13, 170);
        PlayerCharacter rose   = new PlayerCharacter(owner, "Rose", 20, 5, 20, 20, 195);
        PlayerCharacter toffee = new PlayerCharacter(owner, "ButterToffee", 12, 6, 12, 14, 135);
        PlayerCharacter cl     = new PlayerCharacter(owner, "CocoaLiquor", 19, 13, 20, 20, 165);

        Hostile culebratu = new Hostile("Culebratu", 100, 30);
        Hostile shaman    = new Hostile("CulebratuShaman", 250, 30);
        Hostile volpup    = new Hostile("Volpup", 100, 15);
        Hostile volpire   = new Hostile("Volpire", 200, 30);

        this.pcRepository.addPC(froyo);
//        this.pcRepository.addPC(rose);
//        this.pcRepository.addPC(toffee);
//        this.pcRepository.addPC(cl);

        this.hostileRepository.addHostile(culebratu);
        this.hostileRepository.addHostile(shaman);
        this.hostileRepository.addHostile(volpup);
        this.hostileRepository.addHostile(volpire);

        this.encounterManager.addHostile(culebratu.getSpecies(), culebratu.getSpecies());
        this.encounterManager.addHostile(shaman.getSpecies(), shaman.getSpecies());
        this.encounterManager.addHostile(volpup.getSpecies(), volpup.getSpecies());
        this.encounterManager.addHostile(volpup.getSpecies(), volpup.getSpecies());
        this.encounterManager.addHostile(volpire.getSpecies(), volpire.getSpecies());

        this.encounterManager.setMaxPlayerCount(5);
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
