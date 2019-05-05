package bot.Encounter;

import bot.Encounter.EncounteredCreature.EncounteredExplorer;

import java.util.Comparator;

public class ExplorerAgilityComparator implements Comparator<EncounteredExplorerInterface> {

    @Override
    public int compare(EncounteredExplorerInterface explorer1, EncounteredExplorerInterface explorer2) {
        return explorer2.getAgility() - explorer1.getAgility();
    }
}