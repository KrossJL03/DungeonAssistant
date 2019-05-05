package bot.Encounter.Logger.MessageBuilder;

import bot.Encounter.EncounterCreatureInterface;
import bot.Encounter.EncounteredCreature.Slayer;
import bot.Encounter.EncounteredExplorerInterface;
import bot.Encounter.EncounteredHostileInterface;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SummaryMessageBuilder
{

    private SummaryMessageFormatter formatter;

    /**
     * Action MessageBuilder constructor
     */
    public @NotNull SummaryMessageBuilder()
    {
        this.formatter = new SummaryMessageFormatter();
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
        ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(WordUtils.capitalize("ENCOUNTER SUMMARY"));
        codeBlockLines.add(MessageConstants.BREAK);
        codeBlockLines.add(MessageConstants.LINE);
        codeBlockLines.add("Hostiles");
        codeBlockLines.add(MessageConstants.LINE);

        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            codeBlockLines.add(getNameLine(encounteredHostile));
            String healthBar = getHealthBarLine(encounteredHostile);
            if (!healthBar.isEmpty()) {
                codeBlockLines.add(healthBar);
            }
        }

        codeBlockLines.add(MessageConstants.LINE);
        codeBlockLines.add("Explorers");
        codeBlockLines.add(MessageConstants.LINE);

        for (EncounteredExplorerInterface encounteredExplorer : encounteredExplorers) {
            codeBlockLines.add(getNameLine(encounteredExplorer));
            String healthBar = getHealthBarLine(encounteredExplorer);
            if (!healthBar.isEmpty()) {
                codeBlockLines.add(healthBar);
            }
        }

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Get health bar line
     *
     * @param creature Creature
     *
     * @return String
     */
    private @NotNull String getHealthBarLine(EncounterCreatureInterface creature)
    {
        StringBuilder output    = new StringBuilder();
        int           currentHP = creature.getCurrentHP();
        int           maxHP     = creature.getMaxHP();

        if (!creature.isActive()) {
            return "";
        }

        output.append(String.format("[%3d/%3d] ", currentHP, maxHP));
        int healthBlocks      = (int) Math.ceil(maxHP / 10) + 1;
        int emptyHealthBlocks = (int) Math.ceil((double) (maxHP - currentHP) / 10);
        int fullHealthBlocks  = healthBlocks - emptyHealthBlocks;
        output.append(this.repeatString(MessageConstants.FULL_HEALTH_ICON, fullHealthBlocks));
        if (emptyHealthBlocks > 0) {
            output.append(this.repeatString(MessageConstants.EMPTY_HEALTH_ICON, emptyHealthBlocks));
        }

        return isLowHealth(creature) ? formatter.makeRed(output.toString()) : formatter.makeGreen(output.toString());
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
            return formatter.makeGray(String.format(
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
            return formatter.makeGray(String.format("%s has left", encounteredExplorer.getName()));
        } else if (encounteredExplorer.isSlain()) {
            Slayer slayer = encounteredExplorer.getSlayer();
            return formatter.makeGray(String.format(
                "%s was knocked out %s",
                encounteredExplorer.getName(),
                slayer.exists() ? String.format(" by %s", slayer.getName()) : ""
            ));
        } else {
            return String.format("%s [%s]", encounteredExplorer.getName(), encounteredExplorer.getOwner().getName());
        }
    }

    /**
     * Is creature considered low health
     *
     * @param creature Creature
     *
     * @return boolean
     */
    private boolean isLowHealth(EncounterCreatureInterface creature)
    {
        return creature.getCurrentHP() < (creature.getMaxHP() / 4);
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
