package bot.Battle.Encounter;

import bot.Battle.BattleContext;
import bot.Battle.CombatCreature;
import bot.Battle.CombatExplorer;
import bot.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SummaryMessageBuilder extends bot.Battle.SummaryMessageBuilder
{
    /**
     * {@inheritDoc}
     */
    public @NotNull ArrayList<String> buildSummary(@NotNull BattleContext battleContext)
    {
        ArrayList<String>             messages  = new ArrayList<>();
        ArrayList<CombatExplorer>     explorers = new ArrayList<>();
        ArrayList<EncounteredHostile> hostiles  = new ArrayList<>();

        for (CombatCreature creature : battleContext.getCreatures()) {
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

        return messages;
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
}
