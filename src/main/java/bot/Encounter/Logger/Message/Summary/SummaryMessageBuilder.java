package bot.Encounter.Logger.Message.Summary;

import bot.Encounter.EncounteredCreatureInterface;
import bot.Encounter.EncounteredCreature.Slayer;
import bot.Encounter.EncounteredExplorerInterface;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.Logger.Message.*;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SummaryMessageBuilder
{
    private DiffCodeFormatter codeFormatter;

    /**
     * SummaryMessageBuilder constructor
     */
    public @NotNull SummaryMessageBuilder()
    {
        this.codeFormatter = new DiffCodeFormatter();
    }

    /**
     * Build summary message
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     *
     * @return String
     */
    public @NotNull String buildSummary(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        SummaryMessage message = new SummaryMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(WordUtils.capitalize("ENCOUNTER SUMMARY"));
        message.addBreak();
        message.addLine();
        message.add("Hostiles");
        message.addLine();

        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            message.add(getNameLine(encounteredHostile));
            String healthBar = getHealthBarLine(encounteredHostile);
            if (!healthBar.isEmpty()) {
                message.add(healthBar);
            }
        }

        message.addLine();
        message.add("Explorers");
        message.addLine();

        for (EncounteredExplorerInterface encounteredExplorer : encounteredExplorers) {
            message.add(getNameLine(encounteredExplorer));
            String healthBar = getHealthBarLine(encounteredExplorer);
            if (!healthBar.isEmpty()) {
                message.add(healthBar);
            }
        }
        message.endCodeBlock();

        return message.getAsString();
    }

    /**
     * Get health bar line
     *
     * @param creature Creature
     *
     * @return String
     */
    private @NotNull String getHealthBarLine(EncounteredCreatureInterface creature)
    {
        StringBuilder output    = new StringBuilder();
        int           currentHP = creature.getCurrentHP();
        int           maxHP     = creature.getMaxHP();

        if (!creature.isActive()) {
            return "";
        }

        output.append(String.format("[%3s/%3s] ", currentHP > 999 ? "???" : currentHP, maxHP > 999 ? "???" : maxHP));
        int healthBlocks      = (int) Math.ceil(maxHP / 10) + 1;
        int emptyHealthBlocks = (int) Math.ceil((double) (maxHP - currentHP) / 10);
        int fullHealthBlocks  = healthBlocks - emptyHealthBlocks;
        output.append(this.repeatString(SummaryMessage.FULL_HEALTH_ICON, fullHealthBlocks));
        if (emptyHealthBlocks > 0) {
            output.append(this.repeatString(SummaryMessage.EMPTY_HEALTH_ICON, emptyHealthBlocks));
        }

        return creature.isBloodied()
               ? codeFormatter.makeRed(output.toString())
               : codeFormatter.makeGreen(output.toString());
    }

    /**
     * Get name line
     *
     * @param encounteredHostile Encountered hostile
     *
     * @return String
     */
    private @NotNull String getNameLine(EncounteredHostileInterface encounteredHostile)
    {
        if (encounteredHostile.isSlain()) {
            Slayer slayer = encounteredHostile.getSlayer();
            return codeFormatter.makeGrey(String.format(
                "%s was slain %s",
                encounteredHostile.getName(),
                slayer.exists() ? String.format(" by %s", slayer.getName()) : ""
            ));
        } else {
            return encounteredHostile.getName();
        }
    }

    /**
     * Get name line
     *
     * @param encounteredExplorer Encountered explorer
     *
     * @return String
     */
    private @NotNull String getNameLine(EncounteredExplorerInterface encounteredExplorer)
    {
        if (!encounteredExplorer.isPresent()) {
            return codeFormatter.makeGrey(String.format("%s has left", encounteredExplorer.getName()));
        } else if (encounteredExplorer.isSlain()) {
            Slayer slayer = encounteredExplorer.getSlayer();
            return codeFormatter.makeGrey(String.format(
                "%s was defeated %s",
                encounteredExplorer.getName(),
                slayer.exists() ? String.format("by %s", slayer.getName()) : ""
            ));
        } else {
            return String.format("%s [%s]", encounteredExplorer.getName(), encounteredExplorer.getOwner().getName());
        }
    }

    /**
     * Repeat the given string X amount of times
     *
     * @param string String to repeat
     * @param count  Number of times to repeat string
     *
     * @return String
     */
    private String repeatString(String string, int count)
    {
        return new String(new char[count]).replace("\0", string);
    }
}
