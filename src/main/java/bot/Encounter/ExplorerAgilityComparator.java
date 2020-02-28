package bot.Encounter;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class ExplorerAgilityComparator implements Comparator<EncounteredExplorerInterface>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull EncounteredExplorerInterface explorer1, @NotNull EncounteredExplorerInterface explorer2)
    {
        int difference = explorer2.getAgility() - explorer1.getAgility();
        if (difference == 0) {
            difference = explorer1.getJoinedAt().compareTo(explorer2.getJoinedAt());
        }

        return difference;
    }
}