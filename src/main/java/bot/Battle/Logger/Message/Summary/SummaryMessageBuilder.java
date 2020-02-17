package bot.Battle.Logger.Message.Summary;

import bot.Battle.CombatCreature;
import bot.Battle.HostileEncounter.EncounteredExplorer;
import bot.Battle.HostileEncounter.EncounteredHostile;
import bot.Battle.DiffCodeFormatter;
import bot.Battle.Slayer;
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
     * @param creatures Creatures
     *
     * @return String
     */
    public @NotNull String buildSummary(@NotNull ArrayList<CombatCreature> creatures)
    {
        ArrayList<EncounteredExplorer> explorers = new ArrayList<>();
        ArrayList<EncounteredHostile>  hostiles  = new ArrayList<>();

        for (CombatCreature creature : creatures) {
            if (creature instanceof EncounteredExplorer) {
                explorers.add((EncounteredExplorer) creature);
            }
            if (creature instanceof EncounteredHostile) {
                hostiles.add((EncounteredHostile) creature);
            }
        }

        if (hostiles.isEmpty()) {
            return buildPvpSummary(explorers);
        } else {
            return buildHostileEncounterSummary(explorers, hostiles);
        }
    }

    /**
     * Add explorers to message
     *
     * @param message   Message
     * @param explorers Explorers
     */
    private void addExplorers(@NotNull SummaryMessage message, @NotNull ArrayList<EncounteredExplorer> explorers)
    {
        message.addLine();
        message.add("Explorers");
        message.addLine();

        for (EncounteredExplorer explorer : explorers) {
            message.add(getNameLine(explorer));
            String healthBar = getHealthBarLine(explorer);
            if (!healthBar.isEmpty()) {
                message.add(healthBar);
            }
        }
    }

    /**
     * Build hostile encounter summary
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     *
     * @return String
     */
    private @NotNull String buildHostileEncounterSummary(
        @NotNull ArrayList<EncounteredExplorer> explorers,
        @NotNull ArrayList<EncounteredHostile> hostiles
    )
    {
        SummaryMessage message = new SummaryMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(WordUtils.capitalize("HOSTILE ENCOUNTER SUMMARY"));
        message.addBreak();
        message.addLine();
        message.add("Hostiles");
        message.addLine();

        for (EncounteredHostile hostile : hostiles) {
            message.add(getNameLine(hostile));
            String healthBar = getHealthBarLine(hostile);
            if (!healthBar.isEmpty()) {
                message.add(healthBar);
            }
        }

        addExplorers(message, explorers);
        message.endCodeBlock();

        return message.getAsString();
    }

    /**
     * Build pvp summary
     *
     * @param explorers Explorers
     *
     * @return String
     */
    private @NotNull String buildPvpSummary(@NotNull ArrayList<EncounteredExplorer> explorers)
    {
        SummaryMessage message = new SummaryMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(WordUtils.capitalize("PVP SUMMARY"));
        message.addBreak();
        addExplorers(message, explorers);
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
    private @NotNull String getHealthBarLine(CombatCreature creature)
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
     * @param hostile Encountered hostile
     *
     * @return String
     */
    private @NotNull String getNameLine(EncounteredHostile hostile)
    {
        if (hostile.isSlain()) {
            Slayer slayer = hostile.getSlayer();
            return codeFormatter.makeGrey(String.format(
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
     * @param encounteredExplorer Encountered explorer
     *
     * @return String
     */
    private @NotNull String getNameLine(EncounteredExplorer encounteredExplorer)
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
