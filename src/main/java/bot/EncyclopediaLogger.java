package bot;

import bot.Entity.Hostile;
import bot.Entity.Loot;
import bot.Entity.PlayerCharacter;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;

public class EncyclopediaLogger {

    private static String NEWLINE = System.getProperty("line.separator");

    public EncyclopediaLogger() {}

    void logException(MessageChannel channel, Throwable e) {
        channel.sendMessage(e.getMessage()).queue();
    }

    void viewCharacters(MessageChannel channel, ArrayList<PlayerCharacter> playerCharacters) {
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
            output.append("------------------------------------------------");
            output.append(bot.EncyclopediaLogger.NEWLINE);
            output.append(
                String.format(
                    " HP %3d | STR %2d | DEF %2d | AGI %2d | WIS %2d ",
                    character.getHitpoints(),
                    character.getStrength(),
                    character.getDefense(),
                    character.getAgility(),
                    character.getWisdom()
                )
            );
            output.append(bot.EncyclopediaLogger.NEWLINE);
        }
        output.append("```");
        this.logMessage(channel, output.toString());
    }

    void viewHostileLoot(MessageChannel channel, Hostile hostile) {
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
        this.logMessage(channel, output.toString());
    }

    void viewHostiles(MessageChannel channel, ArrayList<Hostile> hostiles) {
        StringBuilder output = new StringBuilder();
        output.append("```cs");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("# Hostile Database");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(bot.EncyclopediaLogger.NEWLINE);
        for (Hostile hostile : hostiles) {
            output.append(String.format(
                "%-25s %3d HP  %s%d",
                hostile.getSpecies(),
                hostile.getHitpoints(),
                "d ",
                hostile.getAttackDice()
            ));
            output.append(bot.EncyclopediaLogger.NEWLINE);
        }
        output.append("```");
        this.logMessage(channel, output.toString());
    }

    private void logMessage(MessageChannel channel, String content) {
        channel.sendMessage(content).queue();
    }

}
