package bot;

import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import bot.PlayerCharacter.PlayerCharacter;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Hashtable;

class EncyclopediaLogger {

    private static String NEWLINE = System.getProperty("line.separator");

    static void viewCharacters(MessageChannel channel, ArrayList<PlayerCharacter> playerCharacters) {
        StringBuilder output = new StringBuilder();
        output.append("```cs");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("# Character Database #");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        for (PlayerCharacter character : playerCharacters) {
            output.append(String.format("'%s' /*%s*/", character.getName(), character.getOwner().getName()));
            output.append(bot.EncyclopediaLogger.NEWLINE);
        }
        output.append("```");
        EncyclopediaLogger.logMessage(channel, output.toString());
    }

    static void viewHostileLoot(MessageChannel channel, Hostile hostile) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append(String.format("'%s' loot", hostile.getSpecies()));
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        for (int i = 1; i < 11; i++) {
            Loot loot = hostile.getLoot(i);
            if (loot != null) {
                output.append(String.format("roll %2d --> x%d %s", i, loot.getQuantity(), loot.getItem()));
                output.append(bot.EncyclopediaLogger.NEWLINE);
            }
        }
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("```");
        EncyclopediaLogger.logMessage(channel, output.toString());
    }

    static void viewHostiles(MessageChannel channel, ArrayList<Hashtable<String, String>> hostileInfos) {
        StringBuilder output = new StringBuilder();
        output.append("```cs");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("# Hostile Database");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        for (Hashtable<String, String> hostileInfo : hostileInfos) {
            output.append(String.format(
                "%-20s %3s★  %3s HP  %s%s",
                hostileInfo.get("species"),
                hostileInfo.get("danger_level"),
                hostileInfo.get("hitpoints"),
                "d",
                hostileInfo.get("attack_dice")
            ));
            output.append(bot.EncyclopediaLogger.NEWLINE);
        }
        output.append("```");
        EncyclopediaLogger.logMessage(channel, output.toString());
    }

    private static void logMessage(MessageChannel channel, String content) {
        channel.sendMessage(content).queue();
    }

}
