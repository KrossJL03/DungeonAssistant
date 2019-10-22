package bot.Registry;

import bot.Explorer.Explorer;
import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.Hashtable;

public class RegistryLogger
{
    private static String NEWLINE     = System.getProperty("line.separator");

    /**
     * Log explorers with details
     *
     * @param channel   Channel to log to
     * @param explorers Explorers to log
     */
    public void logExplorersWithDetails(MessageChannel channel, ArrayList<Explorer> explorers)
    {
        // todo convert to MessageBuilder
        StringBuilder output = new StringBuilder();
        output.append("```md");
        output.append(RegistryLogger.NEWLINE);
        output.append("          CHARACTER DATABASE           ");
        output.append(RegistryLogger.NEWLINE);
        output.append("=======================================");
        output.append(RegistryLogger.NEWLINE);
        output.append("Name            <  HP STR WIS AGI DEF >");
        output.append(RegistryLogger.NEWLINE);
        output.append("---------------------------------------");
        output.append(RegistryLogger.NEWLINE);
        for (Explorer character : explorers) {
            output.append(
                String.format(
                    "%-15s < %3d %2d %3d %3d %3d  >",
                    character.getName(),
                    character.getHitpoints(),
                    character.getStrength(),
                    character.getWisdom(),
                    character.getAgility(),
                    character.getDefense()
                )
            );
            output.append(RegistryLogger.NEWLINE);
        }
        output.append("```");
        logMessage(channel, output.toString());
    }

    /**
     * Log explorers with owners
     *
     * @param channel   Channel to log to
     * @param explorers Explorers to log
     */
    public void logExplorersWithOwners(MessageChannel channel, ArrayList<Explorer> explorers)
    {
        // todo convert to MessageBuilder
        StringBuilder output = new StringBuilder();
        output.append("```md");
        output.append(RegistryLogger.NEWLINE);
        output.append("          CHARACTER DATABASE           ");
        output.append(RegistryLogger.NEWLINE);
        output.append("=======================================");
        output.append(RegistryLogger.NEWLINE);
        output.append("Name            < Player >");
        output.append(RegistryLogger.NEWLINE);
        output.append("---------------------------------------");
        output.append(RegistryLogger.NEWLINE);
        for (Explorer character : explorers) {
            output.append(String.format("%-15s < %s >", character.getName(), character.getOwner().getName()));
            output.append(RegistryLogger.NEWLINE);
        }
        output.append("```");
        logMessage(channel, output.toString());
    }

    /**
     * Log hostile info
     *
     * @param channel      Channel to log message to
     * @param hostilesInfo Hostile info
     */
    public void logHostileInfo(MessageChannel channel, ArrayList<Hashtable<String, String>> hostilesInfo)
    {
        // todo convert to MessageBuilder
        StringBuilder output = new StringBuilder();
        output.append("```cs");
        output.append(RegistryLogger.NEWLINE);
        output.append("# Hostile Database");
        output.append(RegistryLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(RegistryLogger.NEWLINE);
        for (Hashtable<String, String> hostileInfo : hostilesInfo) {
            output.append(String.format(
                "%-20s %3s★  %4s HP  %sd%s",
                hostileInfo.get("species"),
                hostileInfo.get("dangerLevel"),
                hostileInfo.get("hitpoints"),
                hostileInfo.get("attackCount"),
                hostileInfo.get("attack")
            ));
            output.append(RegistryLogger.NEWLINE);
        }
        output.append("```");
        logMessage(channel, output.toString());
    }

    /**
     * Log hostile loot
     *
     * @param channel Channel to log message to
     * @param hostile Hostile with loot to log
     */
    public void logHostileLoot(MessageChannel channel, Hostile hostile)
    {
        // todo convert to MessageBuilder
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(RegistryLogger.NEWLINE);
        output.append(String.format("'%s' loot", hostile.getSpecies()));
        output.append(RegistryLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(RegistryLogger.NEWLINE);
        for (int i = 1; i < hostile.getLootPoolSize() + 1; i++) {
            Loot loot = hostile.getLoot(i);
            output.append(String.format("roll %2d --> x%d %s", i, loot.getQuantity(), loot.getItem()));
            output.append(RegistryLogger.NEWLINE);
        }
        output.append(RegistryLogger.NEWLINE);
        output.append("```");
        logMessage(channel, output.toString());
    }

    /**
     * Log message
     *
     * @param channel Channel to log message to
     * @param message Message to log
     */
    private void logMessage(MessageChannel channel, String message)
    {
        channel.sendMessage(message).queue();
    }

    /**
     * Log embed
     *
     * @param channel Channel to log message to
     * @param embed   Message embed to log
     */
    private void logEmbed(MessageChannel channel, MessageEmbed embed)
    {
        channel.sendMessage(embed).queue();
    }
}
