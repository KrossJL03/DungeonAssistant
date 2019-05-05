package bot;

import bot.Hostile.Hostile;
import bot.Hostile.Loot;
import bot.Item.Consumable.ConsumableItem;
import bot.Item.ItemAbstract;
import bot.Explorer.Explorer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

class RepositoryLogger {

    private static int    EMBED_COLOR = 0x62c4f9;
    private static String NEWLINE     = System.getProperty("line.separator");

    static void viewCharacters(MessageChannel channel, ArrayList<Explorer> explorers) {
        StringBuilder output = new StringBuilder();
        output.append("```md");
        output.append(RepositoryLogger.NEWLINE);
        output.append("          CHARACTER DATABASE           ");
        output.append(RepositoryLogger.NEWLINE);
        output.append("=======================================");
        output.append(RepositoryLogger.NEWLINE);
        output.append("Name            < Player >");
        output.append(RepositoryLogger.NEWLINE);
        output.append("---------------------------------------");
        output.append(RepositoryLogger.NEWLINE);
        for (Explorer character : explorers) {
            output.append(String.format("%-15s < %s >", character.getName(), character.getOwner().getName()));
            output.append(RepositoryLogger.NEWLINE);
        }
        output.append("```");
        RepositoryLogger.logMessage(channel, output.toString());
    }

    static void viewCharactersWithStats(MessageChannel channel, ArrayList<Explorer> explorers) {
        StringBuilder output = new StringBuilder();
        output.append("```md");
        output.append(RepositoryLogger.NEWLINE);
        output.append("          CHARACTER DATABASE           ");
        output.append(RepositoryLogger.NEWLINE);
        output.append("=======================================");
        output.append(RepositoryLogger.NEWLINE);
        output.append("Name            <  HP STR DEF AGI WIS >");
        output.append(RepositoryLogger.NEWLINE);
        output.append("---------------------------------------");
        output.append(RepositoryLogger.NEWLINE);
        for (Explorer character : explorers) {
            output.append(
                String.format(
                    "%-15s < %3d %2d %3d %3d %3d  >",
                    character.getName(),
                    character.getHitpoints(),
                    character.getStrength(),
                    character.getDefense(),
                    character.getAgility(),
                    character.getWisdom()
                )
            );
            output.append(RepositoryLogger.NEWLINE);
        }
        output.append("```");
        RepositoryLogger.logMessage(channel, output.toString());
    }

    static void viewHostileLoot(MessageChannel channel, Hostile hostile) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(RepositoryLogger.NEWLINE);
        output.append(String.format("'%s' loot", hostile.getSpecies()));
        output.append(RepositoryLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(RepositoryLogger.NEWLINE);
        for (int i = 1; i < 11; i++) {
            Loot loot = hostile.getLoot(i);
            if (loot != null) {
                output.append(String.format("roll %2d --> x%d %s", i, loot.getQuantity(), loot.getItem()));
                output.append(RepositoryLogger.NEWLINE);
            }
        }
        output.append(RepositoryLogger.NEWLINE);
        output.append("```");
        RepositoryLogger.logMessage(channel, output.toString());
    }

    static void viewHostiles(MessageChannel channel, ArrayList<Hashtable<String, String>> hostileInfos) {
        StringBuilder output = new StringBuilder();
        output.append("```cs");
        output.append(RepositoryLogger.NEWLINE);
        output.append("# Hostile Database");
        output.append(RepositoryLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(RepositoryLogger.NEWLINE);
        for (Hashtable<String, String> hostileInfo : hostileInfos) {
            output.append(String.format(
                "%-20s %3s★  %3s HP  %s%s",
                hostileInfo.get("species"),
                hostileInfo.get("danger_level"),
                hostileInfo.get("hitpoints"),
                "d",
                hostileInfo.get("attack_dice")
            ));
            output.append(RepositoryLogger.NEWLINE);
        }
        output.append("```");
        RepositoryLogger.logMessage(channel, output.toString());
    }

    static void viewItem(MessageChannel channel, ItemAbstract item) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(item.getName());
        embed.setColor(new Color(RepositoryLogger.EMBED_COLOR));
        embed.setThumbnail(item.getImage());
        embed.setDescription(item.getDescription());
        embed.addField("Effect", item.getShortDescription(), false);
        if (item.isBuyable()) {
            embed.addField(
                "Value",
                String.format("%dc | %dc", item.getBuyValue(), item.getSellValue()),
                true
            );
        }
        if (item.isSeasonal()) {
            embed.addField("Seasonal", item.getSeasonalMonth(), true);
        }
        if (item instanceof ConsumableItem) {
            embed.addField(
                "Usable Phase",
                ((ConsumableItem) item).getUsablePhase(),
                true
            );
        }
        RepositoryLogger.logEmbed(channel, embed.build());
    }

    static void viewItems(MessageChannel channel, ArrayList<ConsumableItem> items) {
        StringBuilder output = new StringBuilder();
        output.append("```md");
        output.append(RepositoryLogger.NEWLINE);
        output.append("            ITEM DATABASE              ");
        output.append(RepositoryLogger.NEWLINE);
        output.append("========================================");
        output.append(RepositoryLogger.NEWLINE);
        for (ConsumableItem item : items) {
            String description = item.getShortDescription();
            output.append(String.format("< %s >", item.getName()));
            output.append(RepositoryLogger.NEWLINE);
            output.append(String.format(">  %s", description));
            output.append(RepositoryLogger.NEWLINE);
        }
        output.append("```");
        RepositoryLogger.logMessage(channel, output.toString());
    }

    private static void logMessage(MessageChannel channel, String content) {
        channel.sendMessage(content).queue();
    }

    private static void logEmbed(MessageChannel channel, MessageEmbed content) {
        channel.sendMessage(content).queue();
    }
}
