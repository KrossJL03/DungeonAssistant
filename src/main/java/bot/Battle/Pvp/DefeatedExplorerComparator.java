package bot.Battle.Pvp;

import bot.Battle.CombatExplorer;
import bot.CustomException;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Comparator;

public class DefeatedExplorerComparator implements Comparator<CombatExplorer>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull CombatExplorer explorer1, @NotNull CombatExplorer explorer2)
    {
        ZonedDateTime explorer1SlainAt = explorer1.getSlayer().getSlainAt();
        ZonedDateTime explorer2SlainAt = explorer2.getSlayer().getSlainAt();

        if (explorer1SlainAt == null) {
            throw new CustomException(String.format("%s has not been slain.", explorer1.getName()));
        }
        if (explorer2SlainAt == null) {
            throw new CustomException(String.format("%s has not been slain", explorer2.getName()));
        }

        return explorer1SlainAt.compareTo(explorer2SlainAt);
    }
}
