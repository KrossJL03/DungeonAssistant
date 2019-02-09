package bot.Encounter;

import bot.Encounter.EncounterData.PCEncounterData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PCAgilityComparatorTest {

    @Test
    @DisplayName("Compare: 2 PCs with different agility stats")
    void compareTest1() {
        PCAgilityComparator comparator = new PCAgilityComparator();
        PCEncounterData     pc1        = this.mockPCEncounterData(10);
        PCEncounterData     pc2        = this.mockPCEncounterData(9);

        assertEquals(-1, comparator.compare(pc1,pc2));
        assertEquals(1, comparator.compare(pc2,pc1));
    }


    @Test
    @DisplayName("Compare: 2 PCs with the same agility stat")
    void compareTest2() {
        PCAgilityComparator comparator = new PCAgilityComparator();
        PCEncounterData     pc1        = this.mockPCEncounterData(10);
        PCEncounterData     pc2        = this.mockPCEncounterData(10);

        assertEquals(0, comparator.compare(pc1,pc2));
        assertEquals(0, comparator.compare(pc2,pc1));
    }

    /**
     * Mock PCEncounterData
     *
     * @return PCEncounterData
     */
    private PCEncounterData mockPCEncounterData(int agility) {
        PCEncounterData mock = mock(PCEncounterData.class);
        when(mock.getAgility()).thenReturn(agility);
        return mock;
    }
}
