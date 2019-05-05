package bot.Encounter.Logger.MessageBuilder;

import bot.Encounter.EncounterData.EncounterDataInterface;
import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.EncounterData.Slayer;
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
     * @param explorers Explorers
     * @param hostiles  Hostiles
     *
     * @return String
     */
    public @NotNull String buildSummary(ArrayList<PCEncounterData> explorers, ArrayList<HostileEncounterData> hostiles)
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(WordUtils.capitalize("ENCOUNTER SUMMARY"));
        codeBlockLines.add(MessageConstants.BREAK);
        codeBlockLines.add(MessageConstants.LINE);
        codeBlockLines.add("Hostiles");
        codeBlockLines.add(MessageConstants.LINE);

        for (HostileEncounterData hostile : hostiles) {
            codeBlockLines.add(getNameLine(hostile));
            String healthBar = getHealthBarLine(hostile);
            if (!healthBar.isEmpty()) {
                codeBlockLines.add(healthBar);
            }
        }

        codeBlockLines.add(MessageConstants.LINE);
        codeBlockLines.add("Explorers");
        codeBlockLines.add(MessageConstants.LINE);

        for (PCEncounterData explorer : explorers) {
            codeBlockLines.add(getNameLine(explorer));
            String healthBar = getHealthBarLine(explorer);
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
    private @NotNull String getHealthBarLine(EncounterDataInterface creature)
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
     * @param hostile Hostile
     *
     * @return String
     */
    private @NotNull String getNameLine(HostileEncounterData hostile)
    {
        if (hostile.isSlain()) {
            Slayer slayer = hostile.getSlayer();
            return formatter.makeGray(String.format(
                "%s was slain %s",
                hostile.getName(),
                slayer.exists() ? String.format(" by %s", slayer.getName()) : ""
            ));
        } else {
            return hostile.getName();
        }
    }

    /**
     * Get name line
     *
     * @param explorer Explorer
     *
     * @return String
     */
    private @NotNull String getNameLine(PCEncounterData explorer)
    {
        if (!explorer.isPresent()) {
            return formatter.makeGray(String.format("%s has left", explorer.getName()));
        } else if (explorer.isSlain()) {
            Slayer slayer = explorer.getSlayer();
            return formatter.makeGray(String.format(
                "%s was knocked out %s",
                explorer.getName(),
                slayer.exists() ? String.format(" by %s", slayer.getName()) : ""
            ));
        } else {
            return String.format("%s [%s]", explorer.getName(), explorer.getOwner().getName());
        }
    }

    /**
     * Is creature considered low health
     *
     * @param creature Creature
     *
     * @return boolean
     */
    private boolean isLowHealth(EncounterDataInterface creature)
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
