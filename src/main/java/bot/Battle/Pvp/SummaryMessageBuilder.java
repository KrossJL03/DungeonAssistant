package bot.Battle.Pvp;

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
        ArrayList<String>         messages       = new ArrayList<>();
        ArrayList<CombatExplorer> aliveExplorers = new ArrayList<>();
        ArrayList<CombatExplorer> slainExplorers = new ArrayList<>();

        for (CombatCreature creature : battleContext.getCreatures()) {
            if (creature instanceof CombatExplorer) {
                if (creature.isSlain()) {
                    slainExplorers.add((CombatExplorer) creature);
                } else {
                    aliveExplorers.add((CombatExplorer) creature);
                }
            }
        }

        if (!aliveExplorers.isEmpty()) {
            messages.add(buildExplorerSummary(aliveExplorers));
        }
        if (!slainExplorers.isEmpty()) {
            messages.add(buildDefeatedExplorerSummary(aliveExplorers));
        }

        return messages;
    }

    /**
     * Build slain explorer summary
     *
     * @param explorers Explorers
     *
     * @return String
     */
    private @NotNull String buildDefeatedExplorerSummary(@NotNull ArrayList<CombatExplorer> explorers)
    {
        Message message = new Message();

        explorers.sort(new DefeatedExplorerComparator());

        message.startCodeBlock(codeFormatter.getStyle());
        message.addLine();
        message.add("Defeated Explorers");
        message.addLine();

        for (int i = 0; i < explorers.size(); i++) {
            CombatExplorer explorer = explorers.get(i);
            message.add(i + " " + getNameLine(explorer));
        }

        message.endCodeBlock();

        return message.getAsString();
    }
}
