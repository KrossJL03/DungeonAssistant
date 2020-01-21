package bot.Battle;

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
        return explorer2.getAgility() - explorer1.getAgility();
    }
}