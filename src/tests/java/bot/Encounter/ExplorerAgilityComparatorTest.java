package bot.Encounter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExplorerAgilityComparatorTest
{
    @Test
    @DisplayName("Compare: 2 explorers with different agility stats")
    void compareTest1()
    {
        ExplorerAgilityComparator    comparator = new ExplorerAgilityComparator();
        EncounteredExplorerInterface explorer1  = this.mockEncounteredExplorerInterface(10);
        EncounteredExplorerInterface explorer2  = this.mockEncounteredExplorerInterface(9);

        assertEquals(-1, comparator.compare(explorer1, explorer2));
        assertEquals(1, comparator.compare(explorer2, explorer1));
    }


    @Test
    @DisplayName("Compare: 2 explorers with the same agility stat")
    void compareTest2()
    {
        ExplorerAgilityComparator    comparator = new ExplorerAgilityComparator();
        EncounteredExplorerInterface explorer1  = this.mockEncounteredExplorerInterface(10);
        EncounteredExplorerInterface explorer2  = this.mockEncounteredExplorerInterface(10);

        assertEquals(0, comparator.compare(explorer1, explorer2));
        assertEquals(0, comparator.compare(explorer2, explorer1));
    }

    /**
     * Mock EncounteredExplorerInterface
     *
     * @return EncounteredExplorerInterface
     */
    private EncounteredExplorerInterface mockEncounteredExplorerInterface(int agility)
    {
        EncounteredExplorerInterface mock = mock(EncounteredExplorerInterface.class);
        when(mock.getAgility()).thenReturn(agility);
        return mock;
    }
}
