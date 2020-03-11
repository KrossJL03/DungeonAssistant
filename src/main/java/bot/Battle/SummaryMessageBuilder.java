package bot.Battle;

import bot.Battle.Encounter.EncounteredHostile;
import bot.DiffCodeFormatter;
import bot.Message;
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
     * @return ArrayList
     */
    @NotNull ArrayList<String> buildSummary(@NotNull ArrayList<CombatCreature> creatures)
    {
        ArrayList<String>             messages  = new ArrayList<>();
        ArrayList<CombatExplorer>     explorers = new ArrayList<>();
        ArrayList<EncounteredHostile> hostiles  = new ArrayList<>();

        for (CombatCreature creature : creatures) {
            if (creature instanceof CombatExplorer) {
                explorers.add((CombatExplorer) creature);
            }
            if (creature instanceof EncounteredHostile) {
                hostiles.add((EncounteredHostile) creature);
            }
        }

        if (!explorers.isEmpty()) {
            messages.add(buildExplorerSummary(explorers));
        }
        if (!hostiles.isEmpty()) {
            messages.add(buildHostileSummary(hostiles));
        }
        if (messages.isEmpty()) {
            messages.add(buildEmptySummary());
        }

        return messages;
    }

    /**
     * Build empty summary
     */
    private @NotNull String buildEmptySummary()
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());
        message.add("This battle is empty!");
        message.endCodeBlock();

        return message.getAsString();
    }

    /**
     * Build explorer summary
     *
     * @param explorers Explorers
     */
    private @NotNull String buildExplorerSummary(@NotNull ArrayList<CombatExplorer> explorers)
    {
        Message message = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
        message.addLine();
        message.add("Explorers");
        message.addLine();

        for (CombatExplorer explorer : explorers) {
            message.add(getNameLine(explorer));
            String healthBar = getHealthBarLine(explorer);
            if (!healthBar.isEmpty()) {
                message.add(healthBar);
            }
        }

        message.endCodeBlock();

        return message.getAsString();
    }

    /**
     * Build hostile summary
     *
     * @param hostiles Hostiles
     */
    private @NotNull String buildHostileSummary(@NotNull ArrayList<EncounteredHostile> hostiles)
    {
        Message message = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
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
        int healthBlocks      = (int) Math.ceil((float) maxHP / 10) + 1;
        int emptyHealthBlocks = (int) Math.ceil((double) (maxHP - currentHP) / 10);
        int fullHealthBlocks  = healthBlocks - emptyHealthBlocks;
        output.append(this.repeatString(Message.FULL_HEALTH_ICON, fullHealthBlocks));
        if (emptyHealthBlocks > 0) {
            output.append(this.repeatString(Message.EMPTY_HEALTH_ICON, emptyHealthBlocks));
        }

        return creature.isBloodied()
               ? codeFormatter.makeRed(output.toString())
               : codeFormatter.makeGreen(output.toString());
    }

    /**
     * Get name line
     *
     * @param hostile Hostile
     *
     * @return String
     */
    private @NotNull String getNameLine(@NotNull EncounteredHostile hostile)
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
     * @param explorer Explorer
     *
     * @return String
     */
    private @NotNull String getNameLine(@NotNull CombatExplorer explorer)
    {
        if (!explorer.isPresent()) {
            return codeFormatter.makeGrey(String.format("%s has left", explorer.getName()));
        } else if (explorer.isSlain()) {
            Slayer slayer = explorer.getSlayer();
            return codeFormatter.makeGrey(String.format(
                "%s was defeated %s",
                explorer.getName(),
                slayer.exists() ? String.format("by %s", slayer.getName()) : ""
            ));
        } else {
            return String.format("%s [%s]", explorer.getName(), explorer.getOwner().getName());
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
