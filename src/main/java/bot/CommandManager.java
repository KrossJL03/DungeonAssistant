package bot;

import bot.Encounter.EncounterHolder;
import bot.Encounter.EncounterManager;
import bot.Encounter.Tier.Tier;
import bot.Encounter.Tier.TierRegistry;
import bot.Hostile.Exception.HostileNotFoundException;
import bot.Hostile.Hostile;
import bot.Exception.*;
import bot.Hostile.HostileManager;
import bot.Item.Consumable.ConsumableManager;
import bot.Player.Player;
import bot.Player.PlayerManager;
import bot.Player.PlayerRepository;
import bot.PlayerCharacter.Exception.PlayerCharacterNotFoundException;
import bot.PlayerCharacter.PlayerCharacter;
import bot.Hostile.HostileRepository;
import bot.PlayerCharacter.PlayerCharacterManager;

import java.util.ArrayList;
import java.util.List;

import bot.Repository.RepositoryException;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Class to parse commands and feed input to specified managers
 */
public class CommandManager
{
    private EncounterHolder  encounterHolder;
    private EncounterManager encounterManager;

    public CommandManager(@NotNull EncounterManager encounterManager, @NotNull EncounterHolder encounterHolder)
    {
        this.encounterHolder = encounterHolder;
        this.encounterManager = encounterManager;
    }

    void addHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   species    = splitInput[1];
            String   name       = splitInput.length > 2 ? splitInput[2] : splitInput[1];
            this.encounterManager.addHostile(species, name);
        }
    }

    void attackCommand(MessageReceivedEvent event) {
        Player   player      = PlayerRepository.getPlayer(event.getAuthor().getId());
        String[] splitInput  = event.getMessage().getContentRaw().split("\\s+");
        String   hostileName = splitInput[1];
        this.encounterManager.attackAction(player, hostileName);
    }

    void boostStatCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            String   statName   = splitInput[2];
            int      statBoost  = Integer.parseInt(splitInput[3]);
            this.encounterManager.modifyStat(name, statName, statBoost);
        }
    }

    // todo move to RepositoryManager
    void createCharacterCommand(MessageReceivedEvent event)
    {
        if (encounterHolder.hasActiveEncounter()) {
            throw RepositoryException.createCommandLocked();
        }

        MessageChannel channel    = event.getChannel();
        User           author     = event.getAuthor();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");

        String name      = splitInput[2];
        int    HP        = Integer.parseInt(splitInput[3]);
        int    STR       = Integer.parseInt(splitInput[4]);
        int    DEF       = Integer.parseInt(splitInput[5]);
        int    AGI       = Integer.parseInt(splitInput[6]);
        int    WIS       = Integer.parseInt(splitInput[7]);
        String appLink   = splitInput[8];
        String statsLink = splitInput[9];

        PlayerCharacterManager.createPlayerCharacter(author.getId(), name, HP, STR, DEF, AGI, WIS, appLink, statsLink);
        // todo move to RepositoryLogger
        channel.sendMessage(String.format("%s record has been saved!", name)).queue();
        this.viewCharacters(event);
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

        PlayerCharacterManager.deletePlayerCharacter(author.getId(), name);
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

    void dodgePassCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.dodgePassAction();
        } else {
            Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
            this.encounterManager.dodgePassActionHelp(player);
        }
    }

    void dropStatCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            String   statName   = splitInput[2];
            int      statBoost  = 0 - Integer.parseInt(splitInput[3]);
            this.encounterManager.modifyStat(name, statName, statBoost);
        }
    }

    void endActionCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.endCurrentPlayersAction();
        }
    }

    void endEncounterCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.endEncounter();
        }
    }

    void endTurnCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            this.encounterManager.endCurrentPlayersTurn();
        }
    }

    void healCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            int      hitpoints  = Integer.parseInt(splitInput[2]);
            this.encounterManager.heal(name, hitpoints);
        }
    }

    void healAllHostilesCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            int      hitpoints  = Integer.parseInt(splitInput[1]);
            this.encounterManager.healAllHostiles(hitpoints);
        }
    }

    void healAllPcsCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            int      hitpoints  = Integer.parseInt(splitInput[1]);
            this.encounterManager.healAllPcs(hitpoints);
        }
    }

    void helloCommand(MessageReceivedEvent event) {
        MessageChannel channel  = event.getChannel();
        String         nickname = event.getMember().getNickname();
        if (nickname == null) {
            nickname = event.getAuthor().getName();
        }
        PlayerManager.savePlayer(event.getAuthor().getId(), nickname);
        // todo move to RepositoryLogger
        channel.sendMessage(String.format("Hi %s! I've got all your information down now.", nickname)).queue();
    }

    void helpCommand(MessageReceivedEvent event) {
        PrivateLogger.showHelpPage(event.getAuthor(), this.isAdmin(event));
    }

    void hurtCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            int      hitpoints  = Integer.parseInt(splitInput[2]);
            this.encounterManager.hurt(name, hitpoints);
        }
    }

    void hurtAllHostilesCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            int      hitpoints  = Integer.parseInt(splitInput[1]);
            this.encounterManager.hurtAllHostiles(hitpoints);
        }
    }

    void hurtAllPcsCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            int      hitpoints  = Integer.parseInt(splitInput[1]);
            this.encounterManager.hurtAllPcs(hitpoints);
        }
    }

    void joinCommand(MessageReceivedEvent event) {
        MessageChannel channel    = event.getChannel();
        User           owner      = event.getAuthor();
        String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
        String         name       = splitInput[1];

        try {
            PlayerCharacter playerCharacter = PlayerCharacterManager.getMyPC(owner.getId(), name);
            this.encounterManager.joinEncounter(playerCharacter);
        } catch (PlayerCharacterNotFoundException e) {
            // todo create new logger
            ArrayList<PlayerCharacter> ownersPCs = PlayerCharacterManager.getAllMyPCs(owner.getId());
            if (!ownersPCs.isEmpty()) {
                StringBuilder output = new StringBuilder();
                output.append(String.format(
                    "I don't think I know %s, but I do know these characters: %s", name, ownersPCs
                ));
                channel.sendMessage(output).queue();
            } else {
                channel.sendMessage(
                    String.format(
                        "I don't remember %s, who are they again? Please tell me using the `%screate character` command",
                        name,
                        CommandListener.COMMAND_KEY
                    )
                ).queue();
            }
        }
    }

    void kickCommand(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            this.encounterManager.kick(name);
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

    void removeExplorer(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            this.encounterManager.removePlayerCharacter(name);
        }
    }

    void removeHostile(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   name       = splitInput[1];
            this.encounterManager.removeHostile(name);
        }
    }

    void rejoinCommand(MessageReceivedEvent event) {
        Player player = PlayerRepository.getPlayer(event.getAuthor().getId());
        this.encounterManager.rejoinEncounter(player);
    }

    void setMaxPlayers(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput     = event.getMessage().getContentRaw().split("\\s+");
            int      maxPlayerCount = Integer.parseInt(splitInput[1]);
            this.encounterManager.setMaxPlayerCount(maxPlayerCount);
        }
    }

    /**
     * Set tier command
     *
     * @param event Message event
     */
    void setTierCommand(@NotNull MessageReceivedEvent event) {
        if (isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   tierName   = splitInput[1];
            Tier tier           = TierRegistry.getTier(tierName);
            encounterManager.setTier(tier);
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

    /**
     * Use Item Command
     * todo reimplement later
     *
     * @param event MessageReceivedEvent
     */
    void useItemCommand(MessageReceivedEvent event) {
        this.pingDmItemUsed(event);
//        if (this.isAdmin(event)) {
//            Player         player     = PlayerRepository.getPlayer(event.getAuthor().getId());
//            String[]       splitInput = event.getMessage().getContentRaw().split("\\s+");
//            ConsumableItem item       = ConsumableManager.getItem(splitInput[1]);
//            String[] context = splitInput.length > 2 ?
//                               Arrays.copyOfRange(splitInput, 2, splitInput.length) :
//                               new String[0];
//            this.encounterManager.useItem(player, item, context);
//        }
    }

    void viewAllCharacters(MessageReceivedEvent event) {
        RepositoryLogger.viewCharacters(
            event.getChannel(),
            PlayerCharacterManager.getAllPCs()
        );
    }

    void viewCharacters(MessageReceivedEvent event) {
        RepositoryLogger.viewCharactersWithStats(
            event.getChannel(),
            PlayerCharacterManager.getAllMyPCs(event.getAuthor().getId())
        );
    }

    void viewEncounterSummary() {
        this.encounterManager.viewEncounterSummary();
    }

    void viewHostileLoot(MessageReceivedEvent event) {
        if (this.isAdmin(event)) {
            String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
            String   species    = splitInput[2];
            Hostile  hostile    = HostileRepository.getHostile(species);
            if (hostile != null) {
                RepositoryLogger.viewHostileLoot(event.getChannel(), hostile);
            } else {
                throw HostileNotFoundException.createNotInDatabase(species);
            }
        }
    }

    void viewHostiles(MessageReceivedEvent event) {
        RepositoryLogger.viewHostiles(event.getChannel(), HostileRepository.getAllHostiles());
    }

    void viewItem(MessageReceivedEvent event) {
        String[] splitInput = event.getMessage().getContentRaw().split("\\s+");
        String   name       = splitInput[2];
        RepositoryLogger.viewItem(event.getChannel(), ConsumableManager.getItem(name));
    }

    void viewItems(MessageReceivedEvent event) {
        RepositoryLogger.viewItems(event.getChannel(), ConsumableManager.getAllItems());
    }

    //todo remove
    void pingDmItemUsed(MessageReceivedEvent event) {
        this.encounterManager.pingDmItemUsed(PlayerRepository.getPlayer(event.getAuthor().getId()));
    }

    private Role getDungeonMaster(MessageReceivedEvent event) {
        List<Role> roles = event.getGuild().getRolesByName("Dungeon Master", false);
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
        return event.getMember().getRoles().indexOf(this.getDungeonMaster(event)) > -1;
    }
}
