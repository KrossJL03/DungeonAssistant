package bot;

import bot.Entity.Hostile;
import bot.Entity.PlayerCharacter;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class PrivateLogger {

    private static String NEWLINE        = System.getProperty("line.separator");

    public PrivateLogger() {}


    void showHelpPage(User user, boolean isAdmin) {
//        EmbedBuilder   embed   = new EmbedBuilder();

//        embed.setTitle("Dungeon Assistant");
//        embed.setDescription("Help page for DungeonAssistant. Lists all existing commands.");
//        embed.setColor(new Color(0x62c4f9));
//        embed.setThumbnail("https://orig00.deviantart.net/5042/f/2018/092/5/3/53fe81050e0c6de55bb521f5b191749c-dc7pyqr.png");
//        embed.addField("$addCharacter [Name]", "Add a character to an encounter", true);
//        embed.addField("$attack [HostileName]", "Attack a hostile during the attack turn", true);
//        embed.addField("$createCharacter [Name] [STR] [DEF] [AGI] [WIS] [HP]", "Create record for a character", true);
//        embed.addField("$createHostile [Name] [HP] [AttackDice]", "Create record for a hostile", true);
//        embed.addField("$dodge", "Attempt to dodge enemy attacks during the dodge turn", true);
//        embed.addField("$help", "View a list of all commands", true);

        StringBuilder output = new StringBuilder();

        output.append("**DUNGEON ASSISTANT COMMANDS:**");
        output.append(PrivateLogger.NEWLINE);
        output.append(PrivateLogger.NEWLINE);
        output.append("General Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(String.format("   %-16s %-16s", "$createCharacter" ,"[Name] [STR] [DEF] [AGI] [WIS] [HP]"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s", "$createHostile", "[Name] [HP] [AttackDice]"));
            output.append(PrivateLogger.NEWLINE);
        } else {
            output.append(String.format("   %-16s %-16s", "$createCharacter" ,"[Name] [STR] [DEF] [AGI] [WIS] [HP]"));
            output.append(PrivateLogger.NEWLINE);
        }
        output.append("```");
        output.append(PrivateLogger.NEWLINE);
        output.append("Encounter Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "$attack", "[HostileName]", "Attack a hostile during the attack turn"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "$dodge", "", "Attempt to dodge enemy attacks during the dodge turn"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "$join", "[Name]", "Add a character to an encounter"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "$protect", "[Name]", "Protect a teammate during the dodge turn"));
        output.append("```");

        if (isAdmin) {
            output.append(PrivateLogger.NEWLINE);
            output.append("Dungeon Master Commands [$dm + COMMAND]:");
            output.append(PrivateLogger.NEWLINE);
            output.append("```ini");
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "addHostile", "[Species] (Name)", "Add a hostile, Name is optional"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "attackTurn", "", "Start attack turn"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "dodgeTurn", "", "Start dodge turn"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "healHostile", "[Name] [HP]", "Heal a hostile by X hitpoints"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "healPlayer", "[Name] [HP]", "Heal a player character by X hitpoints"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "hurtHostile", "[Name] [HP]", "Deal X damage to a hostile"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "hurtPlayer", "[Name] [HP]", "Deal X damage to a player character"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "maxPlayers", "[PlayerCount]", "Set number of players permitted"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "removeHostile", "[Name]", "Remove a hostile"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "removePlayer", "[Name]", "Remove a player"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "start", "", "Start the encounter"));
            output.append("```");
        }
        output.append(PrivateLogger.NEWLINE);
        output.append("View Commands [$view + COMMAND]:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(String.format("   %-16s %-16s %s", "players", "", "View all characters registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "hostiles", "", "View all hostiles registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        } else {
            output.append(String.format("   %-16s %-16s %s", "players", "", "View all characters registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        }
        output.append("```");

        this.sendPrivateMessage(user, output.toString());
    }


    void viewCharacters(User author, ArrayList<PlayerCharacter> playerCharacters) {
        StringBuilder output  = new StringBuilder();
        String        newLine = System.getProperty("line.separator");
        output.append("```cs");
        output.append(newLine);
        output.append("# Character Database");
        output.append(newLine);
        output.append("------------------------------------");
        output.append(newLine);
        for (PlayerCharacter character : playerCharacters) {
            output.append(
                String.format(
                    "%-25s %-20s " +
                        "------------------------------------------------" +
                        PrivateLogger.NEWLINE +
                        " HP %3d | STR %2d | DEF %2d | AGI %2d | WIS %2d ",
                    character.getOwner().getName(),
                    character.getName(),
                    character.getHitpoints(),
                    character.getStrength(),
                    character.getDefense(),
                    character.getAgility(),
                    character.getWisdom()
                )
            );
            output.append(newLine);
        }
        output.append("```");
        this.sendPrivateMessage(author, output.toString());
    }

    void viewHostiles(User author, ArrayList<Hostile> hostiles) {
        StringBuilder output  = new StringBuilder();
        String        newLine = System.getProperty("line.separator");
        output.append("```cs");
        output.append(newLine);
        output.append("# Hostile Database");
        output.append(newLine);
        output.append("------------------------------------");
        output.append(newLine);
        for (Hostile hostile : hostiles) {
            output.append(String.format(
                "%-25s %3d HP  %s%d",
                hostile.getSpecies(),
                hostile.getHitpoints(),
                "d ",
                hostile.getAttackDice()
            ));
            output.append(newLine);
        }
        output.append("```");
        this.sendPrivateMessage(author, output.toString());
    }

    private void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue( (channel) -> channel.sendMessage(content).queue() );
    }
}
