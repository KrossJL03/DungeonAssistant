package bot.Battle;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class ExplorerInitiativeComparator implements Comparator<CombatExplorer>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull CombatExplorer explorer1, @NotNull CombatExplorer explorer2)
    {
        int difference = explorer2.getAgility() - explorer1.getAgility();
        if (difference == 0) {
            difference = explorer1.getJoinedAt().compareTo(explorer2.getJoinedAt());
        }

        return difference;
    }
}