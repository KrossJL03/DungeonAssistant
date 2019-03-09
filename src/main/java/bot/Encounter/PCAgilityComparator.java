package bot.Encounter;

import bot.Encounter.EncounterData.PCEncounterData;

import java.util.Comparator;

public class PCAgilityComparator implements Comparator<PCEncounterData> {

    @Override
    public int compare(PCEncounterData pc1, PCEncounterData pc2) {
        return pc2.getAgility() - pc1.getAgility();
    }
}